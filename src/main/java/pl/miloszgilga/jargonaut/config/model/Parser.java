package pl.miloszgilga.jargonaut.config.model;

import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;

public class Parser {
  @YamlRequired
  private String syntax;

  @YamlRequired
  private String math;

  public Parser() {
  }

  public String getSyntax() {
    return syntax;
  }

  public void setSyntax(String syntax) {
    this.syntax = syntax;
  }

  public String getMath() {
    return math;
  }

  public void setMath(String math) {
    this.math = math;
  }
}
