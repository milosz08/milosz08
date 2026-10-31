package pl.miloszgilga.jargonaut.asset.processor;

import pl.miloszgilga.jargonaut.asset.AssetTarget;
import pl.miloszgilga.jargonaut.asset.converter.NoopAssetFileConverter;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.processor.TaskProcessor;

import java.nio.file.Files;
import java.nio.file.Path;

public class FaviconAssetProcessor implements TaskProcessor {
  private final Path baseDir;

  public FaviconAssetProcessor(Path baseDir) {
    this.baseDir = baseDir;
  }

  @Override
  public void process(SiteBuildContext context) {
    final String faviconUrl = context.getConfig().getSeo().getFavicon();
    if (faviconUrl == null) {
      return;
    }
    final Path faviconInPath = baseDir.resolve(faviconUrl);
    if (!Files.exists(faviconInPath)) {
      return;
    }
    context.getAssetTargets().put(faviconInPath, new AssetTarget("", faviconUrl, new NoopAssetFileConverter()));
  }
}
