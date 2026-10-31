package pl.miloszgilga.jargonaut.core.parser.md;

import com.vladsch.flexmark.ext.gitlab.GitLabExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

import pl.miloszgilga.jargonaut.core.event.BuildEventBus;
import pl.miloszgilga.jargonaut.core.parser.yaml.YamlParser;
import pl.miloszgilga.jargonaut.core.polyglot.MathRenderer;
import pl.miloszgilga.jargonaut.core.polyglot.SyntaxHighlighter;

import java.util.List;

public class FlexmarkFrontmatterMdParser extends GenericFrontmatterMdParser {
  private final Parser mdParser;
  private final HtmlRenderer htmlRenderer;

  public FlexmarkFrontmatterMdParser(SyntaxHighlighter syntaxHighlighter,
                                     MathRenderer mathRenderer,
                                     YamlParser yamlParser,
                                     BuildEventBus eventBus) {
    super(eventBus, yamlParser);
    final MutableDataSet options = new MutableDataSet();
    options.set(Parser.EXTENSIONS, List.of(
      TablesExtension.create(),
      GitLabExtension.create()
    ));
    mdParser = Parser.builder(options).build();
    htmlRenderer = HtmlRenderer.builder(options)
      .extensions(List.of(
        new PreRenderExtension(syntaxHighlighter, mathRenderer)
      ))
      .build();
  }

  @Override
  protected String renderHtml(String markdownContent) {
    return htmlRenderer.render(mdParser.parse(markdownContent));
  }
}
