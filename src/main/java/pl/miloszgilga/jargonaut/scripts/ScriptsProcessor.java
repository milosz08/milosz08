package pl.miloszgilga.jargonaut.scripts;

import pl.miloszgilga.jargonaut.core.TextContentType;
import pl.miloszgilga.jargonaut.core.file.FileIndexComparator;
import pl.miloszgilga.jargonaut.core.file.FileUtils;
import pl.miloszgilga.jargonaut.core.file.HashUtils;
import pl.miloszgilga.jargonaut.core.minifier.js.JsMinifier;
import pl.miloszgilga.jargonaut.core.processor.ResourceCheckerProcessor;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.template.generator.RenderedTextContent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScriptsProcessor extends ResourceCheckerProcessor {
  private final JsMinifier jsMinifier;
  private final Comparator<Path> fileIndexComparator;

  public ScriptsProcessor(Path baseDir, SiteBuildContext siteBuildContext, JsMinifier jsMinifier) {
    super(baseDir.resolve(siteBuildContext.getConfig().getDir().getScripts()));
    this.jsMinifier = jsMinifier;
    fileIndexComparator = new FileIndexComparator();
  }

  @Override
  protected void processAfterCheck(SiteBuildContext context) {
    final List<Path> scriptFiles = FileUtils.findFilesInDirectory(resourcePath, ".js", p -> true).stream()
      .sorted(fileIndexComparator)
      .toList();
    if (scriptFiles.isEmpty()) {
      return;
    }
    final List<RenderedTextContent> renderedScripts = new ArrayList<>();
    for (final Path scriptFile : scriptFiles) {
      String jsContent = FileUtils.readAsString(scriptFile);
      if (Boolean.TRUE.equals(context.getConfig().getMinify())) {
        jsContent = jsMinifier.minify(jsContent);
      }
      final String fullFileName = scriptFile.getFileName().toString();
      final String baseName = fullFileName.substring(0, fullFileName.lastIndexOf('.'));
      final String fileName = baseName + "." + HashUtils.generateTextHash(jsContent) + ".js";
      renderedScripts.add(new RenderedTextContent(Path.of("scripts", fileName), jsContent));
    }
    context.getRenderedTextContents().put(TextContentType.JS, renderedScripts);
  }

  @Override
  protected boolean isBaseResourceExists() {
    return Files.exists(resourcePath);
  }
}
