package pl.miloszgilga.jargonaut.core.extractor.contributions.github;

import pl.miloszgilga.jargonaut.core.event.BuildEventBus;
import pl.miloszgilga.jargonaut.core.extractor.GenericGithubDataExtractor;
import pl.miloszgilga.jargonaut.core.extractor.GithubExtractorConfig;
import pl.miloszgilga.jargonaut.core.extractor.contributions.ContributionCalendar;
import pl.miloszgilga.jargonaut.core.extractor.contributions.UserContributionsExtractor;
import pl.miloszgilga.jargonaut.core.graphql.adapter.BasicGraphQlTemplate;
import pl.miloszgilga.jargonaut.core.parser.json.JsonParser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GithubUserContributionsExtractor extends GenericGithubDataExtractor implements UserContributionsExtractor {
  private static final Pattern GITHUB_USER_URL_PATTERN = Pattern.compile(
    "^https?://(?:www\\.)?github\\.com/([^/]+)/?$"
  );

  public GithubUserContributionsExtractor(BuildEventBus eventBus, GithubExtractorConfig config, JsonParser jsonParser) {
    super(eventBus, config, jsonParser);
  }

  @Override
  protected Pattern getUrlPattern() {
    return GITHUB_USER_URL_PATTERN;
  }

  @Override
  public ContributionCalendar extractUserCalendar(String principalId) {
    final GithubUserCalendarGraphQlRes response = graphQlClient.executeFromTemplate(
      BasicGraphQlTemplate.GITHUB_USER_CALENDAR,
      Map.of(),
      Map.of("username", principalId),
      GithubUserCalendarGraphQlRes.class
    );
    if (response == null || response.data() == null || response.data().user() == null) {
      return new ContributionCalendar(0, List.of());
    }
    final GithubUserCalendarGraphQlRes.CalendarNode rawCalendar = response.data()
      .user().contributionsCollection().contributionCalendar();
    int maxDaily = 0;
    for (final GithubUserCalendarGraphQlRes.WeekNode week : rawCalendar.weeks()) {
      for (final GithubUserCalendarGraphQlRes.DayNode day : week.contributionDays()) {
        if (day.contributionCount() > maxDaily) {
          maxDaily = day.contributionCount();
        }
      }
    }
    final List<ContributionCalendar.ContributionWeek> mappedWeeks = createMappedWeeks(rawCalendar, maxDaily);
    return new ContributionCalendar(rawCalendar.totalContributions(), mappedWeeks);
  }

  @Override
  public String extractPrincipalId(String url) {
    if (url == null) {
      return null;
    }
    final Matcher matcher = GITHUB_USER_URL_PATTERN.matcher(url);
    if (matcher.matches()) {
      return matcher.group(1);
    }
    return null;
  }

  private List<ContributionCalendar.ContributionWeek> createMappedWeeks(
    GithubUserCalendarGraphQlRes.CalendarNode rawCalendar,
    int maxDaily
  ) {
    final List<ContributionCalendar.ContributionWeek> mappedWeeks = new ArrayList<>();
    for (final GithubUserCalendarGraphQlRes.WeekNode week : rawCalendar.weeks()) {
      final List<ContributionCalendar.ContributionDay> mappedDays = new ArrayList<>();
      for (final GithubUserCalendarGraphQlRes.DayNode day : week.contributionDays()) {
        final int count = day.contributionCount();
        final LocalDate date = LocalDate.parse(day.date());
        final int level = ContributionCalendar.ContributionDay.calculateLevel(count, maxDaily);
        mappedDays.add(new ContributionCalendar.ContributionDay(count, date, level));
      }
      mappedWeeks.add(new ContributionCalendar.ContributionWeek(mappedDays));
    }
    return mappedWeeks;
  }
}
