package pl.miloszgilga.jargonaut.asset.processor;

import pl.miloszgilga.jargonaut.asset.AssetTarget;
import pl.miloszgilga.jargonaut.asset.AssetTransformer;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.processor.TaskProcessor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class OgImageAssetProcessor implements TaskProcessor {
  private final Path baseDir;
  private final AssetTransformer assetTransformer;

  public OgImageAssetProcessor(Path baseDir, AssetTransformer assetTransformer) {
    this.baseDir = baseDir;
    this.assetTransformer = assetTransformer;
  }

  @Override
  public void process(SiteBuildContext context) {
    final String ogImageUrl = context.getConfig().getSeo().getOgImage();
    if (ogImageUrl == null) {
      return;
    }
    final Path ogImageInPath = baseDir.resolve(ogImageUrl);
    if (!Files.exists(ogImageInPath)) {
      return;
    }
    final Map<Path, AssetTarget> processed = assetTransformer.processSelfDirectory(baseDir);
    final AssetTarget ogTarget = processed.get(ogImageInPath);
    if (ogTarget == null) {
      return;
    }
    context.getConfig().getSeo().setOgImage(ogTarget.shortPath());
    context.getAssetTargets().put(ogImageInPath, ogTarget);
  }
}
