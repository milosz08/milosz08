package pl.miloszgilga.jargonaut.page.education.model;

import pl.miloszgilga.jargonaut.core.DataUtils;
import pl.miloszgilga.jargonaut.core.GenerationHaltedException;
import pl.miloszgilga.jargonaut.core.parser.GenericMdDocument;
import pl.miloszgilga.jargonaut.core.parser.validation.YamlNested;
import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;
import pl.miloszgilga.jargonaut.page.education.TimelineItem;

import java.util.Comparator;
import java.util.List;

public class EducationMetadata extends GenericMdDocument<EducationMetadata> {
  @YamlNested
  private Resume resume;

  @YamlRequired
  @YamlNested
  private List<EducationItem> education;

  @YamlRequired
  @YamlNested
  private List<ResearchItem> research;

  @Override
  protected EducationMetadata createInstance() {
    return new EducationMetadata();
  }

  public List<EducationItem> getEducation() {
    return education;
  }

  public void setEducation(List<EducationItem> education) {
    this.education = education;
  }

  public List<ResearchItem> getResearch() {
    return research;
  }

  public void setResearch(List<ResearchItem> research) {
    this.research = research;
  }

  public Resume getResume() {
    return resume;
  }

  public void setResume(Resume resume) {
    this.resume = resume;
  }

  public void validateChronology() {
    if (education != null) {
      for (final EducationItem item : education) {
        validateDates(item.getStart(), item.getEnd(), item.getSpec());
      }
    }
    if (research != null) {
      for (final ResearchItem item : research) {
        validateDates(item.getStart(), item.getEnd(), item.getName());
      }
    }
  }

  @Override
  protected void mergeAdditionalFields(EducationMetadata merged, EducationMetadata override) {
    merged.resume = DataUtils.mergeObject(resume, override.resume);
    merged.education = DataUtils.mergeList(education, override.education);
    merged.research = DataUtils.mergeList(research, override.research);
  }

  public void sortItems(Comparator<? super TimelineItem> comparator) {
    if (education != null) {
      education.sort(comparator);
    }
    if (research != null) {
      research.sort(comparator);
    }
  }

  private void validateDates(DateNode start, DateNode end, String itemName) {
    if (start != null) {
      start.validate();
    }
    if (end != null) {
      end.validate();
    }
    if (!(start != null && start.getYear() != null && end != null && end.getYear() != null)) {
      return;
    }
    if (start.getYear() > end.getYear()) {
      throw new GenerationHaltedException(
        String.format("Validation error in '%s': start year (%d) is after end year (%d)",
          itemName, start.getYear(), end.getYear())
      );
    }
    if (start.getYear().equals(end.getYear())) {
      if (start.getMonth() != null && end.getMonth() != null && start.getMonth() > end.getMonth()) {
        throw new GenerationHaltedException(
          String.format("Validation error in '%s': start month (%d) is after end month (%d) in the same year.",
            itemName, start.getMonth(), end.getMonth())
        );
      }
    }
  }
}
