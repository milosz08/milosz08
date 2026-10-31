package pl.miloszgilga.jargonaut.asset.converter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public abstract class GenericExtensionAssetFileConverter extends GenericAssetFileConverter {
  @Override
  public final boolean supports(String fileName) {
    return matchExtensions(fileName, getSupportedExtensions());
  }

  @Override
  public final void executeConvertAndSave(Path sourceFile, Path targetFile) throws IOException {
    final String fileName = sourceFile.getFileName().toString();
    if (matchExtensions(fileName, getMirrorCopyExtensions())) {
      Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
      return;
    }
    mirrorCopyConvertAndSave(sourceFile, targetFile);
  }

  private boolean matchExtensions(String fileName, List<String> extensions) {
    if (extensions.isEmpty()) {
      return false;
    }
    return extensions.stream().anyMatch(extension -> fileName.toLowerCase().endsWith("." + extension));
  }

  protected abstract void mirrorCopyConvertAndSave(Path sourceFile, Path targetFile) throws IOException;

  protected abstract List<String> getSupportedExtensions();

  protected List<String> getMirrorCopyExtensions() {
    return List.of();
  }
}
