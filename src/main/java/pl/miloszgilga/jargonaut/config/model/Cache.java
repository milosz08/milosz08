package pl.miloszgilga.jargonaut.config.model;

import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;

public class Cache {
  @YamlRequired
  private Boolean enabled;

  @YamlRequired
  private String cacheDir;

  public Cache() {
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public String getCacheDir() {
    return cacheDir;
  }

  public void setCacheDir(String cacheDir) {
    this.cacheDir = cacheDir;
  }
}
