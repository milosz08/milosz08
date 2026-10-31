package pl.miloszgilga.jargonaut.asset.converter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class NoopAssetFileConverter extends GenericAssetFileConverter {
  @Override
  public boolean supports(String fileName) {
    return true;
  }

  @Override
  public String getTargetExtension(String originalExtension) {
    return originalExtension;
  }

  @Override
  protected void executeConvertAndSave(Path sourceFile, Path targetFile) throws IOException {
    Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
  }
}
