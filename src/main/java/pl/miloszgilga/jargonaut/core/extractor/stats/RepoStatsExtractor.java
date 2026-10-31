package pl.miloszgilga.jargonaut.core.extractor.stats;

import pl.miloszgilga.jargonaut.core.extractor.RepoExtractor;

import java.util.Map;
import java.util.Set;

public interface RepoStatsExtractor extends RepoExtractor {
  Map<String, RepoStats> extractRepoStats(Set<String> matchedRepoUrls);
}
