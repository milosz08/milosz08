package pl.miloszgilga.jargonaut.page.project.model;

import pl.miloszgilga.jargonaut.core.DataUtils;
import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;
import pl.miloszgilga.jargonaut.core.processor.MergeableMetadata;

public class CategoryMetadata implements MergeableMetadata<CategoryMetadata> {
  @YamlRequired
  private String name;

  @YamlRequired
  private Integer weight;

  public CategoryMetadata() {
  }

  @Override
  public CategoryMetadata merge(CategoryMetadata override) {
    if (override == null) {
      return this;
    }
    final CategoryMetadata merged = new CategoryMetadata();
    merged.name = DataUtils.mergeValues(name, override.name);
    merged.weight = DataUtils.mergeValues(weight, override.weight);
    return merged;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getWeight() {
    return weight;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
  }
}
