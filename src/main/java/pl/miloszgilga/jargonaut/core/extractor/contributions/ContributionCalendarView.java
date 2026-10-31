package pl.miloszgilga.jargonaut.core.extractor.contributions;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public record ContributionCalendarView(
  int totalContributions,
  List<ContributionCalendar.ContributionWeek> weeks,
  List<String> monthLabels,
  List<String> dayLabels
) {
  public ContributionCalendarView(Map<Locale, ContributionCalendar> contributionCalendars, Locale locale) {
    this(
      contributionCalendars.get(locale).totalContributions(),
      contributionCalendars.get(locale).weeks(),
      buildMonthLabels(contributionCalendars.get(locale).weeks(), locale),
      buildDayLabels(locale)
    );
  }

  public ContributionCalendarView(ContributionCalendar contributionCalendar, Locale locale) {
    this(
      contributionCalendar.totalContributions(),
      contributionCalendar.weeks(),
      buildMonthLabels(contributionCalendar.weeks(), locale),
      buildDayLabels(locale)
    );
  }

  public static List<String> buildMonthLabels(List<ContributionCalendar.ContributionWeek> weeks, Locale locale) {
    final List<String> labels = new ArrayList<>();
    if (weeks == null || weeks.isEmpty()) {
      return labels;
    }
    final int size = weeks.size();
    final List<LocalDate> startDates = new ArrayList<>(size);
    for (final ContributionCalendar.ContributionWeek week : weeks) {
      if (week.contributionDays() != null && !week.contributionDays().isEmpty()) {
        startDates.add(week.contributionDays().getFirst().date());
      } else {
        startDates.add(null);
      }
    }
    int currentMonth = -1;
    final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM", locale);
    for (int i = 0; i < size; i++) {
      final LocalDate date = startDates.get(i);
      String label = "";
      if (date != null) {
        final int month = date.getMonthValue();
        if (month != currentMonth) {
          boolean show = true;
          if (i == 0 && (i + 1 < size)) {
            final LocalDate nextDate = startDates.get(i + 1);
            if (nextDate != null && month != nextDate.getMonthValue()) {
              show = false;
            }
          }
          if (i == size - 1) {
            show = false;
          }
          if (show) {
            label = date.format(monthFormatter);
            label = capitalize(label, locale);
          }
          currentMonth = month;
        }
      }
      labels.add(label);
    }
    return labels;
  }

  public static List<String> buildDayLabels(Locale locale) {
    final List<String> days = new ArrayList<>(7);
    for (int i = 0; i < 7; i++) {
      days.add("");
    }
    days.set(1, getCleanDayLabel(DayOfWeek.MONDAY, locale));
    days.set(3, getCleanDayLabel(DayOfWeek.WEDNESDAY, locale));
    days.set(5, getCleanDayLabel(DayOfWeek.FRIDAY, locale));
    return days;
  }

  private static String getCleanDayLabel(DayOfWeek dayOfWeek, Locale locale) {
    String label = dayOfWeek.getDisplayName(TextStyle.SHORT, locale);
    label = label.replace(".", "");
    return capitalize(label, locale);
  }

  private static String capitalize(String text, Locale locale) {
    if (text == null || text.isEmpty()) {
      return text;
    }
    return text.substring(0, 1).toUpperCase(locale) + text.substring(1);
  }
}
