package pl.miloszgilga.jargonaut.core.extractor;

import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;

public class GithubExtractorConfig {
  @YamlRequired
  private String token;

  public GithubExtractorConfig() {
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
