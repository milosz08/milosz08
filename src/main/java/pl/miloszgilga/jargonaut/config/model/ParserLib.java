package pl.miloszgilga.jargonaut.config.model;

import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;

import java.util.ArrayList;
import java.util.List;

public class ParserLib {
  @YamlRequired
  private String libUrl;

  private List<String> additionalAssets = new ArrayList<>();

  public ParserLib() {
  }

  public String getLibUrl() {
    return libUrl;
  }

  public void setLibUrl(String libUrl) {
    this.libUrl = libUrl;
  }

  public List<String> getAdditionalAssets() {
    return additionalAssets;
  }

  public void setAdditionalAssets(List<String> additionalAssets) {
    this.additionalAssets = additionalAssets;
  }
}
