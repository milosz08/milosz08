package pl.miloszgilga.jargonaut.asset.resolver;

import java.nio.file.Path;

public class GlobalAssetResolutionStrategy implements AssetResolutionStrategy {
  private static final String GLOBAL_PREFIX = "/~";

  @Override
  public String getPrefix() {
    return GLOBAL_PREFIX;
  }

  @Override
  public boolean supports(String aliasPath) {
    return aliasPath != null && aliasPath.startsWith(GLOBAL_PREFIX);
  }

  @Override
  public Path resolveKey(String aliasPath, Path resourcePath, Path baseAssetsDir) {
    if (baseAssetsDir == null) {
      return null;
    }
    final String cleanGlobalPath = aliasPath.substring(1);
    return baseAssetsDir.resolve(cleanGlobalPath).normalize();
  }

  @Override
  public String formatReplacementPath(String hashedWebPath) {
    return hashedWebPath.startsWith("/") ? hashedWebPath : "/" + hashedWebPath;
  }
}
