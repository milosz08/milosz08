package pl.miloszgilga.jargonaut.core.processor.md;

import pl.miloszgilga.jargonaut.core.GenerationHaltedException;
import pl.miloszgilga.jargonaut.core.parser.GenericPage;
import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.parser.ParsedDocument;
import pl.miloszgilga.jargonaut.core.processor.MergeableMetadata;
import pl.miloszgilga.jargonaut.core.processor.ProcessorContext;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.template.generator.GeneratorContext;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlGenerator;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlGeneratorFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public abstract class DynamicPageMergingMdProcessor<T extends GenericPage & MergeableMetadata<T>>
  extends PageMergingMdProcessor<T> {
  private static final Pattern DYNAMIC_FOLDER_PATTERN = Pattern.compile("\\[\\.\\.\\.(.+?)]");
  private static final String DYNAMIC_PREFIX = "dynamic";

  protected final Path parentResourcePath;
  protected final String dynamicParameterName;

  protected String currentDynamicSlug;
  protected Map<Locale, String> currentDynamicReplacements;
  protected Locale currentDefaultLocale;

  protected DynamicPageMergingMdProcessor(String parentDir, ProcessorContext processorContext) {
    super(parentDir + "/" + findDynamicFolderName(processorContext.getBaseSitesDir().resolve(parentDir)),
      processorContext);
    this.parentResourcePath = processorContext.getBaseSitesDir().resolve(parentDir);
    dynamicParameterName = extractDynamicParameterName();
  }

  @Override
  protected final void process(SiteBuildContext context,
                               Map<String, List<Path>> processedAssets,
                               Locale defaultLocale) {
    currentContext = context;
    currentDefaultLocale = defaultLocale;
    Set<Locale> availableLocales = context.getActiveLocales();
    if (availableLocales.isEmpty()) {
      availableLocales = Set.of(defaultLocale);
    }
    final Map<String, Map<Locale, String>> slugReplacements = new HashMap<>();
    for (final Locale locale : availableLocales) {
      final List<DynamicPageData> dynamicValues = provideDynamicValues(context, locale);
      if (dynamicValues == null) {
        continue;
      }
      for (final DynamicPageData data : dynamicValues) {
        slugReplacements
          .computeIfAbsent(data.slug(), k -> new HashMap<>())
          .put(locale, data.replacementText());
      }
    }
    if (slugReplacements.isEmpty()) {
      return;
    }
    final List<PageRepresentation<T>> generatedPages = new ArrayList<>();
    for (final Map.Entry<String, Map<Locale, String>> entry : slugReplacements.entrySet()) {
      currentDynamicSlug = entry.getKey();
      currentDynamicReplacements = entry.getValue();
      final Map<Locale, ParsedDocument<T>> translations = loadTranslationsFromDirectory(resourcePath, defaultLocale);
      mergeTranslations(translations, defaultLocale);
      generatedPages.add(new PageRepresentation<>(
        resolveDynamicPageId(currentDynamicSlug),
        resourcePath,
        translations,
        processedAssets,
        getHtmlRendererFactory()
      ));
      context.getActiveLocales().addAll(translations.keySet());
    }
    currentDynamicSlug = null;
    currentDynamicReplacements = null;
    currentDefaultLocale = null;
    context.setCollectionPages(getTargetClass(), generatedPages);
  }

  @Override
  protected final void persistPageRepresentation(SiteBuildContext context, PageRepresentation<T> page) {
  }

  @Override
  protected final boolean isLoadingAssets() {
    return false;
  }

  @Override
  protected final boolean isBaseResourceExists() {
    return Files.exists(parentResourcePath) && Files.isDirectory(parentResourcePath);
  }

  @Override
  protected final String getTargetWebDir() {
    return currentDynamicSlug != null ? resolveDynamicPageId(currentDynamicSlug) : resolvePageId();
  }

  @Override
  protected final HtmlGeneratorFactory<T> getHtmlRendererFactory() {
    final String capturedSlug = currentDynamicSlug;
    return (page, context) -> createHtmlGenerator(page, context, capturedSlug);
  }

  @Override
  protected Map<String, Object> getDynamicInterpolationContext(Locale locale) {
    if (dynamicParameterName == null || currentDynamicReplacements == null) {
      return new HashMap<>();
    }
    String replacement = currentDynamicReplacements.get(locale);
    if (replacement == null && currentDefaultLocale != null) {
      replacement = currentDynamicReplacements.get(currentDefaultLocale);
    }
    if (replacement == null && !currentDynamicReplacements.isEmpty()) {
      replacement = currentDynamicReplacements.values().iterator().next();
    }
    if (replacement != null) {
      return Map.of(DYNAMIC_PREFIX + "." + dynamicParameterName, replacement);
    }
    return new HashMap<>();
  }

  private static String findDynamicFolderName(Path parentPath) {
    if (!Files.exists(parentPath) || !Files.isDirectory(parentPath)) {
      return "[...unknown]";
    }
    try (final Stream<Path> paths = Files.list(parentPath)) {
      return paths
        .filter(Files::isDirectory)
        .map(path -> path.getFileName().toString())
        .filter(folderName -> folderName.startsWith("[...") && folderName.endsWith("]"))
        .findFirst()
        .orElseThrow(() -> new GenerationHaltedException(
          "Unable to find dynamic directory ([...]) inside: " + parentPath));
    } catch (IOException ex) {
      throw new GenerationHaltedException(ex);
    }
  }

  private String extractDynamicParameterName() {
    if (baseSiteDir == null || resourcePath == null) {
      return null;
    }
    final Path relativePath = baseSiteDir.relativize(resourcePath);
    for (final Path part : relativePath) {
      final Matcher matcher = DYNAMIC_FOLDER_PATTERN.matcher(part.toString());
      if (matcher.find()) {
        return matcher.group(1);
      }
    }
    return null;
  }

  private String resolveDynamicPageId(String slug) {
    final String baseId = resolvePageId();
    return baseId.isEmpty() ? slug : baseId + "/" + slug;
  }

  protected abstract List<DynamicPageData> provideDynamicValues(SiteBuildContext context, Locale locale);

  protected abstract HtmlGenerator<T> createHtmlGenerator(PageRepresentation<T> page,
                                                          GeneratorContext context,
                                                          String slug);
}
