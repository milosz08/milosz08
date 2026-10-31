package pl.miloszgilga.jargonaut.core.extractor.contributions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record ContributionCalendar(int totalContributions, List<ContributionWeek> weeks) {
  public static ContributionCalendar mergeCalendars(List<ContributionCalendar> contributionCalendars) {
    final List<ContributionCalendar> calendars = contributionCalendars.stream()
      .filter(Objects::nonNull)
      .filter(c -> c.weeks() != null && !c.weeks().isEmpty())
      .toList();
    if (calendars.isEmpty()) {
      return new ContributionCalendar(0, List.of());
    }
    if (calendars.size() == 1) {
      return calendars.getFirst();
    }
    final Map<LocalDate, Integer> dailySums = new HashMap<>();
    int totalContributions = 0;
    int maxDaily = 0;
    for (final ContributionCalendar cal : calendars) {
      totalContributions += cal.totalContributions();
      for (final ContributionWeek week : cal.weeks()) {
        for (final ContributionDay day : week.contributionDays()) {
          int mergedCount = dailySums.merge(day.date(), day.contributionCount(), Integer::sum);
          if (mergedCount > maxDaily) {
            maxDaily = mergedCount;
          }
        }
      }
    }
    final List<ContributionWeek> mergedWeeks = new ArrayList<>();
    for (final ContributionWeek templateWeek : calendars.getFirst().weeks()) {
      final List<ContributionDay> mergedDays = new ArrayList<>();
      for (final ContributionDay templateDay : templateWeek.contributionDays()) {
        final int count = dailySums.getOrDefault(templateDay.date(), 0);
        final int level = ContributionDay.calculateLevel(count, maxDaily);
        mergedDays.add(new ContributionDay(count, templateDay.date(), level));
      }
      mergedWeeks.add(new ContributionWeek(mergedDays));
    }
    return new ContributionCalendar(totalContributions, mergedWeeks);
  }

  public record ContributionWeek(List<ContributionDay> contributionDays) {
  }

  public record ContributionDay(int contributionCount, LocalDate date, int level) {
    public static int calculateLevel(int count, int maxDaily) {
      if (count == 0 || maxDaily == 0) {
        return 0;
      }
      return (int) Math.ceil(((double) count / maxDaily) * 4.0);
    }
  }
}
