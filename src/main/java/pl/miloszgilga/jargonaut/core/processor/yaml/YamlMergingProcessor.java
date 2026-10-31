package pl.miloszgilga.jargonaut.core.processor.yaml;

import pl.miloszgilga.jargonaut.core.file.FileUtils;
import pl.miloszgilga.jargonaut.core.parser.yaml.YamlParser;
import pl.miloszgilga.jargonaut.core.processor.MergeableMetadata;
import pl.miloszgilga.jargonaut.core.processor.ResourceCheckerProcessor;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class YamlMergingProcessor extends ResourceCheckerProcessor {
  private final YamlParser yamlParser;

  protected YamlMergingProcessor(Path resourcePath, YamlParser yamlParser) {
    super(resourcePath);
    this.yamlParser = yamlParser;
  }

  protected <T> Map<Locale, T> loadYamlTranslations(Path directory, Class<T> targetClass, Locale defaultLocale) {
    final Map<Locale, T> translations = new HashMap<>();
    final List<Path> yamlFiles = FileUtils.findFilesInDirectory(directory, ".yml", p -> true);
    for (final Path file : yamlFiles) {
      final String fileName = file.getFileName().toString();
      final String langStr = fileName.replace(".yml", "");
      Locale locale;
      if (langStr.equals("default") || langStr.equalsIgnoreCase(defaultLocale.toString())) {
        locale = defaultLocale;
      } else {
        locale = Locale.forLanguageTag(langStr.replace("_", "-"));
      }
      final boolean shouldValidate = locale.equals(defaultLocale);
      final T parsed = yamlParser.parseAndValidateYamlFile(file, targetClass, shouldValidate);
      if (parsed != null) {
        translations.put(locale, parsed);
      }
    }
    return translations;
  }

  protected <T extends MergeableMetadata<T>> void mergeYamlTranslations(Map<Locale, T> translations,
                                                                        Locale defaultLocale) {
    final T defaultMetadata = translations.get(defaultLocale);
    if (defaultMetadata == null) {
      return;
    }
    for (final Map.Entry<Locale, T> entry : translations.entrySet()) {
      final Locale locale = entry.getKey();
      if (!locale.equals(defaultLocale)) {
        final T mergedMetadata = defaultMetadata.merge(entry.getValue());
        yamlParser.validate(mergedMetadata);
        translations.put(locale, mergedMetadata);
      }
    }
  }
}
