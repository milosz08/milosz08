package pl.miloszgilga.jargonaut.core.bootstrap;

import pl.miloszgilga.jargonaut.asset.AssetTransformer;
import pl.miloszgilga.jargonaut.asset.HashingAssetTransformer;
import pl.miloszgilga.jargonaut.asset.converter.AssetFileConverter;
import pl.miloszgilga.jargonaut.asset.converter.SvgAssetFileConverter;
import pl.miloszgilga.jargonaut.asset.converter.WebpAssetFileConverter;
import pl.miloszgilga.jargonaut.asset.processor.FaviconAssetProcessor;
import pl.miloszgilga.jargonaut.asset.processor.GlobalAssetsProcessor;
import pl.miloszgilga.jargonaut.asset.processor.OgImageAssetProcessor;
import pl.miloszgilga.jargonaut.asset.resolver.AssetResolutionStrategy;
import pl.miloszgilga.jargonaut.asset.resolver.GlobalAssetResolutionStrategy;
import pl.miloszgilga.jargonaut.asset.resolver.LocalAssetResolutionStrategy;
import pl.miloszgilga.jargonaut.core.extractor.contributions.UserContributionsExtractor;
import pl.miloszgilga.jargonaut.core.extractor.contributions.github.GithubUserContributionsExtractor;
import pl.miloszgilga.jargonaut.core.extractor.stats.RepoStatsExtractor;
import pl.miloszgilga.jargonaut.core.extractor.stats.github.GithubRepoStatsExtractor;
import pl.miloszgilga.jargonaut.core.minifier.html.GoogleHtmlMinifierAdapter;
import pl.miloszgilga.jargonaut.core.minifier.html.HtmlMinifier;
import pl.miloszgilga.jargonaut.core.minifier.js.GoogleClosureJsMinifierAdapter;
import pl.miloszgilga.jargonaut.core.minifier.js.InlineJsInHtmlMinifier;
import pl.miloszgilga.jargonaut.core.minifier.js.InlineJsMinifier;
import pl.miloszgilga.jargonaut.core.minifier.js.JsMinifier;
import pl.miloszgilga.jargonaut.core.minifier.styles.LogicSquadStylesMinifierAdapter;
import pl.miloszgilga.jargonaut.core.minifier.styles.StylesMinifier;
import pl.miloszgilga.jargonaut.core.minifier.svg.BareBoneSvgMinifier;
import pl.miloszgilga.jargonaut.core.minifier.xml.GoogleXmlMinifier;
import pl.miloszgilga.jargonaut.core.minifier.xml.XmlMinifier;
import pl.miloszgilga.jargonaut.core.parser.DocumentParser;
import pl.miloszgilga.jargonaut.core.parser.SlugifyUrlNormalizer;
import pl.miloszgilga.jargonaut.core.parser.UrlNormalizer;
import pl.miloszgilga.jargonaut.core.parser.json.JacksonJsonParserAdapter;
import pl.miloszgilga.jargonaut.core.parser.json.JsonParser;
import pl.miloszgilga.jargonaut.core.parser.md.FlexmarkFrontmatterMdParser;
import pl.miloszgilga.jargonaut.core.parser.yaml.JacksonYamlParser;
import pl.miloszgilga.jargonaut.core.parser.yaml.YamlParser;
import pl.miloszgilga.jargonaut.core.polyglot.JsMathRenderer;
import pl.miloszgilga.jargonaut.core.polyglot.JsSyntaxHighlighter;
import pl.miloszgilga.jargonaut.core.polyglot.MathRenderer;
import pl.miloszgilga.jargonaut.core.polyglot.SyntaxHighlighter;
import pl.miloszgilga.jargonaut.core.processor.ProcessorContext;
import pl.miloszgilga.jargonaut.core.processor.TaskProcessor;
import pl.miloszgilga.jargonaut.core.template.FreeMarkerTemplateEngine;
import pl.miloszgilga.jargonaut.core.template.TemplateEngine;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlPostProcessor;
import pl.miloszgilga.jargonaut.dictionary.DictionaryProcessor;
import pl.miloszgilga.jargonaut.globals.GlobalsProcessor;
import pl.miloszgilga.jargonaut.postprocessor.EmailManglingHtmlPostProcessor;
import pl.miloszgilga.jargonaut.postprocessor.ExternalLinkHtmlPostProcessor;
import pl.miloszgilga.jargonaut.postprocessor.toc.TocHtmlPostProcessor;
import pl.miloszgilga.jargonaut.rss.RssProcessor;

import java.util.List;

public interface JArgonautEntrypoint {
  default void attachAssetResolutionStrategies(JArgonautBootstrapper b,
                                               List<AssetResolutionStrategy> assetResolutionStrategies) {
    assetResolutionStrategies.add(new GlobalAssetResolutionStrategy());
    assetResolutionStrategies.add(new LocalAssetResolutionStrategy());
  }

  default YamlParser yamlParser(JArgonautBootstrapper b) {
    return new JacksonYamlParser(b.getEventBus());
  }

  default void attachAssetFileConverters(JArgonautBootstrapper b, List<AssetFileConverter> assetFileConverters) {
    assetFileConverters.add(new WebpAssetFileConverter());
    assetFileConverters.add(new SvgAssetFileConverter(new BareBoneSvgMinifier(b.getEventBus())));
  }

  default AssetTransformer assetTransformer(JArgonautBootstrapper b, List<AssetFileConverter> assetFileConverters) {
    return new HashingAssetTransformer(b.getEventBus(), assetFileConverters);
  }

  default AssetTransformer noopAssetTransformer(JArgonautBootstrapper b) {
    return new HashingAssetTransformer(b.getEventBus());
  }

  default StylesMinifier stylesMinifier(JArgonautBootstrapper b) {
    return new LogicSquadStylesMinifierAdapter(b.getEventBus());
  }

  default JsMinifier jsMinifier(JArgonautBootstrapper b) {
    return new GoogleClosureJsMinifierAdapter(b.getEventBus());
  }

  default HtmlMinifier htmlMinifier(JArgonautBootstrapper b) {
    return new GoogleHtmlMinifierAdapter(b.getEventBus());
  }

  default XmlMinifier xmlMinifier(JArgonautBootstrapper b) {
    return new GoogleXmlMinifier(b.getEventBus());
  }

  default InlineJsMinifier inlineJsMinifier(JArgonautBootstrapper b) {
    return new InlineJsInHtmlMinifier(b.getEventBus(), b.getJsMinifier());
  }

  default JsonParser jsonParser(JArgonautBootstrapper b) {
    return new JacksonJsonParserAdapter(b.getEventBus());
  }

  default TemplateEngine templateEngine(JArgonautBootstrapper b) {
    return new FreeMarkerTemplateEngine(b.getEventBus());
  }

  default UrlNormalizer urlNormalizer(JArgonautBootstrapper b) {
    return new SlugifyUrlNormalizer(b.getEventBus());
  }

  default void attachHtmlPostProcessors(JArgonautBootstrapper b, List<HtmlPostProcessor> htmlPostProcessors) {
    htmlPostProcessors.add(new TocHtmlPostProcessor(b.getUrlNormalizer(), b.getTemplateEngine(), b.getEventBus()));
    htmlPostProcessors.add(new EmailManglingHtmlPostProcessor(b.getEventBus()));
    htmlPostProcessors.add(new ExternalLinkHtmlPostProcessor(b.getEventBus()));
  }

  default void attachRepoStatsExtractor(JArgonautBootstrapper b, List<RepoStatsExtractor> repoStatsExtractors) {
    repoStatsExtractors.add(new GithubRepoStatsExtractor(
      b.getEventBus(),
      b.getSiteBuildContext().getConfig().getExtractor().getGithub(),
      b.getJsonParser()
    ));
  }

  default void attachUserContributionsExtractor(JArgonautBootstrapper b,
                                                List<UserContributionsExtractor> userContributionsExtractors) {
    userContributionsExtractors.add(new GithubUserContributionsExtractor(
      b.getEventBus(),
      b.getSiteBuildContext().getConfig().getExtractor().getGithub(),
      b.getJsonParser()
    ));
  }

  default SyntaxHighlighter syntaxHighlighter(JArgonautBootstrapper b) {
    return new JsSyntaxHighlighter(b.getSiteBuildContext(), b.getEventBus());
  }

  default MathRenderer mathRenderer(JArgonautBootstrapper b) {
    return new JsMathRenderer(b.getSiteBuildContext(), b.getEventBus());
  }

  default DocumentParser documentParser(JArgonautBootstrapper b) {
    return new FlexmarkFrontmatterMdParser(
      b.getSyntaxHighlighter(),
      b.getMathRenderer(),
      b.getYamlParser(),
      b.getEventBus()
    );
  }

  default void attachPreParsers(JArgonautBootstrapper b, List<TaskProcessor> preParsers) {
    preParsers.add(new DictionaryProcessor(b.getBaseDir(), b.getSiteBuildContext(), b.getYamlParser()));
    preParsers.add(new GlobalsProcessor(b.getBaseDir(), b.getSiteBuildContext(), b.getYamlParser()));
    preParsers.add(new GlobalAssetsProcessor(b.getBaseDir(), b.getAssetTransformer()));
    preParsers.add(new FaviconAssetProcessor(b.getBaseDir()));
    preParsers.add(new OgImageAssetProcessor(b.getBaseDir(), b.getNoopAssetTransformer()));
  }

  void attachPageParsers(JArgonautBootstrapper b, ProcessorContext pc, List<TaskProcessor> pageParsers);

  RssProcessor rssProcessor(JArgonautBootstrapper b);
}
