package pl.miloszgilga.jargonaut.core.template.generator;

import pl.miloszgilga.jargonaut.core.parser.GenericPage;
import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;

public abstract class DynamicHtmlGenerator<T extends GenericPage> extends HtmlGenerator<T> {
  protected final String slug;

  protected DynamicHtmlGenerator(PageRepresentation<T> pageRepresentation,
                                 GeneratorContext generatorContext,
                                 String slug) {
    super(pageRepresentation, generatorContext);
    this.slug = slug;
  }
}
