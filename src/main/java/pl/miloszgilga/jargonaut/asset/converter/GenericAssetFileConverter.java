package pl.miloszgilga.jargonaut.asset.converter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class GenericAssetFileConverter implements AssetFileConverter {
  @Override
  public final void convertAndSave(Path sourceFile, Path targetFile) throws IOException {
    Files.createDirectories(targetFile.getParent());
    executeConvertAndSave(sourceFile, targetFile);
  }

  protected abstract void executeConvertAndSave(Path sourceFile, Path targetFile) throws IOException;
}
