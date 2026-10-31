package pl.miloszgilga.jargonaut.core.parser.yaml;

import pl.miloszgilga.jargonaut.core.CastUtils;
import pl.miloszgilga.jargonaut.core.GenerationHaltedException;

import pl.miloszgilga.jargonaut.core.event.BuildEvent;
import pl.miloszgilga.jargonaut.core.event.BuildEventBus;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.nio.file.Path;
import java.util.Map;

public class JacksonYamlParser extends GenericYamlParser {
  private final YAMLMapper yamlMapper;

  public JacksonYamlParser(BuildEventBus eventBus) {
    eventBus.publish(BuildEvent.spinnerDetail("Registering YAML parser: " + getClass().getSimpleName()));
    yamlMapper = YAMLMapper.builder()
      .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      .enable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES)
      .enable(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES)
      .build();
  }

  @Override
  protected <T> T parseYamlString(String yamlContent, Class<T> targetClass) {
    try {
      return yamlMapper.readValue(yamlContent, targetClass);
    } catch (JacksonException ex) {
      throw new GenerationHaltedException(ex);
    }
  }

  @Override
  protected <T> T parseYamlFile(Path yamlFile, Class<T> targetClass) {
    try {
      return yamlMapper.readValue(yamlFile.toFile(), targetClass);
    } catch (JacksonException ex) {
      throw new GenerationHaltedException(ex);
    }
  }

  @Override
  protected Map<String, Object> convertObjToMap(Object obj) {
    try {
      return CastUtils.unsafeCast(yamlMapper.convertValue(obj, Map.class));
    } catch (IllegalArgumentException ex) {
      throw new GenerationHaltedException(ex);
    }
  }

  @Override
  protected <T> T convertMapToObj(Map<String, Object> map, Class<T> targetClass) {
    try {
      return yamlMapper.convertValue(map, targetClass);
    } catch (IllegalArgumentException ex) {
      throw new GenerationHaltedException(ex);
    }
  }
}
