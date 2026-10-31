package pl.miloszgilga.jargonaut.scripts;

import pl.miloszgilga.jargonaut.core.file.FileIndexComparator;
import pl.miloszgilga.jargonaut.core.file.FileUtils;
import pl.miloszgilga.jargonaut.core.processor.ResourceCheckerProcessor;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class GenericScriptsProcessor<T> extends ResourceCheckerProcessor {
  protected final Comparator<Path> fileIndexComparator;

  protected GenericScriptsProcessor(Path baseDir, String dir) {
    super(baseDir.resolve(dir));
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
    final List<T> renderedScripts = new ArrayList<>();
    for (final Path scriptFile : scriptFiles) {
      renderedScripts.add(processScript(context, scriptFile, FileUtils.readAsString(scriptFile)));
    }
    persistScripts(context, renderedScripts);
  }

  @Override
  protected boolean isBaseResourceExists() {
    return Files.exists(resourcePath);
  }

  protected abstract T processScript(SiteBuildContext context, Path scriptFile, String scriptContent);

  protected abstract void persistScripts(SiteBuildContext context, List<T> renderedScripts);
}
