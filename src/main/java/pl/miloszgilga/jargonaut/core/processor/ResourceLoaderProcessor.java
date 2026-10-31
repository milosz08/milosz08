package pl.miloszgilga.jargonaut.core.processor;

import pl.miloszgilga.jargonaut.asset.AssetTarget;
import pl.miloszgilga.jargonaut.asset.AssetTransformer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ResourceLoaderProcessor extends ResourceCheckerProcessor {
  private static final String ASSETS_DIR = "assets";

  protected final AssetTransformer assetTransformer;

  protected ResourceLoaderProcessor(Path resourcePath, AssetTransformer assetTransformer) {
    super(resourcePath);
    this.assetTransformer = assetTransformer;
  }

  @Override
  protected final void processAfterCheck(SiteBuildContext context) {
    final Map<String, List<Path>> processedAssets = new HashMap<>();
    if (isLoadingAssets()) {
      processedAssets.put(ASSETS_DIR, processAssetDirectory(context, ASSETS_DIR));
      final List<String> additionalAssets = new ArrayList<>(loadAdditionalAssets());
      additionalAssets.remove(ASSETS_DIR);
      if (!additionalAssets.isEmpty()) {
        for (final String asset : additionalAssets) {
          processedAssets.put(asset, processAssetDirectory(context, asset));
        }
      }
    }
    processAfterLoad(context, processedAssets);
  }

  private List<Path> processAssetDirectory(SiteBuildContext context, String dirName) {
    final Path dirToProcess = resourcePath.resolve("~" + dirName);
    final Map<Path, AssetTarget> result = assetTransformer.processDirectory(dirToProcess, dirName, getTargetWebDir());
    context.getAssetTargets().putAll(result);
    return new ArrayList<>(result.keySet());
  }

  protected List<String> loadAdditionalAssets() {
    return List.of();
  }

  protected abstract void processAfterLoad(SiteBuildContext context, Map<String, List<Path>> processedAssets);

  protected boolean isLoadingAssets() {
    return true;
  }

  protected abstract String getTargetWebDir();
}
