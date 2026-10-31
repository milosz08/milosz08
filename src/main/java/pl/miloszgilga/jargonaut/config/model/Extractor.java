package pl.miloszgilga.jargonaut.config.model;

import pl.miloszgilga.jargonaut.core.extractor.GithubExtractorConfig;
import pl.miloszgilga.jargonaut.core.parser.validation.YamlNested;
import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;

public class Extractor {
  @YamlRequired
  @YamlNested
  private GithubExtractorConfig github;

  public Extractor() {
  }

  public GithubExtractorConfig getGithub() {
    return github;
  }

  public void setGithub(GithubExtractorConfig github) {
    this.github = github;
  }
}
