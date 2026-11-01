package pl.miloszgilga.jargonaut.scripts;

import pl.miloszgilga.jargonaut.core.TextContentType;
import pl.miloszgilga.jargonaut.core.file.HashUtils;
import pl.miloszgilga.jargonaut.core.minifier.js.JsMinifier;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.template.generator.RenderedTextContent;

import java.nio.file.Path;
import java.util.List;

public class ScriptsProcessor extends GenericScriptsProcessor<RenderedTextContent> {
  private final JsMinifier jsMinifier;

  public ScriptsProcessor(Path baseDir, SiteBuildContext siteBuildContext, JsMinifier jsMinifier) {
    super(baseDir, siteBuildContext.getConfig().getDir().getScripts());
    this.jsMinifier = jsMinifier;
  }

  @Override
  protected RenderedTextContent processScript(SiteBuildContext context, Path scriptFile, String scriptContent) {
    String scriptRawContent = scriptContent;
    if (Boolean.TRUE.equals(context.getConfig().getMinify())) {
      scriptRawContent = jsMinifier.minify(scriptContent);
    }
    final String fullFileName = scriptFile.getFileName().toString();
    String baseName = fullFileName.substring(0, fullFileName.lastIndexOf('.'));
    final int firstDotIndex = baseName.indexOf('.');
    if (firstDotIndex != -1) {
      baseName = baseName.substring(firstDotIndex + 1);
    }
    final String fileName = baseName + "." + HashUtils.generateTextHash(scriptRawContent) + ".js";
    return new RenderedTextContent(Path.of("scripts", fileName), scriptRawContent);
  }

  @Override
  protected void persistScripts(SiteBuildContext context, List<RenderedTextContent> renderedScripts) {
    context.getRenderedTextContents().put(TextContentType.JS, renderedScripts);
  }
}
