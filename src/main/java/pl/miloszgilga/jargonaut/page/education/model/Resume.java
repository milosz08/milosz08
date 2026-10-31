package pl.miloszgilga.jargonaut.page.education.model;

import pl.miloszgilga.jargonaut.core.CastUtils;
import pl.miloszgilga.jargonaut.core.DataUtils;
import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;
import pl.miloszgilga.jargonaut.core.processor.MergeableMetadata;

public class Resume implements MergeableMetadata<Resume> {
  @YamlRequired
  private String label;

  @YamlRequired
  private String url;

  public Resume() {
  }

  @Override
  public Resume merge(Resume override) {
    if (override == null) {
      return CastUtils.unsafeCast(this);
    }
    final Resume merged = new Resume();
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
