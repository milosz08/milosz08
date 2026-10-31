package pl.miloszgilga.jargonaut.page.education.model;

import pl.miloszgilga.jargonaut.core.DataUtils;
import pl.miloszgilga.jargonaut.core.parser.validation.YamlNested;
import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;
import pl.miloszgilga.jargonaut.core.processor.MergeableMetadata;
import pl.miloszgilga.jargonaut.page.education.TimelineItem;

import java.util.List;

public class EducationItem implements TimelineItem, MergeableMetadata<EducationItem> {
  @YamlRequired
  private String spec;

  @YamlRequired
  private String org;

  @YamlRequired
  private String location;

  @YamlRequired
  private String type;

  @YamlRequired
  @YamlNested
  private DateNode start;

  @YamlNested
  private DateNode end;

  private String grade;

  @YamlNested
  private Thesis thesis;

  private List<String> additionalAchievements;

  public EducationItem() {
  }

  @Override
  public EducationItem merge(EducationItem override) {
    if (override == null) {
      return this;
    }
    final EducationItem merged = new EducationItem();
    merged.spec = DataUtils.mergeValues(spec, override.spec);
    merged.org = DataUtils.mergeValues(org, override.org);
    merged.location = DataUtils.mergeValues(location, override.location);
    merged.type = DataUtils.mergeValues(type, override.type);
    merged.start = DataUtils.mergeObject(start, override.start);
    merged.end = DataUtils.mergeObject(end, override.end);
    merged.grade = DataUtils.mergeValues(grade, override.grade);
    merged.additionalAchievements = DataUtils.mergeValues(additionalAchievements, override.additionalAchievements);
    merged.thesis = DataUtils.mergeObject(thesis, override.thesis);
    return merged;
  }

  public String getSpec() {
    return spec;
  }

  public void setSpec(String spec) {
    this.spec = spec;
  }

  public String getOrg() {
    return org;
  }

  public void setOrg(String org) {
    this.org = org;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public DateNode getStart() {
    return start;
  }

  public void setStart(DateNode start) {
    this.start = start;
  }

  @Override
  public DateNode getEnd() {
    return end;
  }

  public void setEnd(DateNode end) {
    this.end = end;
  }

  public String getGrade() {
    return grade;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }

  public Thesis getThesis() {
    return thesis;
  }

  public void setThesis(Thesis thesis) {
    this.thesis = thesis;
  }

  public List<String> getAdditionalAchievements() {
    return additionalAchievements;
  }

  public void setAdditionalAchievements(List<String> additionalAchievements) {
    this.additionalAchievements = additionalAchievements;
  }
}
