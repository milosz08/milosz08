package pl.miloszgilga.jargonaut.core.extractor.stats;

import pl.miloszgilga.jargonaut.core.extractor.contributions.ContributionCalendar;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public record RepoStats(
  List<LanguageStat> languages,
  Instant lastCommitDate,
  ContributionCalendar contributionCalendar
) {
  public static RepoStats aggregate(List<RepoStats> statsList) {
    if (statsList == null || statsList.isEmpty()) {
      return new RepoStats(List.of(), null, new ContributionCalendar(0, List.of()));
    }
    final List<List<LanguageStat>> rawLanguageLists = statsList.stream()
      .filter(Objects::nonNull)
      .map(RepoStats::languages)
      .filter(Objects::nonNull)
      .toList();
    final List<LanguageStat> aggregatedLanguages = LanguageStat.aggregate(rawLanguageLists);
    final Instant latestCommit = statsList.stream()
      .filter(Objects::nonNull)
      .map(RepoStats::lastCommitDate)
      .filter(Objects::nonNull)
      .max(Comparator.naturalOrder())
      .orElse(null);
    final List<ContributionCalendar> calendars = statsList.stream()
      .map(s -> s.contributionCalendar)
      .filter(Objects::nonNull)
      .toList();
    final ContributionCalendar mergedCalendar = ContributionCalendar.mergeCalendars(calendars);
    return new RepoStats(aggregatedLanguages, latestCommit, mergedCalendar);
  }
}
