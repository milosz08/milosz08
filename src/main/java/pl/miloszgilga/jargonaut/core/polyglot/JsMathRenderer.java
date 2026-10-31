package pl.miloszgilga.jargonaut.core.polyglot;

import pl.miloszgilga.jargonaut.core.event.BuildEventBus;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;

public class JsMathRenderer extends GenericGraalJsRenderer implements MathRenderer {
  public JsMathRenderer(SiteBuildContext context, BuildEventBus eventBus) {
    super(context.getConfig().getParser().getMath(), context, eventBus, "/js/katex.js");
  }

  @Override
  public String renderMath(String tex, boolean isDisplayMode) {
    return functionValue.execute(tex, isDisplayMode).asString();
  }
}
