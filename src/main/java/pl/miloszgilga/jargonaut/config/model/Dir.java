package pl.miloszgilga.jargonaut.config.model;

import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;

public class Dir {
  @YamlRequired
  private String output;

  @YamlRequired
  private String dictionary;

  @YamlRequired
  private String globals;

  @YamlRequired
  private String sites;

  @YamlRequired
  private String styles;

  @YamlRequired
  private String scripts;

  public Dir() {
  }

  public String getOutput() {
    return output;
  }

  public void setOutput(String output) {
    this.output = output;
  }

  public String getDictionary() {
    return dictionary;
  }

  public void setDictionary(String dictionary) {
    this.dictionary = dictionary;
  }

  public String getGlobals() {
    return globals;
  }

  public void setGlobals(String globals) {
    this.globals = globals;
  }

  public String getSites() {
    return sites;
  }

  public void setSites(String sites) {
    this.sites = sites;
  }

  public String getStyles() {
    return styles;
  }

  public void setStyles(String styles) {
    this.styles = styles;
  }

  public String getScripts() {
    return scripts;
  }

  public void setScripts(String scripts) {
    this.scripts = scripts;
  }
}
