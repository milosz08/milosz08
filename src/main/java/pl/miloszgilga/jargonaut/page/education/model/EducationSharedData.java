package pl.miloszgilga.jargonaut.page.education.model;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EducationSharedData {
  private Map<Locale, List<EducationItem>> educationItems = new HashMap<>();
  private Map<Locale, List<ResearchItem>> researchItems = new HashMap<>();

  public Map<Locale, List<EducationItem>> getEducationItems() {
    return educationItems;
  }

  public void setEducationItems(Map<Locale, List<EducationItem>> educationItems) {
    this.educationItems = educationItems;
  }

  public Map<Locale, List<ResearchItem>> getResearchItems() {
    return researchItems;
  }

  public void setResearchItems(Map<Locale, List<ResearchItem>> researchItems) {
    this.researchItems = researchItems;
  }
}
