package pl.miloszgilga.jargonaut.core.parser.json;

import pl.miloszgilga.jargonaut.core.GenerationHaltedException;

import pl.miloszgilga.jargonaut.core.event.BuildEvent;
import pl.miloszgilga.jargonaut.core.event.BuildEventBus;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

public class JacksonJsonParserAdapter implements JsonParser {
  private final ObjectMapper objectMapper;

  public JacksonJsonParserAdapter(BuildEventBus eventBus) {
    eventBus.publish(BuildEvent.spinnerDetail("Registering JSON parser: " + getClass().getSimpleName()));
    objectMapper = new ObjectMapper();
  }

  @Override
  public String writeValueAsString(Object schema) {
    try {
      return objectMapper.writeValueAsString(schema);
    } catch (JacksonException ex) {
      throw new GenerationHaltedException(ex);
    }
  }

  @Override
  public <T> T readValue(String content, Class<T> valueType) {
    try {
      return objectMapper.readValue(content, valueType);
    } catch (JacksonException ex) {
      throw new GenerationHaltedException(ex);
    }
  }
}
