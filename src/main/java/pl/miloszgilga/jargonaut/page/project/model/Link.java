package pl.miloszgilga.jargonaut.page.project.model;

import pl.miloszgilga.jargonaut.core.DataUtils;
import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;
import pl.miloszgilga.jargonaut.core.processor.MergeableMetadata;

public class Link implements MergeableMetadata<Link> {
  @YamlRequired
  private String url;

  private String name;

  public Link() {
  }

  @Override
  public Link merge(Link override) {
    if (override == null) {
      return this;
    }
    final Link merged = new Link();
    merged.url = DataUtils.mergeValues(url, override.url);
    merged.name = DataUtils.mergeValues(name, override.name);
    return merged;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
