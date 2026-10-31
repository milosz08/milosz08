package pl.miloszgilga.jargonaut.core.writer;

import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class PrepareDirectoryDiskWriter extends GenericWriter {
  @Override
  public void process(SiteBuildContext context, Path outputDir) throws IOException {
    if (!Files.exists(outputDir)) {
      Files.createDirectories(outputDir);
      return;
    }
    try (final Stream<Path> walk = Files.walk(outputDir)) {
      for (final Path path : walk.sorted(Comparator.reverseOrder()).toList()) {
        Files.delete(path);
      }
    }
    Files.createDirectories(outputDir);
  }
}
