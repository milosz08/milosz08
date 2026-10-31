package pl.miloszgilga.jargonaut.core.parser;

import pl.miloszgilga.jargonaut.core.CastUtils;
import pl.miloszgilga.jargonaut.core.GenerationHaltedException;
import pl.miloszgilga.jargonaut.core.parser.yaml.YamlParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FrontmatterInterpolator {
  private static final Pattern VAR_PATTERN = Pattern.compile("\\$\\{([^}]+)}");

  private final YamlParser yamlParser;

  public FrontmatterInterpolator(YamlParser yamlParser) {
    this.yamlParser = yamlParser;
  }

  public <T> T interpolateMergedObject(T mergedObject,
                                       Class<T> targetClass,
                                       Object globals,
                                       Object config,
                                       Map<String, Object> dynamicContext) {
    try {
      final Map<String, Object> objectMap = CastUtils.unsafeCast(yamlParser.convertToMap(mergedObject));
      if (objectMap == null) {
        return mergedObject;
      }
      final Map<String, Object> lookupContext = new HashMap<>(flattenMap(objectMap, ""));
      insertInterpolatedData(globals, config, dynamicContext, lookupContext);
      resolveNode(objectMap, lookupContext);
      return yamlParser.convertFromMap(objectMap, targetClass);
    } catch (Exception ex) {
      throw new GenerationHaltedException(ex);
    }
  }

  public String interpolateMarkdown(String markdownContent,
                                    Object globals,
                                    Object config,
                                    Map<String, Object> dynamicContext) {
    if (markdownContent == null || markdownContent.isBlank()) {
      return markdownContent;
    }
    try {
      final Map<String, Object> lookupContext = new HashMap<>();
      insertInterpolatedData(globals, config, dynamicContext, lookupContext);
      return resolveString(markdownContent, lookupContext);
    } catch (Exception ex) {
      throw new GenerationHaltedException(ex);
    }
  }

  private void insertInterpolatedData(Object globals,
                                      Object config,
                                      Map<String, Object> dynamicContext,
                                      Map<String, Object> lookupContext) {
    if (dynamicContext != null) {
      lookupContext.putAll(dynamicContext);
    }
    if (globals != null) {
      lookupContext.putAll(flattenMap(yamlParser.convertToMap(globals), "globals."));
    }
    if (config != null) {
      lookupContext.putAll(flattenMap(yamlParser.convertToMap(config), "config."));
    }
  }

  private static Object resolveNode(Object node, Map<String, Object> lookupContext) {
    return switch (node) {
      case String str -> resolveString(str, lookupContext);
      case Map<?, ?> m -> {
        final Map<String, Object> map = CastUtils.unsafeCast(m);
        map.replaceAll((k, v) -> resolveNode(v, lookupContext));
        yield map;
      }
      case List<?> l -> {
        final List<Object> list = CastUtils.unsafeCast(l);
        list.replaceAll(item -> resolveNode(item, lookupContext));
        yield list;
      }
      case null, default -> node;
    };
  }

  private static String resolveString(String value, Map<String, Object> lookupContext) {
    final Matcher matcher = VAR_PATTERN.matcher(value);
    final StringBuilder sb = new StringBuilder();
    boolean found = false;
    while (matcher.find()) {
      found = true;
      final Object resolvedVal = lookupContext.get(matcher.group(1));
      final String replacement = resolvedVal != null ? String.valueOf(resolvedVal) : matcher.group(0);
      matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
    }
    matcher.appendTail(sb);
    final String result = sb.toString();
    return (found && !result.equals(value)) ? resolveString(result, lookupContext) : result;
  }

  private static Map<String, Object> flattenMap(Map<String, Object> map, String prefix) {
    final Map<String, Object> flat = new HashMap<>();
    if (map == null) {
      return flat;
    }
    map.forEach((k, v) -> {
      final String key = prefix + k;
      if (v instanceof Map<?, ?> nested) {
        flat.putAll(flattenMap(CastUtils.unsafeCast(nested), key + "."));
      } else {
        flat.put(key, v);
      }
    });
    return flat;
  }
}
