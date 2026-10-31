package pl.miloszgilga.jargonaut.config.model;

import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;

public class Generator {
  @YamlRequired
  private String name;

  @YamlRequired
  private String url;

  public Generator() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
