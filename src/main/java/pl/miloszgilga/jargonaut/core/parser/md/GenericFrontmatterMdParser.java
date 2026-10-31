package pl.miloszgilga.jargonaut.core.parser.md;

import pl.miloszgilga.jargonaut.core.GenerationHaltedException;
import pl.miloszgilga.jargonaut.core.event.BuildEvent;
import pl.miloszgilga.jargonaut.core.event.BuildEventBus;
import pl.miloszgilga.jargonaut.core.parser.DocumentParser;
import pl.miloszgilga.jargonaut.core.parser.ParsedDocument;
import pl.miloszgilga.jargonaut.core.parser.yaml.YamlParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public abstract class GenericFrontmatterMdParser implements DocumentParser {
  private final YamlParser yamlParser;

  protected GenericFrontmatterMdParser(BuildEventBus eventBus, YamlParser yamlParser) {
    eventBus.publish(BuildEvent.spinnerDetail("Registering frontmatter Markdown parser: " +
      getClass().getSimpleName()));
    this.yamlParser = yamlParser;
  }

  @Override
  public <T> ParsedDocument<T> parseMarkdown(Path mdFile, Class<T> metadataClass, Locale defaultLocale) {
    try {
      final String fileName = mdFile.getFileName().toString();
      Locale fileLocale = defaultLocale;
      if (fileName.startsWith("index-") && fileName.endsWith(".md")) {
        final String langStr = fileName.substring(6, fileName.length() - 3);
        fileLocale = Locale.forLanguageTag(langStr.replace("_", "-"));
      }
      final String content = Files.readString(mdFile);
      String yamlContent = "";
      String markdownContent = content;
      if (content.startsWith("---")) {
        int endIndex = content.indexOf("\n---", 3);
        if (endIndex != -1) {
          yamlContent = content.substring(3, endIndex).trim();
          markdownContent = content.substring(endIndex + 4).trim();
        }
      }
      T metadata = null;
      if (!yamlContent.isEmpty()) {
        metadata = yamlParser.parseAndValidateYamlString(yamlContent, metadataClass);
      }
      final String htmlContent = renderHtml(markdownContent);
      return new ParsedDocument<>(fileLocale, metadata, htmlContent);
    } catch (IOException ex) {
      throw new GenerationHaltedException(ex);
    }
  }

  protected abstract String renderHtml(String markdownContent);
}
