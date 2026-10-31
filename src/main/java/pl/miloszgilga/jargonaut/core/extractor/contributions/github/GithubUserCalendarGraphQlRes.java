package pl.miloszgilga.jargonaut.core.extractor.contributions.github;

import java.util.List;

public record GithubUserCalendarGraphQlRes(Data data) {
  public record Data(UserNode user) {
  }

  public record UserNode(ContributionsCollection contributionsCollection) {
  }

  public record ContributionsCollection(CalendarNode contributionCalendar) {
  }

  public record CalendarNode(int totalContributions, List<WeekNode> weeks) {
  }

  public record WeekNode(List<DayNode> contributionDays) {
  }

  public record DayNode(int contributionCount, String date) {
  }
}
