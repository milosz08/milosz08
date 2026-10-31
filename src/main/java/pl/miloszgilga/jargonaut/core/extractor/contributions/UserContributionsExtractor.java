package pl.miloszgilga.jargonaut.core.extractor.contributions;

import pl.miloszgilga.jargonaut.core.extractor.RepoExtractor;

public interface UserContributionsExtractor extends RepoExtractor {
  ContributionCalendar extractUserCalendar(String principalId);

  String extractPrincipalId(String url);
}
