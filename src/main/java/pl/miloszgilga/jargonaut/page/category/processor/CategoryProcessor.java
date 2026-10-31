package pl.miloszgilga.jargonaut.page.category.processor;

import pl.miloszgilga.jargonaut.core.processor.ProcessorContext;
import pl.miloszgilga.jargonaut.core.processor.md.PageMergingMdProcessor;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlGeneratorFactory;
import pl.miloszgilga.jargonaut.page.category.generator.CategoryHtmlGenerator;
import pl.miloszgilga.jargonaut.page.category.model.CategoryMetadata;

public class CategoryProcessor extends PageMergingMdProcessor<CategoryMetadata> {
  public CategoryProcessor(ProcessorContext processorContext) {
    super("category", processorContext);
  }

  @Override
  protected Class<CategoryMetadata> getTargetClass() {
    return CategoryMetadata.class;
  }

  @Override
  protected HtmlGeneratorFactory<CategoryMetadata> getHtmlRendererFactory() {
    return CategoryHtmlGenerator::new;
  }

  @Override
  public int order() {
    return HIGHEST_PRIORITY - 40;
  }
}
