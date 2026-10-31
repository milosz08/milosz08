package pl.miloszgilga.jargonaut.core.template.generator;

import pl.miloszgilga.jargonaut.core.parser.GenericPage;
import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;

public interface HtmlGeneratorFactory<T extends GenericPage> {
  HtmlGenerator<T> create(PageRepresentation<T> page, GeneratorContext context);
}
