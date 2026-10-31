package pl.miloszgilga.jargonaut.core.parser.yaml;

import pl.miloszgilga.jargonaut.core.GenerationHaltedException;
import pl.miloszgilga.jargonaut.core.parser.validation.YamlNested;
import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public abstract class GenericYamlParser implements YamlParser {
  @Override
  public <T> T parseAndValidateYamlString(String yamlContent, Class<T> targetClass) {
    if (yamlContent == null || yamlContent.isBlank()) {
      return null;
    }
    return parseYamlString(yamlContent, targetClass);
  }

  @Override
  public <T> T parseAndValidateYamlFile(Path yamlFile, Class<T> targetClass, boolean validate) {
    if (!Files.exists(yamlFile)) {
      return null;
    }
    final T parsedYaml = parseYamlFile(yamlFile, targetClass);
    if (validate) {
      validate(parsedYaml);
    }
    return parsedYaml;
  }

  @Override
  public Map<String, Object> convertToMap(Object obj) {
    if (obj == null) {
      return null;
    }
    return convertObjToMap(obj);
  }

  @Override
  public <T> T convertFromMap(Map<String, Object> map, Class<T> targetClass) {
    if (map == null) {
      return null;
    }
    return convertMapToObj(map, targetClass);
  }

  @Override
  public void validate(Object obj) {
    if (obj == null) {
      return;
    }
    if (!obj.getClass().getName().startsWith("pl.miloszgilga")) {
      return;
    }
    for (final Field field : obj.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      try {
        final Object fieldValue = field.get(obj);
        if (field.isAnnotationPresent(YamlRequired.class)) {
          if (fieldValue == null) {
            throw new GenerationHaltedException(
              String.format("Field '%s' in class '%s' is required and cannot be null",
                field.getName(), obj.getClass().getSimpleName())
            );
          }
        }
        if (field.isAnnotationPresent(YamlNested.class) && fieldValue != null) {
          if (fieldValue instanceof Iterable<?> iterable) {
            for (final Object item : iterable) {
              validate(item);
            }
          } else {
            validate(fieldValue);
          }
        }
      } catch (IllegalAccessException ex) {
        throw new GenerationHaltedException(ex);
      }
    }
  }

  protected abstract <T> T parseYamlString(String yamlContent, Class<T> targetClass);

  protected abstract <T> T parseYamlFile(Path yamlFile, Class<T> targetClass);

  protected abstract Map<String, Object> convertObjToMap(Object obj);

  protected abstract <T> T convertMapToObj(Map<String, Object> map, Class<T> targetClass);
}
