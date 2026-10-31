package pl.miloszgilga.jargonaut.core.extractor;

import pl.miloszgilga.jargonaut.core.event.BuildEvent;
import pl.miloszgilga.jargonaut.core.event.BuildEventBus;
import pl.miloszgilga.jargonaut.core.graphql.GraphQlClient;
import pl.miloszgilga.jargonaut.core.graphql.adapter.BasicGrapQlClient;
import pl.miloszgilga.jargonaut.core.parser.json.JsonParser;

import java.util.regex.Pattern;

public abstract class GenericGithubDataExtractor implements RepoExtractor {
  protected static final String GRAPHQL_API_URL = "https://api.github.com/graphql";

  protected final GraphQlClient graphQlClient;

  protected GenericGithubDataExtractor(BuildEventBus eventBus, GithubExtractorConfig config, JsonParser jsonParser) {
    eventBus.publish(BuildEvent.spinnerDetail("Registering Github data extractor: " + getClass().getSimpleName()));
    graphQlClient = new BasicGrapQlClient(GRAPHQL_API_URL, config.getToken(), jsonParser);
  }

  @Override
  public boolean isUrlMatchPattern(String repoUrl) {
    if (repoUrl == null) {
      return false;
    }
    return getUrlPattern().matcher(repoUrl).matches();
  }

  protected abstract Pattern getUrlPattern();
}
