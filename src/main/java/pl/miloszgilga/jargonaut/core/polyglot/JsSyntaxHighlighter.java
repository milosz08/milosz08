package pl.miloszgilga.jargonaut.core.polyglot;

import pl.miloszgilga.jargonaut.core.event.BuildEventBus;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;

public class JsSyntaxHighlighter extends GenericGraalJsRenderer implements SyntaxHighlighter {
  public JsSyntaxHighlighter(SiteBuildContext context, BuildEventBus eventBus) {
    super(context.getConfig().getParser().getSyntax(), context, eventBus, "/js/highlight.js");
  }

  @Override
  public String renderHighlight(String code, String language) {
    return functionValue.execute(code, language).asString();
  }
}
