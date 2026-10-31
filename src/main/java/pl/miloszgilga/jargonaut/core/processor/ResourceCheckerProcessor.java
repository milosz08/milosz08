package pl.miloszgilga.jargonaut.core.processor;

import pl.miloszgilga.jargonaut.core.GenerationHaltedException;

import java.nio.file.Path;

public abstract class ResourceCheckerProcessor implements TaskProcessor {
  protected final Path resourcePath;

  protected ResourceCheckerProcessor(Path resourcePath) {
    this.resourcePath = resourcePath;
  }

  @Override
  public final void process(SiteBuildContext context) {
    if (!isBaseResourceExists()) {
      throw new GenerationHaltedException("Resources at path " + resourcePath + " does not exist and is required");
    }
    processAfterCheck(context);
  }

  protected abstract void processAfterCheck(SiteBuildContext context);

  protected abstract boolean isBaseResourceExists();
}
