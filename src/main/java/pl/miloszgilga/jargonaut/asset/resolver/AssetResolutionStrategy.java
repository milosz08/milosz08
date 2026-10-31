package pl.miloszgilga.jargonaut.asset.resolver;

import java.nio.file.Path;

public interface AssetResolutionStrategy {
  String getPrefix();

  boolean supports(String aliasPath);

  Path resolveKey(String aliasPath, Path resourcePath, Path baseAssetsDir);

  String formatReplacementPath(String hashedWebPath);
}
