package pl.miloszgilga.jargonaut.core.processor.md;

import pl.miloszgilga.jargonaut.core.CastUtils;
import pl.miloszgilga.jargonaut.core.parser.FrontmatterInterpolator;
import pl.miloszgilga.jargonaut.core.parser.GenericPage;
import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.parser.ParsedDocument;
import pl.miloszgilga.jargonaut.core.processor.MergeableMetadata;
import pl.miloszgilga.jargonaut.core.processor.ProcessorContext;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlGeneratorFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class PageMergingMdProcessor<T extends GenericPage & MergeableMetadata<T>> extends MdProcessor {
  protected final Path baseSiteDir;
  protected final FrontmatterInterpolator frontmatterInterpolator;

  protected SiteBuildContext currentContext;

  protected PageMergingMdProcessor(String directoryName, ProcessorContext processorContext) {
    super(processorContext.getBaseSitesDir().resolve(directoryName), processorContext);
    this.baseSiteDir = processorContext.getBaseSitesDir();
    frontmatterInterpolator = new FrontmatterInterpolator(yamlParser);
  }

  @Override
  protected void process(SiteBuildContext context, Map<String, List<Path>> processedAssets, Locale defaultLocale) {
    currentContext = context;
    final Map<Locale, ParsedDocument<T>> translations = loadTranslationsFromDirectory(resourcePath, defaultLocale);
    mergeTranslations(translations, defaultLocale);
    persistPageRepresentation(context, new PageRepresentation<>(
      resolvePageId(),
      resourcePath,
      translations,
      processedAssets,
      getHtmlRendererFactory()
    ));
    context.getActiveLocales().addAll(translations.keySet());
  }

  @Override
  protected String getTargetWebDir() {
    return resolvePageId();
  }

  protected final Map<Locale, ParsedDocument<T>> loadTranslationsFromDirectory(Path directory, Locale defaultLocale) {
    final Map<Locale, ParsedDocument<T>> translations = new HashMap<>();
    final List<Path> indexFiles = getIndexFiles(directory);
    for (final Path file : indexFiles) {
      final ParsedDocument<T> parsedDoc = documentParser.parseMarkdown(file, getTargetClass(), defaultLocale);
      final Object globals = currentContext.getGlobals() != null
        ? currentContext.getGlobals().get(parsedDoc.locale())
        : null;
      final Object config = currentContext.getConfig();
      final Map<String, Object> dynamicContext = getDynamicInterpolationContext(parsedDoc.locale());
      final String interpolatedHtmlContent = frontmatterInterpolator.interpolateMarkdown(
        parsedDoc.htmlContent(),
        globals,
        config,
        dynamicContext
      );
      final ParsedDocument<T> interpolatedDoc = new ParsedDocument<>(
        parsedDoc.locale(),
        parsedDoc.metadata(),
        interpolatedHtmlContent
      );
      onFileParsedHook(interpolatedDoc);
      translations.put(interpolatedDoc.locale(), interpolatedDoc);
    }
    return translations;
  }

  protected final void mergeTranslations(Map<Locale, ParsedDocument<T>> translations, Locale locale) {
    final ParsedDocument<T> defaultDoc = translations.get(locale);
    if (!(defaultDoc != null && defaultDoc.metadata() != null)) {
      return;
    }
    final T rawDefaultMetadata = defaultDoc.metadata();
    final Object config = currentContext.getConfig();

    final Object defaultGlobals = currentContext.getGlobals() != null ? currentContext.getGlobals().get(locale) : null;
    final Map<String, Object> defaultDynamicContext = getDynamicInterpolationContext(locale);
    final T finalDefaultMetadata = frontmatterInterpolator
      .interpolateMergedObject(rawDefaultMetadata, getTargetClass(), defaultGlobals, config, defaultDynamicContext);

    yamlParser.validate(finalDefaultMetadata);
    translations.put(locale, new ParsedDocument<>(locale, finalDefaultMetadata, defaultDoc.htmlContent()));
    onPostMergeHook(finalDefaultMetadata);

    for (final Map.Entry<Locale, ParsedDocument<T>> entry : translations.entrySet()) {
      final Locale altLocale = entry.getKey();
      if (!altLocale.equals(locale)) {
        final ParsedDocument<T> originalTrans = entry.getValue();

        final Map<String, Object> defaultMap = CastUtils.unsafeCast(yamlParser.convertToMap(rawDefaultMetadata));
        final T defaultClone = yamlParser.convertFromMap(defaultMap, getTargetClass());

        final T finalLangMetadata = frontmatterInterpolator.interpolateMergedObject(
          defaultClone.merge(originalTrans.metadata()),
          getTargetClass(),
          currentContext.getGlobals() != null ? currentContext.getGlobals().get(altLocale) : null,
          config,
          getDynamicInterpolationContext(altLocale)
        );
        yamlParser.validate(finalLangMetadata);
        onPostMergeHook(finalLangMetadata);
        translations.put(altLocale, new ParsedDocument<>(altLocale, finalLangMetadata, originalTrans.htmlContent()));
      }
    }
  }

  protected final String resolvePageId() {
    if (baseSiteDir == null || resourcePath == null) {
      return "";
    }
    final Path relativePath = baseSiteDir.relativize(resourcePath);
    final List<String> filteredParts = new ArrayList<>();
    for (final Path part : relativePath) {
      final String partStr = part.toString();
      if (!partStr.isEmpty() && !partStr.contains("[") && !partStr.contains("]")) {
        filteredParts.add(partStr);
      }
    }
    return String.join("/", filteredParts);
  }

  protected Map<String, Object> getDynamicInterpolationContext(Locale locale) {
    return new HashMap<>();
  }

  protected void persistPageRepresentation(SiteBuildContext context, PageRepresentation<T> page) {
    context.setSinglePage(getTargetClass(), page);
  }

  protected void onFileParsedHook(ParsedDocument<T> parsedDoc) {
  }

  protected void onPostMergeHook(T mergedMetadata) {
  }

  protected abstract Class<T> getTargetClass();

  protected abstract HtmlGeneratorFactory<T> getHtmlRendererFactory();
}
