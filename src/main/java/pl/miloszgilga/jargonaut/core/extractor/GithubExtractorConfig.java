package pl.miloszgilga.jargonaut.core.extractor;

import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;

import java.time.ZoneId;

public class GithubExtractorConfig {
  @YamlRequired
  private String token;

  @YamlRequired
  private ZoneId timezone;

  public GithubExtractorConfig() {
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public ZoneId getTimezone() {
    return timezone;
  }

  public void setTimezone(ZoneId timezone) {
    this.timezone = timezone;
  }
}
