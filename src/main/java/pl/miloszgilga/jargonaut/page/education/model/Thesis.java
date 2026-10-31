package pl.miloszgilga.jargonaut.page.education.model;

import pl.miloszgilga.jargonaut.core.DataUtils;
import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;
import pl.miloszgilga.jargonaut.core.processor.MergeableMetadata;

public class Thesis implements MergeableMetadata<Thesis> {
  @YamlRequired
  private String description;

  private String url;

  public Thesis() {
  }

  @Override
  public Thesis merge(Thesis override) {
    if (override == null) {
      return this;
    }
    final Thesis merged = new Thesis();
    merged.description = DataUtils.mergeValues(description, override.description);
    merged.url = DataUtils.mergeValues(url, override.url);
    return merged;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
