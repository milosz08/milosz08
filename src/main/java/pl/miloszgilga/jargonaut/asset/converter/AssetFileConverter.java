package pl.miloszgilga.jargonaut.asset.converter;

import java.io.IOException;
import java.nio.file.Path;

public interface AssetFileConverter {
  boolean supports(String fileName);

  String getTargetExtension(String originalExtension);

  void convertAndSave(Path sourceFile, Path targetFile) throws IOException;

  default boolean isCacheable() {
    return false;
  }
}
