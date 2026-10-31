package pl.miloszgilga.jargonaut.page.tag.generator;

import pl.miloszgilga.jargonaut.core.extractor.contributions.ContributionCalendarView;
import pl.miloszgilga.jargonaut.core.extractor.stats.RepoStats;
import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.parser.ParsedDocument;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.template.FreemarkerTemplateName;
import pl.miloszgilga.jargonaut.core.template.TemplateName;
import pl.miloszgilga.jargonaut.core.template.generator.DynamicHtmlGenerator;
import pl.miloszgilga.jargonaut.core.template.generator.GeneratorContext;
import pl.miloszgilga.jargonaut.globals.model.GlobalsMetadata;
import pl.miloszgilga.jargonaut.page.project.CategoryComparator;
import pl.miloszgilga.jargonaut.page.project.ProjectComparator;
import pl.miloszgilga.jargonaut.page.project.ProjectStatsUtils;
import pl.miloszgilga.jargonaut.page.project.dto.ProjectCategoriesTableView;
import pl.miloszgilga.jargonaut.page.project.dto.ProjectTableView;
import pl.miloszgilga.jargonaut.page.project.model.Category;
import pl.miloszgilga.jargonaut.page.project.model.ProjectSharedData;
import pl.miloszgilga.jargonaut.page.tag.model.TagItem;
import pl.miloszgilga.jargonaut.page.tag.model.TagNodeMetadata;
import pl.miloszgilga.jargonaut.page.tag.model.TagSharedData;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class TagNodeHtmlGenerator extends DynamicHtmlGenerator<TagNodeMetadata> {
  public TagNodeHtmlGenerator(PageRepresentation<TagNodeMetadata> pageRepresentation,
                              GeneratorContext generatorContext,
                              String slug) {
    super(pageRepresentation, generatorContext, slug);
  }

  @Override
  protected Object generateJsonLdScript(String canonicalUrl,
                                        String language,
                                        GlobalsMetadata globals,
                                        ParsedDocument<TagNodeMetadata> document) {
    return createBaseSchema("CollectionPage", canonicalUrl, language, document)
      .add("author", createPersonSchema(canonicalUrl, globals));
  }

  @Override
  protected TemplateName getHtmlTemplateName() {
    return FreemarkerTemplateName.TAG_NODE_PAGE;
  }

  @Override
  protected void insertPageAttributes(Map<String, Object> pageAttributes,
                                      Locale locale,
                                      ParsedDocument<TagNodeMetadata> document,
                                      SiteBuildContext context) {
    pageAttributes.put("projectCategories", createProjectCategoriesForTag(context, locale));
    pageAttributes.put("aggregatedCalendar", createAggregatedCalendarView(context, locale));
  }

  private List<ProjectCategoriesTableView> createProjectCategoriesForTag(SiteBuildContext context, Locale locale) {
    final ProjectSharedData projectData = context.getSharedData(ProjectSharedData.class);
    final TagSharedData tagData = context.getSharedData(TagSharedData.class);
    if (projectData == null || tagData == null) {
      return Collections.emptyList();
    }
    final Category root = projectData.getRootProjectCategory();
    if (root == null) {
      return List.of();
    }
    final List<TagItem> allTags = tagData.getTagItems().get(locale);
    final TagItem currentTag = allTags.stream()
      .filter(tag -> tag.slug().equalsIgnoreCase(slug))
      .findFirst()
      .orElse(null);
    if (currentTag == null) {
      return List.of();
    }
    return Stream.concat(
        root.getSubCategories().stream(),
        root.getProjects().isEmpty() ? Stream.empty() : Stream.of(root)
      )
      .sorted(new CategoryComparator())
      .map(category -> mapToProjectsCategory(category, locale, currentTag, allTags, context, projectData))
      .filter(Objects::nonNull)
      .toList();
  }

  private ContributionCalendarView createAggregatedCalendarView(SiteBuildContext context, Locale locale) {
    final RepoStats aggregatedStats = aggregateTagStats(context, locale, getProjectsTableView(context, locale));
    if (aggregatedStats == null) {
      return null;
    }
    return new ContributionCalendarView(aggregatedStats.contributionCalendar(), locale);
  }

  private List<ProjectTableView> getProjectsTableView(SiteBuildContext context, Locale locale) {
    final List<ProjectCategoriesTableView> projectsWithCategories = createProjectCategoriesForTag(context, locale);
    return projectsWithCategories.stream()
      .flatMap(category -> category.projects().stream())
      .toList();
  }

  private RepoStats aggregateTagStats(SiteBuildContext context, Locale locale, List<ProjectTableView> projects) {
    final ProjectSharedData projectData = context.getSharedData(ProjectSharedData.class);
    if (projectData == null) {
      return null;
    }
    final List<RepoStats> statsList = projects.stream()
      .map(project -> ProjectStatsUtils.getRepoStats(projectData, project.id(), locale))
      .filter(Objects::nonNull)
      .toList();
    return RepoStats.aggregate(statsList);
  }

  private ProjectCategoriesTableView mapToProjectsCategory(Category category,
                                                           Locale locale,
                                                           TagItem tag,
                                                           List<TagItem> tags,
                                                           SiteBuildContext context,
                                                           ProjectSharedData projectData) {
    final List<ProjectTableView> projects = category.getProjects().stream()
      .filter(project -> project.translations().get(locale).metadata().getTech().contains(tag.name()))
      .sorted(new ProjectComparator(locale, context.getConfig()))
      .map(project -> {
        final boolean recentlyUpdated = ProjectStatsUtils.isRecentlyUpdated(projectData, project.id(), locale,
          context.getConfig().getProject().getRecentlyUpdatedDeltaDays());
        return new ProjectTableView(project.id(), project.translations().get(locale).metadata(), tags, recentlyUpdated);
      })
      .toList();
    if (projects.isEmpty()) {
      return null;
    }
    return new ProjectCategoriesTableView(category, projects, context, locale);
  }
}
