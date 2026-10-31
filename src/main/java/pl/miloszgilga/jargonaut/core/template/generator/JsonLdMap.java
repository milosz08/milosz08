package pl.miloszgilga.jargonaut.core.template.generator;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class JsonLdMap extends HashMap<String, Object> {
  public JsonLdMap add(String key, Object value) {
    switch (value) {
      case null -> {
        return this;
      }
      case String str when str.isBlank() -> {
        return this;
      }
      case Collection<?> col when col.isEmpty() -> {
        return this;
      }
      default -> {
      }
    }
    put(key, value);
    return this;
  }

  public <T, R> JsonLdMap addList(String key, List<T> values, Function<T, R> mapper) {
    if (values == null || values.isEmpty()) {
      return this;
    }
    final List<R> mappedValues = values.stream()
      .map(mapper)
      .toList();
    put(key, mappedValues);
    return this;
  }
}
