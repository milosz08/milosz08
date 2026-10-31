package pl.miloszgilga.jargonaut.scripts;

import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;

import java.nio.file.Path;
import java.util.List;

public class InlineScriptsProcessor extends GenericScriptsProcessor<String> {
  public InlineScriptsProcessor(Path baseDir, SiteBuildContext siteBuildContext) {
    super(baseDir, siteBuildContext.getConfig().getDir().getInlineScripts());
  }

  @Override
  protected String processScript(SiteBuildContext context, Path scriptFile, String scriptContent) {
    return scriptContent;
  }

  @Override
  protected void persistScripts(SiteBuildContext context, List<String> renderedScripts) {
    context.getInlineScripts().addAll(renderedScripts);
  }
}
