package pl.miloszgilga.jargonaut.config.model;

import pl.miloszgilga.jargonaut.core.parser.validation.YamlNested;
import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Config {
  @YamlRequired
  private Locale defaultLocale;

  @YamlRequired
  private Boolean minify;

  @YamlRequired
  @YamlNested
  private Seo seo;

  @YamlRequired
  @YamlNested
  private Analytics analytics;

  @YamlRequired
  @YamlNested
  private Generator generator;

  @YamlRequired
  @YamlNested
  private Dir dir;

  @YamlRequired
  @YamlNested
  private Extractor extractor;

  @YamlRequired
  @YamlNested
  private Parser parser;

  @YamlRequired
  @YamlNested
  private Cache cache;

  @YamlRequired
  @YamlNested
  private Project project;

  @YamlRequired
  private String rssOutput;

  @YamlNested
  private List<AdditionalAsset> additionalAssets = new ArrayList<>();

  public Config() {
  }

  public Locale getDefaultLocale() {
    return defaultLocale;
  }

  public void setDefaultLocale(Locale defaultLocale) {
    this.defaultLocale = defaultLocale;
  }

  public Boolean getMinify() {
    return minify;
  }

  public void setMinify(Boolean minify) {
    this.minify = minify;
  }

  public Seo getSeo() {
    return seo;
  }

  public void setSeo(Seo seo) {
    this.seo = seo;
  }

  public Analytics getAnalytics() {
    return analytics;
  }

  public void setAnalytics(Analytics analytics) {
    this.analytics = analytics;
  }

  public Generator getGenerator() {
    return generator;
  }

  public void setGenerator(Generator generator) {
    this.generator = generator;
  }

  public Dir getDir() {
    return dir;
  }

  public void setDir(Dir dir) {
    this.dir = dir;
  }

  public Extractor getExtractor() {
    return extractor;
  }

  public void setExtractor(Extractor extractor) {
    this.extractor = extractor;
  }

  public Parser getParser() {
    return parser;
  }

  public void setParser(Parser parser) {
    this.parser = parser;
  }

  public Cache getCache() {
    return cache;
  }

  public void setCache(Cache cache) {
    this.cache = cache;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public String getRssOutput() {
    return rssOutput;
  }

  public void setRssOutput(String rssOutput) {
    this.rssOutput = rssOutput;
  }

  public List<AdditionalAsset> getAdditionalAssets() {
    return additionalAssets;
  }

  public void setAdditionalAssets(List<AdditionalAsset> additionalAssets) {
    this.additionalAssets = additionalAssets;
  }
}
