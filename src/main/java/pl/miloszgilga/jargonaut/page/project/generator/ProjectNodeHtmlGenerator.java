package pl.miloszgilga.jargonaut.page.project.generator;

import pl.miloszgilga.jargonaut.asset.AssetTarget;
import pl.miloszgilga.jargonaut.core.ReadingTimeUtils;
import pl.miloszgilga.jargonaut.core.extractor.contributions.ContributionCalendarView;
import pl.miloszgilga.jargonaut.core.extractor.stats.LanguageStat;
import pl.miloszgilga.jargonaut.core.extractor.stats.RepoStats;
import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.parser.ParsedDocument;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.template.FreemarkerTemplateName;
import pl.miloszgilga.jargonaut.core.template.TemplateName;
import pl.miloszgilga.jargonaut.core.template.generator.GeneratorContext;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlGenerator;
import pl.miloszgilga.jargonaut.core.template.generator.JsonLdMap;
import pl.miloszgilga.jargonaut.globals.model.GlobalsMetadata;
import pl.miloszgilga.jargonaut.page.project.ProjectStatsUtils;
import pl.miloszgilga.jargonaut.page.project.dto.ProjectDetailsView;
import pl.miloszgilga.jargonaut.page.project.model.Link;
import pl.miloszgilga.jargonaut.page.project.model.ProjectNodeMetadata;
import pl.miloszgilga.jargonaut.page.project.model.ProjectSharedData;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ProjectNodeHtmlGenerator extends HtmlGenerator<ProjectNodeMetadata> {
  public ProjectNodeHtmlGenerator(PageRepresentation<ProjectNodeMetadata> pageRepresentation,
                                  GeneratorContext generatorContext) {
    super(pageRepresentation, generatorContext);
  }

  @Override
  protected Object generateJsonLdScript(String canonicalUrl,
                                        String language,
                                        GlobalsMetadata globals,
                                        ParsedDocument<ProjectNodeMetadata> document) {
    return createBaseSchema("SoftwareApplication", canonicalUrl, language, document)
      .add("applicationCategory", document.metadata().getKind())
      .add("operatingSystem", "Any")
      .addList("codeRepository", document.metadata().getRepoUrls(), Link::getUrl)
      .addList("downloadUrl", document.metadata().getDownloadUrls(), Link::getUrl)
      .addList("softwareHelp", document.metadata().getDocsUrls(), Link::getUrl)
      .add("keywords", document.metadata().getTech() != null
        ? String.join(", ", document.metadata().getTech())
        : null)
      .add("author", createPersonSchema(canonicalUrl, globals))
      .add("offers", new JsonLdMap()
        .add("@type", "Offer")
        .add("price", "0")
        .add("priceCurrency", "PLN")
      );
  }

  @Override
  protected TemplateName getHtmlTemplateName() {
    return FreemarkerTemplateName.PROJECT_NODE_PAGE;
  }

  @Override
  protected void insertPageAttributes(Map<String, Object> pageAttributes,
                                      Locale locale,
                                      ParsedDocument<ProjectNodeMetadata> document,
                                      SiteBuildContext context) {
    pageAttributes.put("project", createProjectDetailsView(locale, document, context));
    pageAttributes.put("technologies", document.metadata().getTechDetails());
    pageAttributes.put("calendar", createCalendarView(context, locale));
    pageAttributes.put("languages", createLanguages(context, locale));
    pageAttributes.put("galleryImages", createGalleryImageLinks(context));
    pageAttributes.put("readingTime", ReadingTimeUtils.calculateMinutesFromHtml(document.htmlContent()));
  }

  private ProjectDetailsView createProjectDetailsView(Locale locale,
                                                      ParsedDocument<ProjectNodeMetadata> document,
                                                      SiteBuildContext context) {
    final String projectId = pageRepresentation.id();
    boolean recentlyUpdated = false;
    final ProjectSharedData projectData = context.getSharedData(ProjectSharedData.class);
    if (projectData != null && projectData.getProjectRepoStats() != null) {
      recentlyUpdated = ProjectStatsUtils.isRecentlyUpdated(projectData, projectId, locale,
        context.getConfig().getProject().getRecentlyUpdatedDeltaDays());
    }
    return new ProjectDetailsView(projectId, document.metadata(), recentlyUpdated);
  }

  private ContributionCalendarView createCalendarView(SiteBuildContext context, Locale locale) {
    final RepoStats aggregatedStats = extractProjectRepoStats(context, locale);
    if (aggregatedStats == null) {
      return null;
    }
    return new ContributionCalendarView(aggregatedStats.contributionCalendar(), locale);
  }


  private List<LanguageStat> createLanguages(SiteBuildContext context, Locale locale) {
    final RepoStats aggregatedStats = extractProjectRepoStats(context, locale);
    if (aggregatedStats == null) {
      return Collections.emptyList();
    }
    return aggregatedStats.languages();
  }

  private RepoStats extractProjectRepoStats(SiteBuildContext context, Locale locale) {
    final ProjectSharedData projectData = context.getSharedData(ProjectSharedData.class);
    if (projectData == null || projectData.getProjectRepoStats() == null) {
      return null;
    }
    final Map<Locale, RepoStats> localeStats = projectData
      .getProjectRepoStats()
      .get(pageRepresentation.id());
    if (localeStats != null && localeStats.containsKey(locale)) {
      return localeStats.get(locale);
    }
    return null;
  }

  private List<String> createGalleryImageLinks(SiteBuildContext context) {
    final List<Path> galleryFiles = pageRepresentation.assetsFiles().get("gallery");
    if (galleryFiles == null) {
      return Collections.emptyList();
    }
    final Map<Path, AssetTarget> assetTargets = context.getAssetTargets();
    final List<AssetTarget> galleryImages = galleryFiles.stream()
      .map(assetTargets::get)
      .filter(Objects::nonNull)
      .toList();
    if (galleryImages.isEmpty()) {
      return Collections.emptyList();
    }
    return galleryImages.stream().map(AssetTarget::shortPath).toList();
  }
}
