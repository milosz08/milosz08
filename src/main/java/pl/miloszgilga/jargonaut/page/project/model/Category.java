package pl.miloszgilga.jargonaut.page.project.model;

import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Category {
  private final String id;
  private final Map<Locale, String> localizedNames = new HashMap<>();
  private Integer weight = 0;
  private final List<Category> subCategories = new ArrayList<>();
  private final List<PageRepresentation<ProjectNodeMetadata>> projects = new ArrayList<>();

  public Category(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public Map<Locale, String> getLocalizedNames() {
    return localizedNames;
  }

  public Integer getWeight() {
    return weight;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
  }

  public List<Category> getSubCategories() {
    return subCategories;
  }

  public List<PageRepresentation<ProjectNodeMetadata>> getProjects() {
    return projects;
  }
}
