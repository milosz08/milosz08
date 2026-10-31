package pl.miloszgilga.jargonaut.asset.converter;

import pl.miloszgilga.jargonaut.core.GenerationHaltedException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.imageio.ImageIO;

public class WebpAssetFileConverter extends GenericExtensionAssetFileConverter {
  @Override
  protected List<String> getSupportedExtensions() {
    return List.of("png", "jpg", "jpeg");
  }

  @Override
  public String getTargetExtension(String originalExtension) {
    return ".webp";
  }

  @Override
  protected void mirrorCopyConvertAndSave(Path sourceFile, Path targetFile) throws IOException {
    final BufferedImage sourceImage = ImageIO.read(sourceFile.toFile());
    if (sourceImage == null) {
      throw new GenerationHaltedException("Unable to read image: " + sourceFile);
    }
    ImageIO.write(sourceImage, "webp", targetFile.toFile());
  }

  @Override
  public boolean isCacheable() {
    return true;
  }
}
