package pl.miloszgilga.jargonaut.core.writer;

import pl.miloszgilga.jargonaut.core.GenerationHaltedException;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.processor.TaskProcessor;

import java.nio.file.Path;

public abstract class GenericWriter implements TaskProcessor {
  @Override
  public final void process(SiteBuildContext context) {
    try {
      process(context, Path.of(context.getConfig().getDir().getOutput()));
    } catch (Exception ex) {
      throw new GenerationHaltedException(ex);
    }
  }

  protected abstract void process(SiteBuildContext context, Path outputDir) throws Exception;
}
