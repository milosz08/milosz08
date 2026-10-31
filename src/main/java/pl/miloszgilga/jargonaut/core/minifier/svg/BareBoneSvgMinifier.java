package pl.miloszgilga.jargonaut.core.minifier.svg;

import pl.miloszgilga.jargonaut.core.event.BuildEventBus;
import pl.miloszgilga.jargonaut.core.minifier.GenericMinifier;

public class BareBoneSvgMinifier extends GenericMinifier implements SvgMinifier {
  public BareBoneSvgMinifier(BuildEventBus eventBus) {
    super(eventBus);
  }

  @Override
  public String minify(String rawContent) {
    return rawContent
      .replaceAll("<!--.*?-->", "")
      .replaceAll("(?s)<metadata>.*?</metadata>", "")
      .replaceAll("\\s+", " ")
      .replaceAll(">\\s+<", "><")
      .trim();
  }
}
