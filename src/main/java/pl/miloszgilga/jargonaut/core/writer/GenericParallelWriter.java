package pl.miloszgilga.jargonaut.core.writer;

import pl.miloszgilga.jargonaut.core.GenerationHaltedException;
import pl.miloszgilga.jargonaut.core.event.BuildEvent;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

abstract class GenericParallelWriter extends GenericWriter {
  @Override
  public void process(SiteBuildContext context, Path outputDir) throws Exception {
    try (final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      final List<Future<?>> futures = submitTasks(context, outputDir, executor);
      if (futures == null || futures.isEmpty()) {
        return;
      }
      publishBuildEvent(context, phaseName -> BuildEvent.startPhase(phaseName, futures.size()));
      try {
        for (final Future<?> future : futures) {
          future.get();
        }
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
        throw new GenerationHaltedException(ex);
      } catch (ExecutionException ex) {
        throw new GenerationHaltedException(ex);
      } finally {
        publishBuildEvent(context, BuildEvent::endPhase);
      }
    }
  }

  private void publishBuildEvent(SiteBuildContext context, Function<String, BuildEvent> eventFactory) {
    final String phaseName = getPhaseName();
    if (phaseName != null && !phaseName.isBlank()) {
      context.getEventBus().publish(eventFactory.apply(phaseName));
    }
  }

  protected abstract List<Future<?>> submitTasks(SiteBuildContext context, Path outputDir, ExecutorService executor);

  protected abstract String getPhaseName();
}
