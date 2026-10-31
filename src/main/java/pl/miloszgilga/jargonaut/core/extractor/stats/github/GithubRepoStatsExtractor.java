package pl.miloszgilga.jargonaut.core.extractor.stats.github;

import pl.miloszgilga.jargonaut.core.event.BuildEventBus;
import pl.miloszgilga.jargonaut.core.extractor.GenericGithubDataExtractor;
import pl.miloszgilga.jargonaut.core.extractor.GithubExtractorConfig;
import pl.miloszgilga.jargonaut.core.extractor.contributions.ContributionCalendar;
import pl.miloszgilga.jargonaut.core.extractor.stats.LanguageStat;
import pl.miloszgilga.jargonaut.core.extractor.stats.RepoStats;
import pl.miloszgilga.jargonaut.core.extractor.stats.RepoStatsExtractor;
import pl.miloszgilga.jargonaut.core.graphql.adapter.BasicGraphQlTemplate;
import pl.miloszgilga.jargonaut.core.parser.json.JsonParser;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GithubRepoStatsExtractor extends GenericGithubDataExtractor implements RepoStatsExtractor {
  protected static final Pattern GITHUB_REPO_URL_PATTERN = Pattern.compile(
    "^https?://(?:www\\.)?github\\.com/([^/]+)/([^/]+?)(?:\\.git)?/?$"
  );

  public GithubRepoStatsExtractor(BuildEventBus eventBus, GithubExtractorConfig config, JsonParser jsonParser) {
    super(eventBus, config, jsonParser);
  }

  @Override
  protected Pattern getUrlPattern() {
    return GITHUB_REPO_URL_PATTERN;
  }

  @Override
  public Map<String, RepoStats> extractRepoStats(Set<String> matchedRepoUrls) {
    if (matchedRepoUrls == null || matchedRepoUrls.isEmpty()) {
      return Map.of();
    }
    final LocalDate today = LocalDate.now(ZoneOffset.UTC);
    final LocalDate startGridDate = today.minusWeeks(52).with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
    final String sinceIso = startGridDate.atStartOfDay(ZoneOffset.UTC).toInstant().toString();

    final Map<String, List<LanguageStat>> repoLanguages = new HashMap<>();
    final Map<String, Instant> repoLastCommits = new HashMap<>();
    final Map<String, List<GithubRepoStatsGraphQlRes.CommitNode>> repoCommits = new HashMap<>();
    final Map<String, String> cursors = new HashMap<>();

    Set<String> activeUrls = new HashSet<>(matchedRepoUrls);

    while (!activeUrls.isEmpty()) {
      final Map<String, String> aliasToOriginalUrl = new HashMap<>();
      final String reposQueryPart = buildBatchQuery(activeUrls, cursors, aliasToOriginalUrl);
      final GithubRepoStatsGraphQlRes response = graphQlClient.executeFromTemplate(
        BasicGraphQlTemplate.GITHUB_REPO_STATS,
        Map.of("REPOSITORIES", reposQueryPart),
        Map.of("since", sinceIso),
        GithubRepoStatsGraphQlRes.class
      );
      if (response == null || response.data() == null) {
        break;
      }
      activeUrls = processBatchResponse(response, aliasToOriginalUrl, cursors, repoLanguages, repoLastCommits,
        repoCommits);
    }
    return buildFinalResult(matchedRepoUrls, repoLanguages, repoLastCommits, repoCommits, startGridDate, today);
  }

  private String buildBatchQuery(Set<String> activeUrls,
                                 Map<String, String> cursors,
                                 Map<String, String> aliasToOriginalUrl) {
    final StringBuilder reposPart = new StringBuilder();
    int index = 0;
    for (final String url : activeUrls) {
      final Matcher matcher = GITHUB_REPO_URL_PATTERN.matcher(url);
      if (matcher.matches()) {
        final String owner = matcher.group(1);
        final String repo = matcher.group(2);
        final String alias = "repo" + index++;
        aliasToOriginalUrl.put(alias, url);

        final String cursor = cursors.get(url);
        final String afterArg = cursor != null ? ", after: \"" + cursor + "\"" : "";

        reposPart.append(String.format("""
          %s: repository(owner: "%s", name: "%s") {
            ...RepoStatsFragment
            defaultBranchRef {
              target {
                ... on Commit {
                  yearlyCommits: history(first: 100, since: $since%s) {
                    pageInfo { hasNextPage endCursor }
                    nodes { committedDate }
                  }
                }
              }
            }
          }
          """, alias, owner, repo, afterArg));
      }
    }
    return reposPart.toString();
  }

  private Set<String> processBatchResponse(GithubRepoStatsGraphQlRes response,
                                           Map<String, String> aliasToOriginalUrl,
                                           Map<String, String> cursors,
                                           Map<String, List<LanguageStat>> repoLanguages,
                                           Map<String, Instant> repoLastCommits,
                                           Map<String, List<GithubRepoStatsGraphQlRes.CommitNode>> repoCommits) {
    final Set<String> nextActiveUrls = new HashSet<>();
    for (final Map.Entry<String, GithubRepoStatsGraphQlRes.Data> entry : response.data().entrySet()) {
      final String alias = entry.getKey();
      final GithubRepoStatsGraphQlRes.Data repoData = entry.getValue();
      final String originalUrl = aliasToOriginalUrl.get(alias);
      if (originalUrl == null || repoData == null) {
        continue;
      }
      if (!cursors.containsKey(originalUrl)) {
        repoLanguages.put(originalUrl, extractLanguages(repoData));
        extractLastCommit(repoData).ifPresent(date -> repoLastCommits.put(originalUrl, date));
      }
      if (hasYearlyCommits(repoData)) {
        final GithubRepoStatsGraphQlRes.CommitHistory history = repoData.defaultBranchRef().target().yearlyCommits();
        if (history.nodes() != null) {
          repoCommits.computeIfAbsent(originalUrl, k -> new ArrayList<>()).addAll(history.nodes());
        }
        if (history.pageInfo() != null && history.pageInfo().hasNextPage()) {
          cursors.put(originalUrl, history.pageInfo().endCursor());
          nextActiveUrls.add(originalUrl);
        }
      }
    }
    return nextActiveUrls;
  }

  private List<LanguageStat> extractLanguages(GithubRepoStatsGraphQlRes.Data repoData) {
    final List<LanguageStat> statsList = new ArrayList<>();
    if (repoData.languages() != null && repoData.languages().edges() != null) {
      for (final GithubRepoStatsGraphQlRes.LanguageEdge edge : repoData.languages().edges()) {
        if (edge.node() != null) {
          statsList.add(new LanguageStat(edge.node().name(), edge.size(), edge.node().color()));
        }
      }
    }
    return statsList;
  }

  private Optional<Instant> extractLastCommit(GithubRepoStatsGraphQlRes.Data repoData) {
    if (repoData.defaultBranchRef() == null || repoData.defaultBranchRef().target() == null) {
      return Optional.empty();
    }
    final GithubRepoStatsGraphQlRes.Target target = repoData.defaultBranchRef().target();
    if (target.latestCommit() != null
      && target.latestCommit().nodes() != null
      && !target.latestCommit().nodes().isEmpty()) {
      final String rawDate = target.latestCommit().nodes().getFirst().committedDate();
      if (rawDate != null) {
        return Optional.of(Instant.parse(rawDate));
      }
    }
    return Optional.empty();
  }

  private boolean hasYearlyCommits(GithubRepoStatsGraphQlRes.Data repoData) {
    return repoData.defaultBranchRef() != null
      && repoData.defaultBranchRef().target() != null
      && repoData.defaultBranchRef().target().yearlyCommits() != null;
  }

  private Map<String, RepoStats> buildFinalResult(Set<String> matchedRepoUrls,
                                                  Map<String, List<LanguageStat>> repoLanguages,
                                                  Map<String, Instant> repoLastCommits,
                                                  Map<String, List<GithubRepoStatsGraphQlRes.CommitNode>> repoCommits,
                                                  LocalDate startGridDate,
                                                  LocalDate today) {
    final Map<String, RepoStats> resultMap = new HashMap<>();
    for (final String url : matchedRepoUrls) {
      final List<LanguageStat> langs = repoLanguages.getOrDefault(url, List.of());
      final Instant lastCommit = repoLastCommits.get(url);
      final List<GithubRepoStatsGraphQlRes.CommitNode> commits = repoCommits.getOrDefault(url, List.of());
      final ContributionCalendar calendar = buildCalendar(commits, startGridDate, today);
      resultMap.put(url, new RepoStats(langs, lastCommit, calendar));
    }
    return resultMap;
  }

  private ContributionCalendar buildCalendar(List<GithubRepoStatsGraphQlRes.CommitNode> commitNodes,
                                             LocalDate startGridDate,
                                             LocalDate today) {
    final Map<LocalDate, Integer> dailyCounts = new HashMap<>();
    int totalContributions = 0;
    int maxDaily = 0;
    for (final GithubRepoStatsGraphQlRes.CommitNode node : commitNodes) {
      if (node != null && node.committedDate() != null) {
        final LocalDate date = Instant.parse(node.committedDate()).atZone(ZoneOffset.UTC).toLocalDate();
        int currentDaySum = dailyCounts.merge(date, 1, Integer::sum);
        if (currentDaySum > maxDaily) {
          maxDaily = currentDaySum;
        }
        totalContributions++;
      }
    }
    final List<ContributionCalendar.ContributionWeek> weeks = new ArrayList<>();
    List<ContributionCalendar.ContributionDay> currentWeek = new ArrayList<>(7);
    for (LocalDate date = startGridDate; !date.isAfter(today); date = date.plusDays(1)) {
      final int count = dailyCounts.getOrDefault(date, 0);
      final int level = ContributionCalendar.ContributionDay.calculateLevel(count, maxDaily);
      currentWeek.add(new ContributionCalendar.ContributionDay(count, date, level));
      if (currentWeek.size() == 7) {
        weeks.add(new ContributionCalendar.ContributionWeek(currentWeek));
        currentWeek = new ArrayList<>(7);
      }
    }
    if (!currentWeek.isEmpty()) {
      weeks.add(new ContributionCalendar.ContributionWeek(currentWeek));
    }
    return new ContributionCalendar(totalContributions, weeks);
  }
}
