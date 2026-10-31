package pl.miloszgilga.jargonaut.page.tag.dto;

import pl.miloszgilga.jargonaut.page.tag.model.TagItem;

public record TagView(String name, String slug, int projectsCount) {
  public TagView(TagItem tagItem, int projectsCount) {
    this(tagItem.name(), tagItem.slug(), projectsCount);
  }
}
