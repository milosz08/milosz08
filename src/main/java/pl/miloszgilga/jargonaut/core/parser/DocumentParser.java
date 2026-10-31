package pl.miloszgilga.jargonaut.core.parser;

import java.nio.file.Path;
import java.util.Locale;

public interface DocumentParser {
  <T> ParsedDocument<T> parseMarkdown(Path mdFile, Class<T> metadataClass, Locale defaultLocale);
}
