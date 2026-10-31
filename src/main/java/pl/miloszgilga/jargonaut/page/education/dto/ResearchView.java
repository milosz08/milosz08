package pl.miloszgilga.jargonaut.page.education.dto;

import pl.miloszgilga.jargonaut.page.education.model.ResearchItem;

public record ResearchView(
  String name,
  String description,
  String dateStart,
  String dateEnd,
  String org,
  String url
) {
  public ResearchView(ResearchItem researchItem) {
    this(
      researchItem.getName(),
      researchItem.getDescription(),
      researchItem.getStart().asText(),
      researchItem.getEnd() != null ? researchItem.getEnd().asText() : null,
      researchItem.getOrg(),
      researchItem.getUrl()
    );
  }
}
