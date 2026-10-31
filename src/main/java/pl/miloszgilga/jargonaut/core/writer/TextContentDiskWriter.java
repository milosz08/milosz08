package pl.miloszgilga.jargonaut.core.writer;

import pl.miloszgilga.jargonaut.core.TextContentType;
import pl.miloszgilga.jargonaut.core.event.BuildEvent;
import pl.miloszgilga.jargonaut.core.event.FileProcessedData;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.template.generator.RenderedTextContent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class TextContentDiskWriter extends GenericParallelWriter {
  @Override
  protected List<Future<?>> submitTasks(SiteBuildContext context, Path outputDir, ExecutorService executor) {
    final Map<TextContentType, List<RenderedTextContent>> renderedTextContents = context.getRenderedTextContents();
    final List<Future<?>> futures = new ArrayList<>();
    if (renderedTextContents == null || renderedTextContents.isEmpty()) {
      return futures;
    }
    for (final Map.Entry<TextContentType, List<RenderedTextContent>> entry : renderedTextContents.entrySet()) {
      for (final RenderedTextContent content : entry.getValue()) {
        futures.add(executor.submit(() -> run(context, outputDir, content)));
      }
    }
    return futures;
  }

  private Void run(SiteBuildContext context, Path outputDir, RenderedTextContent content) throws IOException {
    final Path targetFile = outputDir.resolve(content.outputPath());
    Files.createDirectories(targetFile.getParent());

    final byte[] contentBytes = content.textContent().getBytes(StandardCharsets.UTF_8);
    final long fileSize = contentBytes.length;

    Files.writeString(targetFile, content.textContent(), StandardCharsets.UTF_8);
    context.getEventBus().publish(BuildEvent.tick(
      getPhaseName(),
      new FileProcessedData(content.outputPath().toString(), fileSize, false)
    ));
    return null;
  }

  @Override
  protected String getPhaseName() {
    return "Generating text files";
  }
}
