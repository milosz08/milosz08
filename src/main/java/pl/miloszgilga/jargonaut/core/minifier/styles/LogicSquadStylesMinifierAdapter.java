package pl.miloszgilga.jargonaut.core.minifier.styles;

import pl.miloszgilga.jargonaut.core.event.BuildEventBus;
import pl.miloszgilga.jargonaut.core.minifier.GenericLogicSquadMinifier;

import net.logicsquad.minifier.Minifier;
import net.logicsquad.minifier.css.CSSMinifier;

import java.io.StringReader;

public class LogicSquadStylesMinifierAdapter extends GenericLogicSquadMinifier implements StylesMinifier {
  public LogicSquadStylesMinifierAdapter(BuildEventBus eventBus) {
    super(eventBus);
  }

  @Override
  protected Minifier getMinifier(StringReader stringReader) {
    return new CSSMinifier(stringReader);
  }
}
