package pl.miloszgilga.jargonaut.core.parser;

public interface GenericPage {
  String getPageName();

  String getPageTitle();

  String getPageDescription();

  Boolean getDirectOutput();

  Boolean getListable();

  Boolean getExpanded();
}
