package pl.miloszgilga.jargonaut.page.privacypolicy.processor;

import pl.miloszgilga.jargonaut.core.processor.ProcessorContext;
import pl.miloszgilga.jargonaut.core.processor.md.PageMergingMdProcessor;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlGeneratorFactory;
import pl.miloszgilga.jargonaut.page.privacypolicy.generator.PrivacyPolicyHtmlGenerator;
import pl.miloszgilga.jargonaut.page.privacypolicy.model.PrivacyPolicyMetadata;

public class PrivacyPolicyProcessor extends PageMergingMdProcessor<PrivacyPolicyMetadata> {
  public PrivacyPolicyProcessor(ProcessorContext processorContext) {
    super("privacy-policy", processorContext);
  }

  @Override
  protected Class<PrivacyPolicyMetadata> getTargetClass() {
    return PrivacyPolicyMetadata.class;
  }

  @Override
  protected HtmlGeneratorFactory<PrivacyPolicyMetadata> getHtmlRendererFactory() {
    return PrivacyPolicyHtmlGenerator::new;
  }

  @Override
  public int order() {
    return HIGHEST_PRIORITY - 80;
  }
}
