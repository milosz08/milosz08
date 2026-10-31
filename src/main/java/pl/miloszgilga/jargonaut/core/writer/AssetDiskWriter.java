package pl.miloszgilga.jargonaut.core.writer;

import pl.miloszgilga.jargonaut.asset.AssetTarget;
import pl.miloszgilga.jargonaut.core.event.BuildEvent;
import pl.miloszgilga.jargonaut.core.event.FileProcessedData;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class AssetDiskWriter extends GenericParallelWriter {
  @Override
  protected List<Future<?>> submitTasks(SiteBuildContext context, Path outputDir, ExecutorService executor) {
    final Map<Path, AssetTarget> assetTargets = context.getAssetTargets();
    final List<Future<?>> futures = new ArrayList<>();
    if (assetTargets == null || assetTargets.isEmpty()) {
      return null;
    }
    for (final Map.Entry<Path, AssetTarget> entry : assetTargets.entrySet()) {
      futures.add(executor.submit(() -> run(context, entry.getValue(), outputDir, entry.getKey())));
    }
    return futures;
  }

  private Void run(SiteBuildContext context, AssetTarget target, Path outputDir, Path sourceFile) throws IOException {
    final Path targetFile = outputDir.resolve(target.fullPath());
    Files.createDirectories(targetFile.getParent());
    boolean fromCache = false;
    if (target.assetFileConverter().isCacheable()) {
      final Path cacheBaseDir = outputDir.resolveSibling(context.getConfig().getCache().getCacheDir());
      final Path cachedFile = cacheBaseDir.resolve(target.fullPath());
      Files.createDirectories(cachedFile.getParent());
      if (Files.exists(cachedFile)) {
        Files.copy(cachedFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
        fromCache = true;
      } else {
        target.assetFileConverter().convertAndSave(sourceFile, cachedFile);
        Files.copy(cachedFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
      }
    } else {
      target.assetFileConverter().convertAndSave(sourceFile, targetFile);
    }
    final long fileSize = Files.size(targetFile);
    context.getEventBus().publish(BuildEvent.tick(
      getPhaseName(),
      new FileProcessedData(target.fullPath(), fileSize, fromCache)
    ));
    return null;
  }

  @Override
  protected String getPhaseName() {
    return "Optimizing and copying assets";
  }
}
