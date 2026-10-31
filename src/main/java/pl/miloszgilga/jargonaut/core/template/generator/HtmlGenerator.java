package pl.miloszgilga.jargonaut.core.template.generator;

import pl.miloszgilga.jargonaut.asset.resolver.AssetAliasResolver;
import pl.miloszgilga.jargonaut.config.model.Config;
import pl.miloszgilga.jargonaut.core.TextContentType;
import pl.miloszgilga.jargonaut.core.minifier.html.HtmlMinifier;
import pl.miloszgilga.jargonaut.core.minifier.js.InlineJsMinifier;
import pl.miloszgilga.jargonaut.core.parser.GenericPage;
import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.parser.ParsedDocument;
import pl.miloszgilga.jargonaut.core.parser.json.JsonParser;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.template.TemplateEngine;
import pl.miloszgilga.jargonaut.core.template.TemplateName;
import pl.miloszgilga.jargonaut.core.template.generator.dto.BreadcrumbView;
import pl.miloszgilga.jargonaut.core.template.generator.dto.LanguageView;
import pl.miloszgilga.jargonaut.core.template.generator.dto.NavLinkView;
import pl.miloszgilga.jargonaut.core.template.generator.dto.PageSeo;
import pl.miloszgilga.jargonaut.core.template.generator.dto.SitemapNodeView;
import pl.miloszgilga.jargonaut.globals.model.GlobalsMetadata;
import pl.miloszgilga.jargonaut.rss.RssProcessor;

import java.nio.file.Path;
import java.time.Year;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public abstract class HtmlGenerator<T extends GenericPage> {
  protected final PageRepresentation<T> pageRepresentation;
  protected final ZonedDateTime buildDateTime;
  protected final JsonParser jsonParser;
  protected final TemplateEngine templateEngine;
  protected final HtmlMinifier htmlMinifier;
  protected final InlineJsMinifier inlineJsMinifier;
  protected final AssetAliasResolver assetAliasResolver;
  protected final SiteBuildContext siteBuildContext;
  protected final List<HtmlPostProcessor> postProcessors;
  protected final RssProcessor rssProcessor;

  protected HtmlGenerator(PageRepresentation<T> pageRepresentation, GeneratorContext generatorContext) {
    this.pageRepresentation = pageRepresentation;
    buildDateTime = generatorContext.getBuildDateTime();
    jsonParser = generatorContext.getJsonParser();
    templateEngine = generatorContext.getTemplateEngine();
    htmlMinifier = generatorContext.getHtmlMinifier();
    inlineJsMinifier = generatorContext.getInlineJsMinifier();
    assetAliasResolver = generatorContext.getAssetAliasResolver();
    siteBuildContext = generatorContext.getSiteBuildContext();
    postProcessors = generatorContext.getPostProcessors();
    rssProcessor = generatorContext.getRssProcessor();
  }

  public List<RenderedTextContent> render() {
    final Config config = siteBuildContext.getConfig();
    final String domain = removeEndingSlash(config.getSeo().getDomain());
    final List<RenderedTextContent> renderedTextContents = new ArrayList<>();
    for (final Locale locale : siteBuildContext.getActiveLocales()) {
      final GlobalsMetadata globals = siteBuildContext.getGlobals().get(locale);
      final ParsedDocument<T> document = pageRepresentation.translations().get(locale);
      if (document == null) {
        continue;
      }
      String pageWebPath = pageRepresentation.id();
      if (pageWebPath == null || pageWebPath.isBlank()) {
        pageWebPath = "";
      } else {
        pageWebPath = trimSlash(pageWebPath);
      }
      final String langPrefix = getUrlLanguagePrefix(locale);
      final PageSeo pageSeo = buildPageSeo(
        locale,
        globals,
        document,
        getLanguageCode(locale),
        domain,
        buildCanonicalUrl(domain, buildFullWebPath(langPrefix, pageWebPath)),
        pageWebPath,
        config,
        langPrefix
      );
      final Map<String, Object> i18n = siteBuildContext.getDictionaries().get(locale);
      renderedTextContents.add(renderHtml(
        locale,
        config,
        globals,
        document,
        pageSeo,
        i18n,
        langPrefix,
        pageWebPath
      ));
    }
    return renderedTextContents;
  }

  protected JsonLdMap createBaseSchema(String type, String canonicalUrl, String language, ParsedDocument<T> document) {
    return new JsonLdMap()
      .add("@context", "https://schema.org")
      .add("@type", type)
      .add("name", document.metadata().getPageTitle())
      .add("description", document.metadata().getPageDescription())
      .add("url", canonicalUrl)
      .add("inLanguage", language);
  }

  protected JsonLdMap createPersonSchema(String canonicalUrl, GlobalsMetadata globals) {
    return new JsonLdMap()
      .add("@type", "Person")
      .add("name", globals.getName())
      .add("jobTitle", globals.getJobTitle())
      .add("url", canonicalUrl)
      .add("honorificPrefix", globals.getHonorificPrefixes())
      .add("honorificSuffix", globals.getHonorificSuffixes());
  }

  private String buildFullWebPath(String langPrefix, String pageWebPath) {
    String fullWebPath = langPrefix;
    if (!pageWebPath.isEmpty()) {
      fullWebPath = fullWebPath.isEmpty() ? pageWebPath : fullWebPath + "/" + pageWebPath;
    }
    return fullWebPath;
  }

  private String buildCanonicalUrl(String domain, String fullWebPath) {
    return domain + (fullWebPath.isEmpty() ? "/" : "/" + fullWebPath + "/");
  }

  private PageSeo buildPageSeo(Locale locale,
                               GlobalsMetadata globals,
                               ParsedDocument<T> document,
                               String languageCode,
                               String domain,
                               String canonicalUrl,
                               String pageWebPath,
                               Config config,
                               String langPrefix) {
    final PageSeo.Builder pageSeoBuilder = PageSeo.builder()
      .setDomain(buildCanonicalUrl(domain, buildFullWebPath(langPrefix, "")))
      .setHtmlLanguage(languageCode)
      .setOgLocale(locale)
      .setPageName(document.metadata().getPageName())
      .setPageTitle(document.metadata().getPageTitle())
      .setPageDescription(document.metadata().getPageDescription())
      .setGtmId(config.getAnalytics().getGtm())
      .setAuthorName(globals.getName())
      .setJobTitle(globals.getJobTitle())
      .setFaviconUrl(config.getSeo().getFavicon() != null ? "/" + config.getSeo().getFavicon() : null)
      .setOgImageUrl(config.getSeo().getOgImage() != null ? domain + "/" + config.getSeo().getOgImage() : null)
      .setCanonicalUrl(canonicalUrl)
      .setRssFeedUrl(rssProcessor == null ? null : rssProcessor.getRssFeedUrl(config))
      .addStyleFileNames(extractAssetPaths(siteBuildContext, TextContentType.CSS))
      .addScriptFileNames(extractAssetPaths(siteBuildContext, TextContentType.JS));
    for (final Locale altLocale : siteBuildContext.getActiveLocales()) {
      final String altLangPrefix = getUrlLanguagePrefix(altLocale);
      final String altFullWebPath = buildFullWebPath(altLangPrefix, pageWebPath);
      final String alternateUrl = buildCanonicalUrl(domain, altFullWebPath);
      if (altLocale.equals(config.getDefaultLocale())) {
        pageSeoBuilder.addAlternateUrl("x-default", alternateUrl);
      }
      pageSeoBuilder.addAlternateUrl(getLanguageCode(altLocale), alternateUrl);
    }
    Object jsonLdObj = generateJsonLdScript(canonicalUrl, languageCode, globals, document);
    if (jsonLdObj != null) {
      pageSeoBuilder.setJsonLdScript(jsonParser.writeValueAsString(jsonLdObj));
    }
    return pageSeoBuilder.build();
  }

  private List<String> extractAssetPaths(SiteBuildContext context, TextContentType type) {
    final List<RenderedTextContent> assets = context.getRenderedTextContents().get(type);
    if (assets == null) {
      return List.of();
    }
    return assets.stream()
      .map(asset -> asset.outputPath().toString())
      .toList();
  }

  private RenderedTextContent renderHtml(Locale locale,
                                         Config config,
                                         GlobalsMetadata globals,
                                         ParsedDocument<T> document,
                                         PageSeo pageSeo,
                                         Map<String, Object> i18n,
                                         String langPrefix,
                                         String pageWebPath) {
    final Map<String, Object> pageAttributes = new HashMap<>();
    pageAttributes.put("seo", pageSeo);
    pageAttributes.put("currentYear", Year.now().getValue());
    pageAttributes.put("i18n", i18n == null ? Map.of() : i18n);
    pageAttributes.put("globals", globals);
    pageAttributes.put("langPrefix", prependSlash(langPrefix));
    pageAttributes.put("mdContent", document.htmlContent());
    pageAttributes.put("generator", config.getGenerator());
    pageAttributes.put("lastUpdatedAt", buildDateTime.format(DateTimeFormatter.ofPattern(globals.getDtfPattern())));
    pageAttributes.put("syntaxParserAssets", config.getParser().getSyntax().getAdditionalAssets());
    pageAttributes.put("mathParserAssets", config.getParser().getMath().getAdditionalAssets());
    pageAttributes.put("languageLinks", createLanguageLinks(pageWebPath, locale));
    pageAttributes.put("navLinks", createNavLinks(globals, prependSlash(langPrefix), pageWebPath));
    pageAttributes.put("breadcrumbs", createBreadcrumbPoints(langPrefix, locale, pageWebPath));
    pageAttributes.put("sitemapTree", createSitemapTree(langPrefix, locale, pageWebPath));
    insertPageAttributes(pageAttributes, locale, document, siteBuildContext);
    for (final HtmlPostProcessor postProcessor : postProcessors) {
      postProcessor.insertPageAttributesBeforeRender(pageAttributes);
    }
    String htmlContent = templateEngine.render(getHtmlTemplateName(), pageAttributes, locale);
    for (final HtmlPostProcessor postProcessor : postProcessors) {
      htmlContent = postProcessor.process(htmlContent, locale, siteBuildContext);
    }
    htmlContent = assetAliasResolver.resolveAliases(
      htmlContent,
      pageRepresentation.baseDir(),
      siteBuildContext.getAssetTargets()
    );
    if (Boolean.TRUE.equals(config.getMinify())) {
      htmlContent = htmlMinifier.minify(htmlContent);
      htmlContent = inlineJsMinifier.minify(htmlContent);
    }
    return new RenderedTextContent(buildOutputPath(langPrefix, pageWebPath, document.metadata()), htmlContent);
  }

  private List<LanguageView> createLanguageLinks(String pageWebPath, Locale locale) {
    final List<LanguageView> languageLinks = new ArrayList<>();
    for (final Locale altLocale : siteBuildContext.getActiveLocales()) {
      final String altFullWebPath = buildFullWebPath(getUrlLanguagePrefix(altLocale), pageWebPath);
      final String relativeUrl = removeMultipleSlash("/" + altFullWebPath);
      languageLinks.add(new LanguageView(relativeUrl, getLanguageCode(altLocale), altLocale, locale));
    }
    return languageLinks;
  }

  private List<NavLinkView> createNavLinks(GlobalsMetadata globals, String langPrefix, String pageWebPath) {
    if (globals.getNavLinks() == null || globals.getNavLinks().isEmpty()) {
      return Collections.emptyList();
    }
    final String curPageFullUrl = removeMultipleSlash(langPrefix + "/" + pageWebPath);
    final String normalizedCurrentPageUrl = curPageFullUrl.equals("/") ? "/" : removeEndingSlash(curPageFullUrl);

    return globals.getNavLinks().stream()
      .map(navLink -> {
        final boolean isExternal = navLink.getUrl() != null && navLink.getUrl().matches("(?i)^https?://.*");
        String url = navLink.getUrl();
        if (!isExternal) {
          url = removeMultipleSlash(langPrefix + "/" + url);
        }
        final String normalizedUrl = url.equals("/") ? "/" : removeEndingSlash(url);
        final boolean isCurrent = !isExternal && normalizedUrl.equals(normalizedCurrentPageUrl);
        return new NavLinkView(navLink.getLabel(), url, isCurrent);
      })
      .toList();
  }

  private String getUrlLanguagePrefix(Locale currentLocale) {
    final Locale defaultLocale = siteBuildContext.getConfig().getDefaultLocale();
    if (currentLocale.equals(defaultLocale)) {
      return "";
    }
    return getLanguageCode(currentLocale);
  }

  private String getLanguageCode(Locale currentLocale) {
    final Set<Locale> activeLocales = siteBuildContext.getActiveLocales();
    final long sameLanguageCount = activeLocales.stream()
      .filter(loc -> loc.getLanguage().equals(currentLocale.getLanguage()))
      .count();
    if (sameLanguageCount == 1) {
      return currentLocale.getLanguage().toLowerCase();
    }
    return currentLocale.toLanguageTag().toLowerCase();
  }

  private Path buildOutputPath(String langPrefix, String pageWebPath, T metadata) {
    final Path path = langPrefix.isEmpty() ? Path.of("") : Path.of(langPrefix);
    if (pageWebPath.isEmpty()) {
      return path.resolve("index.html");
    }
    if (Boolean.TRUE.equals(metadata.getDirectOutput())) {
      return path.resolve(pageWebPath + ".html");
    }
    return path.resolve(pageWebPath).resolve("index.html");
  }

  private String removeMultipleSlash(String expression) {
    return expression.replaceAll("//+", "/");
  }

  private String removeEndingSlash(String expression) {
    return expression.replaceAll("/+$", "");
  }

  private String trimSlash(String expression) {
    return expression.replaceAll("^/+|/+$", "");
  }

  private String prependSlash(String expression) {
    return expression.isEmpty() ? "" : "/" + expression;
  }

  private List<BreadcrumbView> createBreadcrumbPoints(String langPrefix, Locale locale, String pageWebPath) {
    final List<BreadcrumbView> breadcrumbs = new ArrayList<>();
    final GenericPage homePage = findPageByPathOrSegment("", locale);
    final boolean isHomePageCurrent = (pageWebPath == null || pageWebPath.isBlank() || pageWebPath.equals("/"));
    if (homePage != null && homePage.getPageName() != null) {
      breadcrumbs.add(new BreadcrumbView(homePage.getPageName(), prependSlash(langPrefix) + "/", isHomePageCurrent));
    }
    if (isHomePageCurrent) {
      return breadcrumbs;
    }
    final String cleanPath = pageWebPath.replaceAll("^/+|/+$", "");
    final String[] segments = cleanPath.split("/");
    final StringBuilder currentPath = new StringBuilder();
    for (int i = 0; i < segments.length; i++) {
      final String segment = segments[i];
      currentPath.append("/");
      currentPath.append(segment);
      final String builtUrl = prependSlash(langPrefix) + currentPath;
      final GenericPage foundPage = findPageByPathOrSegment(currentPath.toString(), locale);
      final boolean isCurrent = (i == segments.length - 1);
      if (foundPage != null && foundPage.getPageName() != null) {
        breadcrumbs.add(new BreadcrumbView(foundPage.getPageName(), builtUrl, isCurrent));
      }
    }
    return breadcrumbs;
  }

  private List<SitemapNodeView> createSitemapTree(String langPrefix, Locale locale, String pageWebPath) {
    final GenericPage homePage = findPageByPathOrSegment("", locale);
    if (homePage == null || homePage.getPageName() == null) {
      return Collections.emptyList();
    }
    final String rootUrl = prependSlash(langPrefix) + "/";
    final MutableSitemapNode root = new MutableSitemapNode(homePage.getPageName(), rootUrl);

    String normalizedCurrentPath = "";
    if (pageWebPath != null && !pageWebPath.isBlank() && !pageWebPath.equals("/")) {
      normalizedCurrentPath = trimSlash(pageWebPath).toLowerCase();
    }
    if (normalizedCurrentPath.isEmpty()) {
      root.setCurrent(true);
      root.setExpanded(true);
    }
    final List<PageRepresentation<?>> allPages = new ArrayList<>(siteBuildContext.getAllSinglePages().values());
    for (final List<PageRepresentation<?>> collection : siteBuildContext.getAllCollectionPages().values()) {
      allPages.addAll(collection);
    }
    allPages.sort(Comparator.comparing(p -> p.id() == null ? "" : p.id()));

    for (final PageRepresentation<?> pageRep : allPages) {
      final String id = pageRep.id() == null ? "" : pageRep.id();
      if (id.isEmpty()) {
        continue;
      }
      final ParsedDocument<?> doc = pageRep.translations().get(locale);
      if (doc == null || !(doc.metadata() instanceof GenericPage genericPage)) {
        continue;
      }
      if (!Boolean.TRUE.equals(genericPage.getListable())) {
        continue;
      }
      final String label = genericPage.getPageName() != null ? genericPage.getPageName() : id;
      final String url = prependSlash(langPrefix) + "/" + id;
      final boolean isCurrent = id.equalsIgnoreCase(normalizedCurrentPath);

      final String[] segments = id.split("/");
      MutableSitemapNode current = root;
      if (isCurrent) {
        root.setExpanded(true);
      }
      final StringBuilder pathSoFar = new StringBuilder();
      for (int i = 0; i < segments.length; i++) {
        final String segment = segments[i];
        if (i > 0) {
          pathSoFar.append("/");
        }
        pathSoFar.append(segment);
        final String currentPathStr = pathSoFar.toString();
        current = current.getChildren().computeIfAbsent(segment, k -> {
          final String interUrl = prependSlash(langPrefix) + "/" + currentPathStr;
          return new MutableSitemapNode(segment, interUrl);
        });
        if (isCurrent) {
          current.setExpanded(true);
        }
      }
      if (Boolean.TRUE.equals(genericPage.getExpanded())) {
        current.setExpanded(true);
      }
      current.setLabel(label);
      current.setUrl(url);
      current.setCurrent(isCurrent);
    }
    return List.of(root.toView("_sm_home"));
  }

  private GenericPage findPageByPathOrSegment(String targetPath, Locale locale) {
    if (targetPath == null) {
      targetPath = "";
    }
    final String normalizedTarget = targetPath.replaceAll("^/+", "").toLowerCase();
    final boolean isHomeQuery = normalizedTarget.isEmpty();
    for (final PageRepresentation<?> pageRep : siteBuildContext.getAllSinglePages().values()) {
      final String pageId = pageRep.id() != null ? pageRep.id().toLowerCase() : "";
      boolean matches = pageId.equalsIgnoreCase(normalizedTarget);
      if (!matches && isHomeQuery) {
        matches = pageId.isEmpty();
      }
      if (!matches) {
        continue;
      }
      if (!pageRep.translations().containsKey(locale)) {
        continue;
      }
      final ParsedDocument<?> doc = pageRep.translations().get(locale);
      if (doc.metadata() instanceof GenericPage genericPage) {
        return genericPage;
      }
    }
    for (final List<PageRepresentation<?>> pageList : siteBuildContext.getAllCollectionPages().values()) {
      for (final PageRepresentation<?> pageRep : pageList) {
        final String pageId = pageRep.id() != null ? pageRep.id().toLowerCase() : "";
        if (!pageId.equalsIgnoreCase(normalizedTarget)) {
          continue;
        }
        if (!pageRep.translations().containsKey(locale)) {
          continue;
        }
        final ParsedDocument<?> doc = pageRep.translations().get(locale);
        if (doc.metadata() instanceof GenericPage genericPage) {
          return genericPage;
        }
      }
    }
    return null;
  }

  protected abstract Object generateJsonLdScript(String canonicalUrl,
                                                 String language,
                                                 GlobalsMetadata globals,
                                                 ParsedDocument<T> document);

  protected abstract TemplateName getHtmlTemplateName();

  protected void insertPageAttributes(Map<String, Object> pageAttributes,
                                      Locale locale,
                                      ParsedDocument<T> document,
                                      SiteBuildContext context) {
  }
}
