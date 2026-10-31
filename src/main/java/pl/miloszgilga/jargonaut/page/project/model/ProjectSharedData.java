package pl.miloszgilga.jargonaut.page.project.model;

import pl.miloszgilga.jargonaut.core.extractor.stats.RepoStats;
import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProjectSharedData {
  private Category rootProjectCategory;

  private final List<PageRepresentation<ProjectNodeMetadata>> allProjects = new ArrayList<>();
  private final Map<String, Map<Locale, RepoStats>> projectRepoStats = new HashMap<>();

  public Category getRootProjectCategory() {
    return rootProjectCategory;
  }

  public void setRootProjectCategory(Category rootProjectCategory) {
    this.rootProjectCategory = rootProjectCategory;
  }

  public List<PageRepresentation<ProjectNodeMetadata>> getAllProjects() {
    return allProjects;
  }

  public Map<String, Map<Locale, RepoStats>> getProjectRepoStats() {
    return projectRepoStats;
  }
}
