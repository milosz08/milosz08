package pl.miloszgilga.jargonaut.page.tag.processor;

import pl.miloszgilga.jargonaut.core.processor.ProcessorContext;
import pl.miloszgilga.jargonaut.core.processor.md.PageMergingMdProcessor;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlGeneratorFactory;
import pl.miloszgilga.jargonaut.page.tag.generator.TagHtmlGenerator;
import pl.miloszgilga.jargonaut.page.tag.model.TagMetadata;

public class TagProcessor extends PageMergingMdProcessor<TagMetadata> {
  public TagProcessor(ProcessorContext processorContext) {
    super("tag", processorContext);
  }

  @Override
  protected Class<TagMetadata> getTargetClass() {
    return TagMetadata.class;
  }

  @Override
  protected HtmlGeneratorFactory<TagMetadata> getHtmlRendererFactory() {
    return TagHtmlGenerator::new;
  }

  @Override
  public int order() {
    return HIGHEST_PRIORITY - 20;
  }
}
