package pl.miloszgilga.jargonaut.core.template.generator;

import pl.miloszgilga.jargonaut.asset.resolver.AssetAliasResolver;
import pl.miloszgilga.jargonaut.core.minifier.html.HtmlMinifier;
import pl.miloszgilga.jargonaut.core.minifier.js.InlineJsMinifier;
import pl.miloszgilga.jargonaut.core.parser.json.JsonParser;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.template.TemplateEngine;
import pl.miloszgilga.jargonaut.rss.RssProcessor;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class GeneratorContext {
  private final ZonedDateTime buildDateTime;
  private final JsonParser jsonParser;
  private final TemplateEngine templateEngine;
  private final HtmlMinifier htmlMinifier;
  private final InlineJsMinifier inlineJsMinifier;
  private final AssetAliasResolver assetAliasResolver;
  private final SiteBuildContext siteBuildContext;
  private final List<HtmlPostProcessor> postProcessors;
  private final RssProcessor rssProcessor;

  private GeneratorContext(Builder builder) {
    buildDateTime = builder.buildDateTime;
    jsonParser = builder.jsonParser;
    templateEngine = builder.templateEngine;
    htmlMinifier = builder.htmlMinifier;
    inlineJsMinifier = builder.inlineJsMinifier;
    assetAliasResolver = builder.assetAliasResolver;
    siteBuildContext = builder.siteBuildContext;
    postProcessors = builder.postProcessors;
    rssProcessor = builder.rssProcessor;
  }

  public ZonedDateTime getBuildDateTime() {
    return buildDateTime;
  }

  public JsonParser getJsonParser() {
    return jsonParser;
  }

  public TemplateEngine getTemplateEngine() {
    return templateEngine;
  }

  public HtmlMinifier getHtmlMinifier() {
    return htmlMinifier;
  }

  public InlineJsMinifier getInlineJsMinifier() {
    return inlineJsMinifier;
  }

  public AssetAliasResolver getAssetAliasResolver() {
    return assetAliasResolver;
  }

  public SiteBuildContext getSiteBuildContext() {
    return siteBuildContext;
  }

  public List<HtmlPostProcessor> getPostProcessors() {
    return postProcessors;
  }

  public RssProcessor getRssProcessor() {
    return rssProcessor;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private ZonedDateTime buildDateTime;
    private JsonParser jsonParser;
    private TemplateEngine templateEngine;
    private HtmlMinifier htmlMinifier;
    private InlineJsMinifier inlineJsMinifier;
    private AssetAliasResolver assetAliasResolver;
    private SiteBuildContext siteBuildContext;
    private final List<HtmlPostProcessor> postProcessors = new ArrayList<>();
    private RssProcessor rssProcessor;

    private Builder() {
    }

    public Builder buildDateTime(ZonedDateTime buildDateTime) {
      this.buildDateTime = buildDateTime;
      return this;
    }

    public Builder jsonParser(JsonParser jsonParser) {
      this.jsonParser = jsonParser;
      return this;
    }

    public Builder templateEngine(TemplateEngine templateEngine) {
      this.templateEngine = templateEngine;
      return this;
    }

    public Builder htmlMinifier(HtmlMinifier htmlMinifier) {
      this.htmlMinifier = htmlMinifier;
      return this;
    }

    public Builder inlineJsMinifier(InlineJsMinifier inlineJsMinifier) {
      this.inlineJsMinifier = inlineJsMinifier;
      return this;
    }

    public Builder assetAliasResolver(AssetAliasResolver assetAliasResolver) {
      this.assetAliasResolver = assetAliasResolver;
      return this;
    }

    public Builder siteBuildContext(SiteBuildContext siteBuildContext) {
      this.siteBuildContext = siteBuildContext;
      return this;
    }

    public Builder addPostProcessors(List<HtmlPostProcessor> postProcessors) {
      this.postProcessors.addAll(postProcessors);
      return this;
    }

    public Builder rssProcessor(RssProcessor rssProcessor) {
      this.rssProcessor = rssProcessor;
      return this;
    }

    public GeneratorContext build() {
      return new GeneratorContext(this);
    }
  }
}
