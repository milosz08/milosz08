package pl.miloszgilga.jargonaut.page.education.dto;

import pl.miloszgilga.jargonaut.page.education.model.EducationItem;

import java.util.List;

public record EducationView(
  String spec,
  String org,
  String location,
  String type,
  String dateStart,
  String dateEnd,
  String grade,
  String thesisDescription,
  String thesisUrl,
  List<String> additionalAchievements
) {
  public EducationView(EducationItem educationItem) {
    this(
      educationItem.getSpec(),
      educationItem.getOrg(),
      educationItem.getLocation(),
      educationItem.getType(),
      educationItem.getStart().asText(),
      educationItem.getEnd() != null ? educationItem.getEnd().asText() : null,
      educationItem.getGrade(),
      educationItem.getThesis() != null ? educationItem.getThesis().getDescription() : null,
      educationItem.getThesis() != null ? educationItem.getThesis().getUrl() : null,
      educationItem.getAdditionalAchievements()
    );
  }
}
