package pl.miloszgilga.jargonaut.page.project;

import pl.miloszgilga.jargonaut.core.extractor.stats.RepoStats;
import pl.miloszgilga.jargonaut.page.project.model.ProjectSharedData;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Map;

public class ProjectStatsUtils {
  private ProjectStatsUtils() {
  }

  public static RepoStats getRepoStats(ProjectSharedData projectData, String projectId, Locale locale) {
    if (projectData == null || projectData.getProjectRepoStats() == null) {
      return null;
    }
    final Map<Locale, RepoStats> localeStats = projectData.getProjectRepoStats().get(projectId);
    if (localeStats != null && localeStats.containsKey(locale)) {
      return localeStats.get(locale);
    }
    return null;
  }

  public static boolean isRecentlyUpdated(RepoStats stats, int daysThreshold) {
    if (stats == null || stats.lastCommitDate() == null) {
      return false;
    }
    final Instant thresholdDate = Instant.now().minus(daysThreshold, ChronoUnit.DAYS);
    return stats.lastCommitDate().isAfter(thresholdDate);
  }

  public static boolean isRecentlyUpdated(ProjectSharedData projectData,
                                          String projectId,
                                          Locale locale,
                                          int daysThreshold) {
    final RepoStats stats = getRepoStats(projectData, projectId, locale);
    return isRecentlyUpdated(stats, daysThreshold);
  }
}
