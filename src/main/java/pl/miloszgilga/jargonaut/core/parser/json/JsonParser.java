package pl.miloszgilga.jargonaut.core.parser.json;

public interface JsonParser {
  String writeValueAsString(Object schema);

  <T> T readValue(String content, Class<T> valueType);
}
