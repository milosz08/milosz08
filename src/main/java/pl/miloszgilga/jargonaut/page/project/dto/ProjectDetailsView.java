package pl.miloszgilga.jargonaut.page.project.dto;

import pl.miloszgilga.jargonaut.page.project.model.Link;
import pl.miloszgilga.jargonaut.page.project.model.ProjectNodeMetadata;

import java.util.List;

public record ProjectDetailsView(
  String id,
  String name,
  String description,
  String status,
  List<Link> repoUrls,
  List<Link> docsUrls,
  List<Link> demoUrls,
  List<Link> downloadUrls,
  boolean recentlyUpdated
) {
  public ProjectDetailsView(String projectId, ProjectNodeMetadata metadata, boolean recentlyUpdated) {
    this(
      projectId,
      metadata.getTitle(),
      metadata.getSummary(),
      metadata.getStatus(),
      metadata.getRepoUrls(),
      metadata.getDocsUrls(),
      metadata.getDemoUrls(),
      metadata.getDownloadUrls(),
      recentlyUpdated
    );
  }
}
