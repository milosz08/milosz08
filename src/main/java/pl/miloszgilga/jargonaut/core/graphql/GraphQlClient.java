package pl.miloszgilga.jargonaut.core.graphql;

import java.util.Map;

public interface GraphQlClient {
  <T> T executeFromTemplate(GraphQlTemplate template,
                            Map<String, String> templatePlaceholders,
                            Map<String, Object> variables,
                            Class<T> responseType);

  <T> T execute(String query, Map<String, Object> variables, Class<T> responseType);
}
