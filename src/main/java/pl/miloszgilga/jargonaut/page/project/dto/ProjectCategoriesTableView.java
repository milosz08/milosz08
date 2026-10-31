package pl.miloszgilga.jargonaut.page.project.dto;

import pl.miloszgilga.jargonaut.config.model.Config;
import pl.miloszgilga.jargonaut.core.extractor.stats.LanguageStat;
import pl.miloszgilga.jargonaut.core.extractor.stats.RepoStats;
import pl.miloszgilga.jargonaut.core.parser.ParsedDocument;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.page.project.ProjectComparator;
import pl.miloszgilga.jargonaut.page.project.ProjectStatsUtils;
import pl.miloszgilga.jargonaut.page.project.model.Category;
import pl.miloszgilga.jargonaut.page.project.model.ProjectNodeMetadata;
import pl.miloszgilga.jargonaut.page.project.model.ProjectSharedData;
import pl.miloszgilga.jargonaut.page.tag.model.TagItem;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public record ProjectCategoriesTableView(
  String name,
  List<ProjectTableView> projects,
  List<LanguageStat> categoryLanguages
) {
  public ProjectCategoriesTableView(Category category,
                                    Locale locale,
                                    List<TagItem> tags,
                                    Config config,
                                    ProjectSharedData projectData) {
    this(
      category.getLocalizedNames().get(locale),
      category.getProjects().stream()
        .sorted(new ProjectComparator(locale, config))
        .map(project -> {
          final ParsedDocument<ProjectNodeMetadata> document = project.translations().get(locale);
          if (document == null) {
            return null;
          }
          final boolean recentlyUpdated = ProjectStatsUtils.isRecentlyUpdated(projectData, project.id(), locale,
            config.getProject().getRecentlyUpdatedDeltaDays());
          return new ProjectTableView(project.id(), document.metadata(), tags, recentlyUpdated);
        })
        .filter(Objects::nonNull)
        .toList(),
      LanguageStat.aggregate(category.getProjects().stream()
        .map(project -> getProjectLanguages(projectData, project.id(), locale))
        .toList())
    );
  }

  public ProjectCategoriesTableView(Category category,
                                    List<ProjectTableView> projects,
                                    SiteBuildContext context,
                                    Locale locale) {
    this(
      category.getLocalizedNames().get(locale),
      projects,
      createLanguageStats(context, locale, projects)
    );
  }

  public static List<LanguageStat> createLanguageStats(SiteBuildContext context,
                                                       Locale locale,
                                                       List<ProjectTableView> projects) {
    if (projects.isEmpty()) {
      return Collections.emptyList();
    }
    final ProjectSharedData projectData = context.getSharedData(ProjectSharedData.class);
    if (projectData == null) {
      return Collections.emptyList();
    }
    final List<List<LanguageStat>> statsToAggregate = projects.stream()
      .map(project -> getProjectLanguages(projectData, project.id(), locale))
      .toList();
    return LanguageStat.aggregate(statsToAggregate);
  }

  private static List<LanguageStat> getProjectLanguages(ProjectSharedData projectData,
                                                        String projectId,
                                                        Locale locale) {
    if (projectData == null || projectData.getProjectRepoStats() == null) {
      return Collections.emptyList();
    }
    final Map<Locale, RepoStats> statsByLocale = projectData.getProjectRepoStats().get(projectId);
    if (statsByLocale == null || !statsByLocale.containsKey(locale)) {
      return Collections.emptyList();
    }
    final RepoStats repoStats = statsByLocale.get(locale);
    return repoStats.languages() != null ? repoStats.languages() : Collections.emptyList();
  }
}
