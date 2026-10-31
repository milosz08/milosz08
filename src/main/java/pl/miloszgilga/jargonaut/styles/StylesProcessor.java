package pl.miloszgilga.jargonaut.styles;

import pl.miloszgilga.jargonaut.asset.resolver.AssetAliasResolver;
import pl.miloszgilga.jargonaut.core.TextContentType;
import pl.miloszgilga.jargonaut.core.file.FileIndexComparator;
import pl.miloszgilga.jargonaut.core.file.FileUtils;
import pl.miloszgilga.jargonaut.core.file.HashUtils;
import pl.miloszgilga.jargonaut.core.minifier.styles.StylesMinifier;
import pl.miloszgilga.jargonaut.core.processor.ResourceCheckerProcessor;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.template.generator.RenderedTextContent;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public class StylesProcessor extends ResourceCheckerProcessor {
  private final StylesMinifier stylesMinifier;
  private final AssetAliasResolver assetAliasResolver;
  private final Comparator<Path> fileIndexComparator;

  public StylesProcessor(Path baseDir,
                         SiteBuildContext siteBuildContext,
                         StylesMinifier stylesMinifier,
                         AssetAliasResolver assetAliasResolver) {
    super(baseDir.resolve(siteBuildContext.getConfig().getDir().getStyles()));
    this.stylesMinifier = stylesMinifier;
    this.assetAliasResolver = assetAliasResolver;
    fileIndexComparator = new FileIndexComparator();
  }

  @Override
  protected void processAfterCheck(SiteBuildContext context) {
    final List<Path> styleFiles = FileUtils.findFilesInDirectory(resourcePath, ".css", p -> true).stream()
      .sorted(fileIndexComparator)
      .toList();
    if (styleFiles.isEmpty()) {
      return;
    }
    final StringBuilder rawCombinedStyles = new StringBuilder();
    for (final Path file : styleFiles) {
      rawCombinedStyles.append(FileUtils.readAsString(file)).append("\n");
    }
    String stylesContent = rawCombinedStyles.toString();
    stylesContent = assetAliasResolver.resolveAliases(
      stylesContent,
      resourcePath,
      context.getAssetTargets()
    );
    if (Boolean.TRUE.equals(context.getConfig().getMinify())) {
      stylesContent = stylesMinifier.minify(stylesContent);
    }
    final String fileName = "styles." + HashUtils.generateTextHash(stylesContent) + ".css";
    context.getRenderedTextContents().put(TextContentType.CSS, List.of(
      new RenderedTextContent(Path.of("styles", fileName), stylesContent)
    ));
  }

  @Override
  protected boolean isBaseResourceExists() {
    return Files.exists(resourcePath);
  }
}
