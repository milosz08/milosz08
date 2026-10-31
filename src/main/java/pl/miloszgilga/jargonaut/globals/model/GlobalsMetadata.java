package pl.miloszgilga.jargonaut.globals.model;

import pl.miloszgilga.jargonaut.core.DataUtils;
import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;
import pl.miloszgilga.jargonaut.core.processor.MergeableMetadata;

import java.util.List;

public class GlobalsMetadata implements MergeableMetadata<GlobalsMetadata> {
  @YamlRequired
  private String name;

  private List<String> honorificPrefixes;

  private List<String> honorificSuffixes;

  @YamlRequired
  private String jobTitle;

  @YamlRequired
  private String dtfPattern;

  @YamlRequired
  private String rssDescription;

  @YamlRequired
  private List<NavLink> navLinks;

  @Override
  public GlobalsMetadata merge(GlobalsMetadata override) {
    if (override == null) {
      return this;
    }
    final GlobalsMetadata merged = new GlobalsMetadata();
    merged.name = DataUtils.mergeValues(name, override.name);
    merged.honorificPrefixes = DataUtils.mergeValues(honorificPrefixes, override.honorificPrefixes);
    merged.honorificSuffixes = DataUtils.mergeValues(honorificSuffixes, override.honorificSuffixes);
    merged.jobTitle = DataUtils.mergeValues(jobTitle, override.jobTitle);
    merged.dtfPattern = DataUtils.mergeValues(dtfPattern, override.dtfPattern);
    merged.rssDescription = DataUtils.mergeValues(rssDescription, override.rssDescription);
    merged.navLinks = DataUtils.mergeList(navLinks, override.navLinks);
    return merged;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getHonorificPrefixes() {
    return honorificPrefixes;
  }

  public void setHonorificPrefixes(List<String> honorificPrefixes) {
    this.honorificPrefixes = honorificPrefixes;
  }

  public List<String> getHonorificSuffixes() {
    return honorificSuffixes;
  }

  public void setHonorificSuffixes(List<String> honorificSuffixes) {
    this.honorificSuffixes = honorificSuffixes;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  public String getDtfPattern() {
    return dtfPattern;
  }

  public void setDtfPattern(String dtfPattern) {
    this.dtfPattern = dtfPattern;
  }

  public String getRssDescription() {
    return rssDescription;
  }

  public void setRssDescription(String rssDescription) {
    this.rssDescription = rssDescription;
  }

  public List<NavLink> getNavLinks() {
    return navLinks;
  }

  public void setNavLinks(List<NavLink> navLinks) {
    this.navLinks = navLinks;
  }
}
