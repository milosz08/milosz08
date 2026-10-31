package pl.miloszgilga.jargonaut.page.project.processor;

import pl.miloszgilga.jargonaut.core.extractor.stats.RepoStats;
import pl.miloszgilga.jargonaut.core.extractor.stats.RepoStatsExtractor;
import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.parser.ParsedDocument;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.processor.TaskProcessor;
import pl.miloszgilga.jargonaut.page.project.model.Link;
import pl.miloszgilga.jargonaut.page.project.model.ProjectNodeMetadata;
import pl.miloszgilga.jargonaut.page.project.model.ProjectSharedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ProjectRepoProcessor implements TaskProcessor {
  private final List<RepoStatsExtractor> extractors;

  public ProjectRepoProcessor(List<RepoStatsExtractor> extractors) {
    this.extractors = extractors;
  }

  @Override
  public void process(SiteBuildContext context) {
    final ProjectSharedData projectData = context.getSharedData(ProjectSharedData.class);
    if (projectData == null || projectData.getAllProjects().isEmpty()) {
      return;
    }
    final Set<String> uniqueRepoUrls = new HashSet<>();
    final Map<String, Map<Locale, List<Link>>> allProjectRepoUrls =
      collectProjectRepoUrls(projectData.getAllProjects(), uniqueRepoUrls);
    if (uniqueRepoUrls.isEmpty()) {
      return;
    }
    final Map<String, RepoStats> extractedStatsByUrl = fetchStatsFromExtractors(uniqueRepoUrls);
    aggregateAndSaveProjectStats(projectData, allProjectRepoUrls, extractedStatsByUrl);
  }

  private Map<String, Map<Locale, List<Link>>> collectProjectRepoUrls(
    List<PageRepresentation<ProjectNodeMetadata>> allProjects,
    Set<String> uniqueRepoUrlsOut
  ) {
    final Map<String, Map<Locale, List<Link>>> allProjectRepoUrls = new HashMap<>();
    for (final PageRepresentation<ProjectNodeMetadata> project : allProjects) {
      final Map<Locale, List<Link>> projectRepoUrls = new HashMap<>();
      for (final Map.Entry<Locale, ParsedDocument<ProjectNodeMetadata>> entry : project.translations().entrySet()) {
        final Locale locale = entry.getKey();
        final ParsedDocument<ProjectNodeMetadata> document = entry.getValue();
        final List<Link> repoUrls = new ArrayList<>();
        if (document.metadata().getRepoUrls() != null) {
          for (final Link link : document.metadata().getRepoUrls()) {
            repoUrls.add(link);
            if (link.getUrl() != null && !link.getUrl().isBlank()) {
              uniqueRepoUrlsOut.add(link.getUrl());
            }
          }
        }
        projectRepoUrls.put(locale, repoUrls);
      }
      allProjectRepoUrls.put(project.id(), projectRepoUrls);
    }
    return allProjectRepoUrls;
  }

  private Map<String, RepoStats> fetchStatsFromExtractors(Set<String> uniqueRepoUrls) {
    final Map<String, RepoStats> extractedStatsByUrl = new HashMap<>();
    final Set<String> urlsToProcess = new HashSet<>(uniqueRepoUrls);
    for (final RepoStatsExtractor extractor : extractors) {
      final Set<String> matchingUrls = new HashSet<>();
      for (final String url : urlsToProcess) {
        if (extractor.isUrlMatchPattern(url)) {
          matchingUrls.add(url);
        }
      }
      if (!matchingUrls.isEmpty()) {
        final Map<String, RepoStats> extractorResults = extractor.extractRepoStats(matchingUrls);
        if (extractorResults != null) {
          extractedStatsByUrl.putAll(extractorResults);
        }
        urlsToProcess.removeAll(matchingUrls);
      }
    }
    return extractedStatsByUrl;
  }

  private void aggregateAndSaveProjectStats(ProjectSharedData projectData,
                                            Map<String, Map<Locale, List<Link>>> allProjectRepoUrls,
                                            Map<String, RepoStats> extractedStatsByUrl) {
    for (final PageRepresentation<ProjectNodeMetadata> project : projectData.getAllProjects()) {
      final Map<Locale, List<Link>> localeLinks = allProjectRepoUrls.get(project.id());
      if (localeLinks == null) {
        continue;
      }
      final Map<Locale, RepoStats> statsByLocale = new HashMap<>();
      for (final Map.Entry<Locale, List<Link>> entry : localeLinks.entrySet()) {
        final Locale locale = entry.getKey();
        final List<RepoStats> projectReposStats = new ArrayList<>();
        for (final Link link : entry.getValue()) {
          if (link.getUrl() == null) {
            continue;
          }
          final RepoStats repoStats = extractedStatsByUrl.get(link.getUrl());
          if (repoStats != null) {
            projectReposStats.add(repoStats);
          }
        }
        if (!projectReposStats.isEmpty()) {
          final RepoStats aggregatedStats = RepoStats.aggregate(projectReposStats);
          statsByLocale.put(locale, aggregatedStats);
        }
      }
      if (!statsByLocale.isEmpty()) {
        projectData.getProjectRepoStats().put(project.id(), statsByLocale);
      }
    }
  }
}
