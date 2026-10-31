package pl.miloszgilga.jargonaut.core.parser;

import pl.miloszgilga.jargonaut.core.CastUtils;
import pl.miloszgilga.jargonaut.core.DataUtils;
import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;
import pl.miloszgilga.jargonaut.core.processor.MergeableMetadata;

public abstract class GenericMdDocument<T extends GenericMdDocument<T>> implements GenericPage, MergeableMetadata<T> {
  @YamlRequired
  protected String pageName;

  @YamlRequired
  protected String pageTitle;

  @YamlRequired
  protected String pageDescription;

  protected Boolean directOutput;

  protected Boolean listable;

  protected Boolean expanded;

  public GenericMdDocument() {
  }

  @Override
  public final T merge(T override) {
    if (override == null) {
      return CastUtils.unsafeCast(this);
    }
    final T merged = createInstance();
    merged.pageName = DataUtils.mergeValues(pageName, override.pageName);
    merged.pageTitle = DataUtils.mergeValues(pageTitle, override.pageTitle);
    merged.pageDescription = DataUtils.mergeValues(pageDescription, override.pageDescription);
    merged.directOutput = DataUtils.mergeValues(directOutput, override.directOutput);
    merged.listable = DataUtils.mergeValues(listable, override.listable);
    merged.expanded = DataUtils.mergeValues(expanded, override.expanded);
    mergeAdditionalFields(merged, override);
    return merged;
  }

  @Override
  public String getPageName() {
    return pageName;
  }

  public void setPageName(String pageName) {
    this.pageName = pageName;
  }

  @Override
  public String getPageTitle() {
    return pageTitle;
  }

  public void setPageTitle(String pageTitle) {
    this.pageTitle = pageTitle;
  }

  @Override
  public String getPageDescription() {
    return pageDescription;
  }

  public void setPageDescription(String pageDescription) {
    this.pageDescription = pageDescription;
  }

  @Override
  public Boolean getDirectOutput() {
    return directOutput;
  }

  public void setDirectOutput(Boolean directOutput) {
    this.directOutput = directOutput;
  }

  @Override
  public Boolean getListable() {
    return listable;
  }

  public void setListable(Boolean listable) {
    this.listable = listable;
  }

  @Override
  public Boolean getExpanded() {
    return expanded;
  }

  public void setExpanded(Boolean expanded) {
    this.expanded = expanded;
  }

  protected void mergeAdditionalFields(T merged, T override) {
  }

  protected abstract T createInstance();
}
