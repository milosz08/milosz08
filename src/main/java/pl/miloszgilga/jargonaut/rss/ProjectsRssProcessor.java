package pl.miloszgilga.jargonaut.rss;

import pl.miloszgilga.jargonaut.config.model.Config;
import pl.miloszgilga.jargonaut.core.TextContentType;
import pl.miloszgilga.jargonaut.core.extractor.stats.RepoStats;
import pl.miloszgilga.jargonaut.core.minifier.xml.XmlMinifier;
import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.template.FreemarkerTemplateName;
import pl.miloszgilga.jargonaut.core.template.TemplateEngine;
import pl.miloszgilga.jargonaut.core.template.generator.RenderedTextContent;
import pl.miloszgilga.jargonaut.page.project.model.ProjectNodeMetadata;
import pl.miloszgilga.jargonaut.page.project.model.ProjectSharedData;
import pl.miloszgilga.jargonaut.rss.model.RssProject;

import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProjectsRssProcessor implements RssProcessor {
  private static final DateTimeFormatter RSS_DATE_FORMATTER =
    DateTimeFormatter.RFC_1123_DATE_TIME.withZone(ZoneOffset.UTC);

  private final ZonedDateTime buildDateTime;
  private final TemplateEngine templateEngine;
  private final XmlMinifier xmlMinifier;

  public ProjectsRssProcessor(ZonedDateTime buildDateTime, TemplateEngine templateEngine, XmlMinifier xmlMinifier) {
    this.buildDateTime = buildDateTime;
    this.templateEngine = templateEngine;
    this.xmlMinifier = xmlMinifier;
  }

  @Override
  public void process(SiteBuildContext context) {
    final ProjectSharedData projectData = context.getSharedData(ProjectSharedData.class);
    if (projectData == null) {
      return;
    }
    final Config config = context.getConfig();
    final String domain = config.getSeo().getDomain().replaceAll("/+$", "");

    final List<RenderedTextContent> rssFeeds = new ArrayList<>();
    for (final Locale locale : context.getActiveLocales()) {
      final String baseUrl = buildCanonicalUrl(context, locale, domain, "");

      final Map<String, Object> templateModel = new HashMap<>();
      templateModel.put("name", context.getGlobals().get(locale).getName());
      templateModel.put("description", context.getGlobals().get(locale).getRssDescription());
      templateModel.put("baseUrl", baseUrl);
      templateModel.put("locale", locale.toLanguageTag());
      templateModel.put("rssFeedUrl", baseUrl + getRssFeedUrl(config));
      templateModel.put("buildDate", RSS_DATE_FORMATTER.format(buildDateTime));

      final List<RssProject> rssProjects = new ArrayList<>();
      for (final PageRepresentation<ProjectNodeMetadata> project : projectData.getAllProjects()) {
        final ProjectNodeMetadata metadata = project.translations().get(locale).metadata();
        Instant latestCommit = null;
        final Map<Locale, RepoStats> localeRepoStatsMap = projectData.getProjectRepoStats().get(project.id());
        if (localeRepoStatsMap != null && localeRepoStatsMap.get(locale) != null) {
          latestCommit = localeRepoStatsMap.get(locale).lastCommitDate();
        }
        if (latestCommit == null) {
          latestCommit = buildDateTime.toInstant();
        }
        rssProjects.add(new RssProject(
          metadata.getTitle(),
          metadata.getSummary(),
          buildCanonicalUrl(context, locale, domain, project.id()),
          RSS_DATE_FORMATTER.format(latestCommit),
          metadata.getTech()
        ));
      }
      templateModel.put("projects", rssProjects);
      String content = templateEngine.render(FreemarkerTemplateName.RSS_FEED, templateModel, locale);
      if (Boolean.TRUE.equals(context.getConfig().getMinify())) {
        content = xmlMinifier.minify(content);
      }
      final String localizedPath = getLocalizedPath(context, locale, "");
      Path outputRssPath = Path.of(localizedPath, getRssFeedUrl(config));
      if (localizedPath.isEmpty()) {
        outputRssPath = Path.of(getRssFeedUrl(config));
      }
      rssFeeds.add(new RenderedTextContent(outputRssPath, content));
    }
    context.getRenderedTextContents().put(TextContentType.XML, rssFeeds);
  }

  @Override
  public String getRssFeedUrl(Config config) {
    return config.getRssOutput();
  }

  private String buildCanonicalUrl(SiteBuildContext context, Locale locale, String domain, String pageWebPath) {
    final String path = getLocalizedPath(context, locale, pageWebPath);
    return path.isEmpty() ? domain + "/" : domain + "/" + path + "/";
  }

  private String getLocalizedPath(SiteBuildContext context, Locale locale, String pageWebPath) {
    String path = "";
    if (!locale.equals(context.getConfig().getDefaultLocale())) {
      final boolean isUniqueLanguage = context.getActiveLocales().stream()
        .filter(loc -> loc.getLanguage().equals(locale.getLanguage()))
        .count() == 1;
      path = isUniqueLanguage ? locale.getLanguage().toLowerCase() : locale.toLanguageTag().toLowerCase();
    }
    if (pageWebPath != null && !pageWebPath.isEmpty()) {
      path = path.isEmpty() ? pageWebPath : path + "/" + pageWebPath;
    }
    return path;
  }
}
