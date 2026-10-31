package pl.miloszgilga.jargonaut.page.home.generator;

import pl.miloszgilga.jargonaut.core.extractor.contributions.ContributionCalendarView;
import pl.miloszgilga.jargonaut.core.extractor.stats.LanguageStat;
import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.parser.ParsedDocument;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.template.FreemarkerTemplateName;
import pl.miloszgilga.jargonaut.core.template.TemplateName;
import pl.miloszgilga.jargonaut.core.template.generator.GeneratorContext;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlGenerator;
import pl.miloszgilga.jargonaut.core.template.generator.JsonLdMap;
import pl.miloszgilga.jargonaut.globals.model.GlobalsMetadata;
import pl.miloszgilga.jargonaut.page.education.dto.ResearchView;
import pl.miloszgilga.jargonaut.page.education.model.EducationSharedData;
import pl.miloszgilga.jargonaut.page.home.model.HomeMetadata;
import pl.miloszgilga.jargonaut.page.home.model.HomeSharedData;
import pl.miloszgilga.jargonaut.page.project.ProjectComparator;
import pl.miloszgilga.jargonaut.page.project.ProjectStatsUtils;
import pl.miloszgilga.jargonaut.page.project.dto.ProjectCategoriesTableView;
import pl.miloszgilga.jargonaut.page.project.dto.ProjectTableView;
import pl.miloszgilga.jargonaut.page.project.model.ProjectNodeMetadata;
import pl.miloszgilga.jargonaut.page.project.model.ProjectSharedData;
import pl.miloszgilga.jargonaut.page.tag.model.TagItem;
import pl.miloszgilga.jargonaut.page.tag.model.TagSharedData;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeHtmlGenerator extends HtmlGenerator<HomeMetadata> {
  public HomeHtmlGenerator(PageRepresentation<HomeMetadata> pageRepresentation,
                           GeneratorContext generatorContext) {
    super(pageRepresentation, generatorContext);
  }

  @Override
  protected Object generateJsonLdScript(String canonicalUrl,
                                        String language,
                                        GlobalsMetadata globals,
                                        ParsedDocument<HomeMetadata> document) {
    final JsonLdMap websiteSchema = createBaseSchema("WebSite", canonicalUrl, language, document);
    final JsonLdMap personSchema = createPersonSchema(canonicalUrl, globals)
      .add("@context", "https://schema.org");
    return List.of(websiteSchema, personSchema);
  }

  @Override
  protected TemplateName getHtmlTemplateName() {
    return FreemarkerTemplateName.HOME_PAGE;
  }

  @Override
  protected void insertPageAttributes(Map<String, Object> pageAttributes,
                                      Locale locale,
                                      ParsedDocument<HomeMetadata> document,
                                      SiteBuildContext context) {
    pageAttributes.put("pinnedProjects", createPinnedProjects(context, locale));
    pageAttributes.put("aggregatedLanguages", createLanguageStats(context, locale));
    pageAttributes.put("contributionCalendar", createContributionCalendarView(context, locale));
    pageAttributes.put("researchList", createResearchList(context, locale));
  }

  private List<ProjectTableView> createPinnedProjects(SiteBuildContext context, Locale locale) {
    final ProjectSharedData projectData = context.getSharedData(ProjectSharedData.class);
    final TagSharedData tagsData = context.getSharedData(TagSharedData.class);
    if (projectData == null || tagsData == null) {
      return Collections.emptyList();
    }
    final List<TagItem> tags = tagsData.getTagItems().get(locale);
    final List<PageRepresentation<ProjectNodeMetadata>> allProjects = projectData.getAllProjects();
    return allProjects.stream()
      .filter(project -> {
        ParsedDocument<ProjectNodeMetadata> translations = project.translations().get(locale);
        if (translations == null && !project.translations().isEmpty()) {
          translations = project.translations().values().iterator().next();
        }
        return translations != null && Boolean.TRUE.equals(translations.metadata().getPinned());
      })
      .sorted(new ProjectComparator(locale, context.getConfig()))
      .map(project -> {
        ParsedDocument<ProjectNodeMetadata> trans = project.translations().get(locale);
        if (trans == null) {
          trans = project.translations().values().iterator().next();
        }
        final boolean recentlyUpdated = ProjectStatsUtils.isRecentlyUpdated(projectData, project.id(), locale,
          context.getConfig().getProject().getRecentlyUpdatedDeltaDays());
        return new ProjectTableView(project.id(), trans.metadata(), tags, recentlyUpdated);
      })
      .toList();
  }

  private List<LanguageStat> createLanguageStats(SiteBuildContext context, Locale locale) {
    final List<ProjectTableView> pinnedProjects = createPinnedProjects(context, locale);
    return ProjectCategoriesTableView.createLanguageStats(context, locale, pinnedProjects);
  }

  private ContributionCalendarView createContributionCalendarView(SiteBuildContext context, Locale locale) {
    final HomeSharedData homeData = context.getSharedData(HomeSharedData.class);
    if (homeData == null || homeData.getContributionCalendars() == null) {
      return null;
    }
    return new ContributionCalendarView(homeData.getContributionCalendars(), locale);
  }

  private List<ResearchView> createResearchList(SiteBuildContext context, Locale locale) {
    final EducationSharedData educationData = context.getSharedData(EducationSharedData.class);
    if (educationData == null || educationData.getResearchItems().get(locale) == null) {
      return Collections.emptyList();
    }
    return educationData.getResearchItems().get(locale).stream().map(ResearchView::new).toList();
  }
}
