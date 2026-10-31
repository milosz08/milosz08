package pl.miloszgilga.jargonaut.core.graphql.adapter;

import pl.miloszgilga.jargonaut.core.graphql.GraphQlTemplate;

public enum BasicGraphQlTemplate implements GraphQlTemplate {
  GITHUB_REPO_STATS("graphql/github-repo-stats.graphql"),
  GITHUB_USER_CALENDAR("graphql/github-user-calendar.graphql"),
  ;

  private final String templatePath;

  BasicGraphQlTemplate(String templatePath) {
    this.templatePath = templatePath;
  }

  @Override
  public String getTemplatePath() {
    return templatePath;
  }
}
