package pl.miloszgilga.jargonaut.asset.processor;

import pl.miloszgilga.jargonaut.asset.AssetTransformer;
import pl.miloszgilga.jargonaut.core.processor.ResourceLoaderProcessor;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class GlobalAssetsProcessor extends ResourceLoaderProcessor {
  public GlobalAssetsProcessor(Path resourcePath, AssetTransformer assetTransformer) {
    super(resourcePath, assetTransformer);
  }

  @Override
  protected boolean isBaseResourceExists() {
    return Files.exists(resourcePath);
  }

  @Override
  protected void processAfterLoad(SiteBuildContext context, Map<String, List<Path>> processedAssets) {
  }

  @Override
  protected String getTargetWebDir() {
    return null;
  }
}
