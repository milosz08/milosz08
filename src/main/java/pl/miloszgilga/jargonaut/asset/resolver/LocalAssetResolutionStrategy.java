package pl.miloszgilga.jargonaut.asset.resolver;

import java.nio.file.Path;

public class LocalAssetResolutionStrategy implements AssetResolutionStrategy {
  private static final String LOCAL_PREFIX = "~";

  @Override
  public String getPrefix() {
    return LOCAL_PREFIX;
  }

  @Override
  public boolean supports(String aliasPath) {
    return aliasPath != null && aliasPath.startsWith(LOCAL_PREFIX);
  }

  @Override
  public Path resolveKey(String aliasPath, Path resourcePath, Path baseAssetsDir) {
    if (resourcePath == null) {
      return null;
    }
    return resourcePath.resolve(aliasPath).normalize();
  }

  @Override
  public String formatReplacementPath(String hashedWebPath) {
    return hashedWebPath.startsWith("/") ? hashedWebPath : "/" + hashedWebPath;
  }
}
