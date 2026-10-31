package pl.miloszgilga.jargonaut.page.education.model;

import pl.miloszgilga.jargonaut.core.DataUtils;
import pl.miloszgilga.jargonaut.core.parser.validation.YamlNested;
import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;
import pl.miloszgilga.jargonaut.core.processor.MergeableMetadata;
import pl.miloszgilga.jargonaut.page.education.TimelineItem;

public class ResearchItem implements TimelineItem, MergeableMetadata<ResearchItem> {
  @YamlRequired
  private String name;

  @YamlRequired
  private String description;

  @YamlRequired
  @YamlNested
  private DateNode start;

  @YamlNested
  private DateNode end;

  private String org;

  private String url;

  public ResearchItem() {
  }

  @Override
  public ResearchItem merge(ResearchItem override) {
    if (override == null) {
      return this;
    }
    final ResearchItem merged = new ResearchItem();
    merged.name = DataUtils.mergeValues(name, override.name);
    merged.description = DataUtils.mergeValues(description, override.description);
    merged.start = DataUtils.mergeObject(start, override.start);
    merged.end = DataUtils.mergeObject(end, override.end);
    merged.org = DataUtils.mergeValues(org, override.org);
    merged.url = DataUtils.mergeValues(url, override.url);
    return merged;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public DateNode getStart() {
    return start;
  }

  public void setStart(DateNode start) {
    this.start = start;
  }

  @Override
  public DateNode getEnd() {
    return end;
  }

  public void setEnd(DateNode end) {
    this.end = end;
  }

  public String getOrg() {
    return org;
  }

  public void setOrg(String org) {
    this.org = org;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
