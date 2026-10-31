package pl.miloszgilga.jargonaut.page.home.model;

import pl.miloszgilga.jargonaut.core.extractor.contributions.ContributionCalendar;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeSharedData {
  private Map<Locale, ContributionCalendar> contributionCalendars = new HashMap<>();

  public Map<Locale, ContributionCalendar> getContributionCalendars() {
    return contributionCalendars;
  }

  public void setContributionCalendars(Map<Locale, ContributionCalendar> contributionCalendars) {
    this.contributionCalendars = contributionCalendars;
  }
}
