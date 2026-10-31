package pl.miloszgilga.jargonaut.page.project.dto;

import pl.miloszgilga.jargonaut.page.project.model.Link;
import pl.miloszgilga.jargonaut.page.project.model.ProjectNodeMetadata;
import pl.miloszgilga.jargonaut.page.tag.model.TagItem;

import java.util.List;

public record ProjectTableView(
  String id,
  String name,
  String kind,
  String description,
  List<TechView> tech,
  String status,
  List<String> modifiers,
  List<String> repoUrls,
  boolean recentlyUpdated
) {
  public ProjectTableView(String id, ProjectNodeMetadata metadata, List<TagItem> tags, boolean recentlyUpdated) {
    this(
      id,
      metadata.getTitle(),
      metadata.getKind(),
      metadata.getSummary(),
      metadata.getTech().stream()
        .flatMap(tag -> tags.stream()
          .filter(tagItem -> tagItem.name().equals(tag))
          .findFirst()
          .map(tagItem -> new TechView(tag, tagItem.slug()))
          .stream()
        )
        .toList(),
      metadata.getStatus(),
      metadata.getModifiers() != null ? metadata.getModifiers() : List.of(),
      metadata.getRepoUrls() != null ? metadata.getRepoUrls().stream().map(Link::getUrl).toList() : List.of(),
      recentlyUpdated
    );
  }
}
