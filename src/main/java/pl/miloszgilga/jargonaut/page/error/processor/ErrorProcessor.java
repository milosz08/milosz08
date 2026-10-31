package pl.miloszgilga.jargonaut.page.error.processor;

import pl.miloszgilga.jargonaut.core.processor.ProcessorContext;
import pl.miloszgilga.jargonaut.core.processor.md.PageMergingMdProcessor;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlGeneratorFactory;
import pl.miloszgilga.jargonaut.page.error.generator.ErrorHtmlGenerator;
import pl.miloszgilga.jargonaut.page.error.model.ErrorMetadata;

public class ErrorProcessor extends PageMergingMdProcessor<ErrorMetadata> {
  public ErrorProcessor(ProcessorContext processorContext) {
    super("404", processorContext);
  }

  @Override
  protected Class<ErrorMetadata> getTargetClass() {
    return ErrorMetadata.class;
  }

  @Override
  protected HtmlGeneratorFactory<ErrorMetadata> getHtmlRendererFactory() {
    return ErrorHtmlGenerator::new;
  }

  @Override
  public int order() {
    return HIGHEST_PRIORITY - 90;
  }
}
