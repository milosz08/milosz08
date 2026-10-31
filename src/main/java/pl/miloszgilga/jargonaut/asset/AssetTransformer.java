package pl.miloszgilga.jargonaut.asset;

import java.nio.file.Path;
import java.util.Map;

public interface AssetTransformer {
  Map<Path, AssetTarget> processDirectory(Path sourceDir, String resourceName, String targetWebDir);

  default Map<Path, AssetTarget> processSelfDirectory(Path sourceDir) {
    return processDirectory(sourceDir, "", "");
  }
}
