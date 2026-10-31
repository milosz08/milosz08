package pl.miloszgilga.jargonaut.core.parser.yaml;

import java.nio.file.Path;
import java.util.Map;

public interface YamlParser {
  <T> T parseAndValidateYamlString(String yamlContent, Class<T> targetClass);

  <T> T parseAndValidateYamlFile(Path yamlFile, Class<T> targetClass, boolean validate);

  void validate(Object obj);

  Map<String, Object> convertToMap(Object obj);

  <T> T convertFromMap(Map<String, Object> map, Class<T> targetClass);
}
