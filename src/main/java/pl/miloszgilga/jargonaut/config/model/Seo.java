package pl.miloszgilga.jargonaut.config.model;

import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;

import java.util.ArrayList;
import java.util.List;

public class Seo {
  @YamlRequired
  private String domain;

  private String favicon;

  private String ogImage;

  private List<String> sameAs = new ArrayList<>();

  public Seo() {
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getFavicon() {
    return favicon;
  }

  public void setFavicon(String favicon) {
    this.favicon = favicon;
  }

  public String getOgImage() {
    return ogImage;
  }

  public void setOgImage(String ogImage) {
    this.ogImage = ogImage;
  }

  public List<String> getSameAs() {
    return sameAs;
  }

  public void setSameAs(List<String> sameAs) {
    this.sameAs = sameAs;
  }
}
