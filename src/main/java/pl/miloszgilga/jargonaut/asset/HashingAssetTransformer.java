package pl.miloszgilga.jargonaut.asset;

import pl.miloszgilga.jargonaut.asset.converter.AssetFileConverter;
import pl.miloszgilga.jargonaut.asset.converter.NoopAssetFileConverter;
import pl.miloszgilga.jargonaut.core.event.BuildEvent;
import pl.miloszgilga.jargonaut.core.event.BuildEventBus;
import pl.miloszgilga.jargonaut.core.file.FileUtils;
import pl.miloszgilga.jargonaut.core.file.HashUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashingAssetTransformer implements AssetTransformer {
  private final List<AssetFileConverter> converters;

  public HashingAssetTransformer(BuildEventBus eventBus, List<AssetFileConverter> customConverters) {
    converters = new ArrayList<>();
    if (customConverters != null) {
      converters.addAll(customConverters);
    }
    converters.add(new NoopAssetFileConverter());
    for (final AssetFileConverter assetFileConverter : converters) {
      eventBus.publish(BuildEvent.spinnerDetail("Registering asset file converter: " +
        assetFileConverter.getClass().getSimpleName() + " for hashing asset transformer"));
    }
  }

  public HashingAssetTransformer(BuildEventBus eventBus) {
    this(eventBus, List.of());
  }

  @Override
  public Map<Path, AssetTarget> processDirectory(Path sourceDir, String resourceName, String targetWebDir) {
    final Map<Path, AssetTarget> results = new HashMap<>();
    if (sourceDir == null || !Files.exists(sourceDir)) {
      return Map.of();
    }
    String baseUrl = "";
    if (resourceName != null && !resourceName.isEmpty()) {
      baseUrl = resourceName.endsWith("/") ? resourceName : resourceName + "/";
    }
    final List<Path> files = FileUtils.listAllFiles(sourceDir);

    for (final Path file : files) {
      final String fileName = file.getFileName().toString();
      final String hash = HashUtils.generateFileHash(file);

      final int dotIndex = fileName.lastIndexOf('.');
      final String nameWithoutExt = dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;
      final String originalExtension = dotIndex > 0 ? fileName.substring(dotIndex) : "";

      AssetFileConverter matchedConverter = new NoopAssetFileConverter();
      for (final AssetFileConverter converter : converters) {
        if (converter.supports(fileName)) {
          matchedConverter = converter;
          break;
        }
      }
      final String primaryExt = matchedConverter.getTargetExtension(originalExtension);
      final String primaryHashedName = nameWithoutExt + "." + hash + primaryExt;

      final Path relativePath = sourceDir.relativize(file);
      final Path hashedRelativePath = relativePath.resolveSibling(primaryHashedName);

      final String shortRelativePath = baseUrl + hashedRelativePath.toString().replace('\\', '/');

      String fullRelativePath = shortRelativePath;
      if (targetWebDir != null && !targetWebDir.isBlank()) {
        fullRelativePath = (targetWebDir.endsWith("/") ? targetWebDir : targetWebDir + "/") + shortRelativePath;
      }
      final AssetTarget target = new AssetTarget(
        shortRelativePath,
        fullRelativePath,
        matchedConverter
      );
      results.put(file, target);
    }
    return results;
  }
}
