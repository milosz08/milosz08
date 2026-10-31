package pl.miloszgilga.jargonaut.globals;

import pl.miloszgilga.jargonaut.core.parser.yaml.YamlParser;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.processor.yaml.YamlMergingProcessor;
import pl.miloszgilga.jargonaut.globals.model.GlobalsMetadata;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;

public class GlobalsProcessor extends YamlMergingProcessor {
  public GlobalsProcessor(Path baseDir, SiteBuildContext siteBuildContext, YamlParser yamlParser) {
    super(baseDir.resolve(siteBuildContext.getConfig().getDir().getGlobals()), yamlParser);
  }

  @Override
  protected void processAfterCheck(SiteBuildContext context) {
    final Locale locale = context.getConfig().getDefaultLocale();
    final Map<Locale, GlobalsMetadata> translations = loadYamlTranslations(resourcePath, GlobalsMetadata.class, locale);
    mergeYamlTranslations(translations, locale);
    context.getGlobals().putAll(translations);
  }

  @Override
  protected boolean isBaseResourceExists() {
    return Files.exists(resourcePath);
  }
}
