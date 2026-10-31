package pl.miloszgilga.jargonaut.globals.model;

import pl.miloszgilga.jargonaut.core.DataUtils;
import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;
import pl.miloszgilga.jargonaut.core.processor.MergeableMetadata;

public class NavLink implements MergeableMetadata<NavLink> {
  @YamlRequired
  private String label;

  @YamlRequired
  private String url;

  public NavLink() {
  }

  @Override
  public NavLink merge(NavLink override) {
    if (override == null) {
      return this;
    }
    final NavLink merged = new NavLink();
    merged.label = DataUtils.mergeValues(label, override.label);
    merged.url = DataUtils.mergeValues(url, override.url);
    return merged;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
