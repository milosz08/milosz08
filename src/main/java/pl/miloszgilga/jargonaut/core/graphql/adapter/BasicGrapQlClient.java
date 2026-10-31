package pl.miloszgilga.jargonaut.core.graphql.adapter;

import pl.miloszgilga.jargonaut.core.GenerationHaltedException;
import pl.miloszgilga.jargonaut.core.graphql.GraphQlClient;
import pl.miloszgilga.jargonaut.core.graphql.GraphQlTemplate;
import pl.miloszgilga.jargonaut.core.parser.json.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BasicGrapQlClient implements GraphQlClient {
  private final String endpointUrl;
  private final String authToken;
  private final JsonParser jsonParser;
  private final HttpClient httpClient;
  private final Map<String, String> templateCache = new ConcurrentHashMap<>();

  public BasicGrapQlClient(String endpointUrl, String authToken, JsonParser jsonParser) {
    this.endpointUrl = endpointUrl;
    this.authToken = authToken;
    this.jsonParser = jsonParser;
    httpClient = HttpClient.newBuilder()
      .connectTimeout(Duration.ofSeconds(10))
      .build();
  }

  @Override
  public <T> T executeFromTemplate(GraphQlTemplate template,
                                   Map<String, String> templatePlaceholders,
                                   Map<String, Object> variables,
                                   Class<T> responseType) {
    String query = loadTemplate(template.getTemplatePath());
    if (templatePlaceholders != null) {
      for (final Map.Entry<String, String> entry : templatePlaceholders.entrySet()) {
        query = query.replace("{{" + entry.getKey() + "}}", entry.getValue());
      }
    }
    return execute(query, variables, responseType);
  }

  @Override
  public <T> T execute(String query, Map<String, Object> variables, Class<T> responseType) {
    try {
      final Map<String, Object> payload = new HashMap<>();
      payload.put("query", query);
      if (variables != null && !variables.isEmpty()) {
        payload.put("variables", variables);
      }
      final String jsonPayload = jsonParser.writeValueAsString(payload);
      final HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(endpointUrl))
        .header("Authorization", "Bearer " + authToken)
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
        .build();
      final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() != 200) {
        throw new RuntimeException("GraphQL API returned status " + response.statusCode() + ": " + response.body());
      }
      return jsonParser.readValue(response.body(), responseType);
    } catch (Exception ex) {
      throw new GenerationHaltedException(ex);
    }
  }

  private String loadTemplate(String resourcePath) {
    return templateCache.computeIfAbsent(resourcePath, path -> {
      try (final InputStream inputStream = getClass().getResourceAsStream(path.startsWith("/") ? path : "/" + path)) {
        if (inputStream == null) {
          throw new IllegalStateException("GraphQL template not found at: " + path);
        }
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
      } catch (IOException ex) {
        throw new GenerationHaltedException(ex);
      }
    });
  }
}
