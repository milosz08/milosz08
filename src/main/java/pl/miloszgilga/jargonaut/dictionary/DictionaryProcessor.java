package pl.miloszgilga.jargonaut.dictionary;

import pl.miloszgilga.jargonaut.core.CastUtils;
import pl.miloszgilga.jargonaut.core.parser.yaml.YamlParser;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.processor.yaml.YamlMergingProcessor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DictionaryProcessor extends YamlMergingProcessor {
  public DictionaryProcessor(Path baseDir, SiteBuildContext siteBuildContext, YamlParser yamlParser) {
    super(baseDir.resolve(siteBuildContext.getConfig().getDir().getDictionary()), yamlParser);
  }

  @Override
  protected void processAfterCheck(SiteBuildContext context) {
    final Locale locale = context.getConfig().getDefaultLocale();
    final Map<Locale, Map<String, Object>> globalI18n = CastUtils
      .unsafeCast(loadYamlTranslations(resourcePath, Map.class, locale));
    mergeDictionaryMaps(globalI18n, locale);
    context.getDictionaries().putAll(globalI18n);
    context.getActiveLocales().addAll(globalI18n.keySet());
  }

  @Override
  protected boolean isBaseResourceExists() {
    return Files.exists(resourcePath);
  }

  protected void mergeDictionaryMaps(Map<Locale, Map<String, Object>> translations, Locale defaultLocale) {
    final Map<String, Object> defaultMap = translations.get(defaultLocale);
    if (defaultMap == null) {
      return;
    }
    for (final Map.Entry<Locale, Map<String, Object>> entry : translations.entrySet()) {
      if (entry.getKey().equals(defaultLocale)) {
        continue;
      }
      final Map<String, Object> mergedMap = deepCopy(defaultMap);
      deepMerge(mergedMap, entry.getValue());
      translations.put(entry.getKey(), mergedMap);
    }
  }

  private Map<String, Object> deepCopy(Map<String, Object> original) {
    final Map<String, Object> copy = new HashMap<>();
    for (final Map.Entry<String, Object> entry : original.entrySet()) {
      if (entry.getValue() instanceof Map) {
        copy.put(entry.getKey(), deepCopy(CastUtils.unsafeCast(entry.getValue())));
      } else {
        copy.put(entry.getKey(), entry.getValue());
      }
    }
    return copy;
  }

  private void deepMerge(Map<String, Object> target, Map<String, Object> source) {
    for (final Map.Entry<String, Object> sourceEntry : source.entrySet()) {
      final String key = sourceEntry.getKey();
      final Object sourceValue = sourceEntry.getValue();
      final Object targetValue = target.get(key);
      if (sourceValue instanceof Map && targetValue instanceof Map) {
        deepMerge(CastUtils.unsafeCast(targetValue), CastUtils.unsafeCast(sourceValue));
      } else {
        target.put(key, sourceValue);
      }
    }
  }
}
