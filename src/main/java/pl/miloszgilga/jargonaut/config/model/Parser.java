package pl.miloszgilga.jargonaut.config.model;

import pl.miloszgilga.jargonaut.core.parser.validation.YamlNested;
import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;

public class Parser {
  @YamlRequired
  @YamlNested
  private ParserLib syntax;

  @YamlRequired
  @YamlNested
  private ParserLib math;

  public Parser() {
  }

  public ParserLib getSyntax() {
    return syntax;
  }

  public void setSyntax(ParserLib syntax) {
    this.syntax = syntax;
  }

  public ParserLib getMath() {
    return math;
  }

  public void setMath(ParserLib math) {
    this.math = math;
  }
}
