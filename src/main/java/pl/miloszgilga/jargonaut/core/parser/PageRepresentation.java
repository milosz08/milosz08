package pl.miloszgilga.jargonaut.core.parser;

import pl.miloszgilga.jargonaut.core.template.generator.HtmlGeneratorFactory;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public record PageRepresentation<T extends GenericPage>(
  String id,
  Path baseDir,
  Map<Locale, ParsedDocument<T>> translations,
  Map<String, List<Path>> assetsFiles,
  HtmlGeneratorFactory<T> generatorFactory
) {
}
