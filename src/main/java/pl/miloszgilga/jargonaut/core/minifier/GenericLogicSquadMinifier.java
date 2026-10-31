package pl.miloszgilga.jargonaut.core.minifier;

import pl.miloszgilga.jargonaut.core.event.BuildEventBus;

import net.logicsquad.minifier.MinificationException;
import net.logicsquad.minifier.Minifier;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public abstract class GenericLogicSquadMinifier extends GenericMinifier {
  protected GenericLogicSquadMinifier(BuildEventBus eventBus) {
    super(eventBus);
  }

  @Override
  public String minify(String rawContent) {
    String minifiedContent;
    try (final StringWriter output = new StringWriter()) {
      final Minifier minifier = getMinifier(new StringReader(rawContent));
      minifier.minify(output);
      output.flush();
      minifiedContent = output.toString();
    } catch (IOException | MinificationException ex) {
      minifiedContent = rawContent;
    }
    return minifiedContent;
  }

  protected abstract Minifier getMinifier(StringReader stringReader);
}
