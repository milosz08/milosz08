package pl.miloszgilga.jargonaut.asset.converter;

import pl.miloszgilga.jargonaut.core.minifier.svg.SvgMinifier;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class SvgAssetFileConverter extends GenericExtensionAssetFileConverter {
  private final SvgMinifier svgMinifier;

  public SvgAssetFileConverter(SvgMinifier svgMinifier) {
    this.svgMinifier = svgMinifier;
  }

  @Override
  protected void mirrorCopyConvertAndSave(Path sourceFile, Path targetFile) throws IOException {
    final String content = Files.readString(sourceFile, StandardCharsets.UTF_8);
    final String minified = svgMinifier.minify(content);
    Files.writeString(targetFile, minified, StandardCharsets.UTF_8);
  }

  @Override
  protected List<String> getSupportedExtensions() {
    return List.of("svg");
  }

  @Override
  public String getTargetExtension(String originalExtension) {
    return originalExtension;
  }
}
