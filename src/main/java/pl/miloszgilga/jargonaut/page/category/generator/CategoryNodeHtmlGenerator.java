package pl.miloszgilga.jargonaut.page.category.generator;

import pl.miloszgilga.jargonaut.core.extractor.contributions.ContributionCalendarView;
import pl.miloszgilga.jargonaut.core.extractor.stats.LanguageStat;
import pl.miloszgilga.jargonaut.core.extractor.stats.RepoStats;
import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.parser.ParsedDocument;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.template.FreemarkerTemplateName;
import pl.miloszgilga.jargonaut.core.template.TemplateName;
import pl.miloszgilga.jargonaut.core.template.generator.DynamicHtmlGenerator;
import pl.miloszgilga.jargonaut.core.template.generator.GeneratorContext;
import pl.miloszgilga.jargonaut.globals.model.GlobalsMetadata;
import pl.miloszgilga.jargonaut.page.category.model.CategoryNodeMetadata;
import pl.miloszgilga.jargonaut.page.project.ProjectComparator;
import pl.miloszgilga.jargonaut.page.project.ProjectStatsUtils;
import pl.miloszgilga.jargonaut.page.project.dto.ProjectTableView;
import pl.miloszgilga.jargonaut.page.project.model.Category;
import pl.miloszgilga.jargonaut.page.project.model.ProjectNodeMetadata;
import pl.miloszgilga.jargonaut.page.project.model.ProjectSharedData;
import pl.miloszgilga.jargonaut.page.tag.model.TagItem;
import pl.miloszgilga.jargonaut.page.tag.model.TagSharedData;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CategoryNodeHtmlGenerator extends DynamicHtmlGenerator<CategoryNodeMetadata> {
  public CategoryNodeHtmlGenerator(PageRepresentation<CategoryNodeMetadata> pageRepresentation,
                                   GeneratorContext generatorContext,
                                   String slug) {
    super(pageRepresentation, generatorContext, slug);
  }

  @Override
  protected Object generateJsonLdScript(String canonicalUrl,
                                        String language,
                                        GlobalsMetadata globals,
                                        ParsedDocument<CategoryNodeMetadata> document) {
    return createBaseSchema("CollectionPage", canonicalUrl, language, document)
      .add("author", createPersonSchema(canonicalUrl, globals));
  }

  @Override
  protected TemplateName getHtmlTemplateName() {
    return FreemarkerTemplateName.CATEGORY_NODE_PAGE;
  }

  @Override
  protected void insertPageAttributes(Map<String, Object> pageAttributes,
                                      Locale locale,
                                      ParsedDocument<CategoryNodeMetadata> document,
                                      SiteBuildContext context) {
    pageAttributes.put("categoryName", createTargetCategoryName(context, locale));
    pageAttributes.put("categoryProjects", createCategoryProjects(context, locale));
    pageAttributes.put("aggregatedLanguages", createAggregatedLanguages(context, locale));
    pageAttributes.put("aggregatedCalendar", createAggregatedCalendarView(context, locale));
  }

  private String createTargetCategoryName(SiteBuildContext context, Locale locale) {
    final Category targetCategory = findTargetCategory(context);
    if (targetCategory == null) {
      return null;
    }
    return targetCategory.getLocalizedNames().get(locale);
  }

  private List<ProjectTableView> createCategoryProjects(SiteBuildContext context, Locale locale) {
    final ProjectSharedData projectData = context.getSharedData(ProjectSharedData.class);
    final TagSharedData tagData = context.getSharedData(TagSharedData.class);
    if (projectData == null || tagData == null) {
      return Collections.emptyList();
    }
    final Category category = findTargetCategory(context);
    if (category == null) {
      return Collections.emptyList();
    }
    final List<TagItem> tags = tagData.getTagItems().get(locale);
    return category.getProjects().stream()
      .sorted(new ProjectComparator(locale, context.getConfig()))
      .map(project -> {
        final ParsedDocument<ProjectNodeMetadata> parsedDocument = project.translations().get(locale);
        if (parsedDocument == null) {
          return null;
        }
        final boolean recentlyUpdated = ProjectStatsUtils.isRecentlyUpdated(projectData, project.id(), locale,
          context.getConfig().getProject().getRecentlyUpdatedDeltaDays());
        return new ProjectTableView(project.id(), parsedDocument.metadata(), tags, recentlyUpdated);
      })
      .filter(Objects::nonNull)
      .toList();
  }

  private List<LanguageStat> createAggregatedLanguages(SiteBuildContext context, Locale locale) {
    final RepoStats aggregatedStats = aggregateCategoryStats(context, locale, createCategoryProjects(context, locale));
    if (aggregatedStats == null) {
      return Collections.emptyList();
    }
    return aggregatedStats.languages();
  }

  private ContributionCalendarView createAggregatedCalendarView(SiteBuildContext context, Locale locale) {
    final RepoStats aggregatedStats = aggregateCategoryStats(context, locale, createCategoryProjects(context, locale));
    if (aggregatedStats == null) {
      return null;
    }
    return new ContributionCalendarView(aggregatedStats.contributionCalendar(), locale);
  }

  private RepoStats aggregateCategoryStats(SiteBuildContext context, Locale locale, List<ProjectTableView> projects) {
    final ProjectSharedData projectData = context.getSharedData(ProjectSharedData.class);
    if (projectData == null || projects.isEmpty()) {
      return null;
    }
    final List<RepoStats> statsList = projects.stream()
      .map(project -> ProjectStatsUtils.getRepoStats(projectData, project.id(), locale))
      .filter(Objects::nonNull)
      .toList();
    return RepoStats.aggregate(statsList);
  }

  private Category findTargetCategory(SiteBuildContext context) {
    final String rootCategoryId = context.getConfig().getProject().getRootCategoryId();
    final ProjectSharedData projectData = context.getSharedData(ProjectSharedData.class);
    if (projectData == null) {
      return null;
    }
    final Category rootCategory = projectData.getRootProjectCategory();
    if (rootCategory == null) {
      return null;
    }
    if (rootCategoryId.equalsIgnoreCase(slug)) {
      return rootCategory;
    }
    return rootCategory.getSubCategories().stream()
      .filter(category -> category.getId().equalsIgnoreCase(slug))
      .findFirst()
      .orElse(null);
  }
}
