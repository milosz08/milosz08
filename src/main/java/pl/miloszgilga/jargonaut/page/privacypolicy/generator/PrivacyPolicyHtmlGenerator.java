package pl.miloszgilga.jargonaut.page.privacypolicy.generator;

import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.parser.ParsedDocument;
import pl.miloszgilga.jargonaut.core.template.FreemarkerTemplateName;
import pl.miloszgilga.jargonaut.core.template.TemplateName;
import pl.miloszgilga.jargonaut.core.template.generator.GeneratorContext;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlGenerator;
import pl.miloszgilga.jargonaut.globals.model.GlobalsMetadata;
import pl.miloszgilga.jargonaut.page.privacypolicy.model.PrivacyPolicyMetadata;

public class PrivacyPolicyHtmlGenerator extends HtmlGenerator<PrivacyPolicyMetadata> {
  public PrivacyPolicyHtmlGenerator(PageRepresentation<PrivacyPolicyMetadata> pageRepresentation,
                                    GeneratorContext generatorContext) {
    super(pageRepresentation, generatorContext);
  }

  @Override
  protected Object generateJsonLdScript(String canonicalUrl,
                                        String language,
                                        GlobalsMetadata globals,
                                        ParsedDocument<PrivacyPolicyMetadata> document) {
    return createBaseSchema("WebPage", canonicalUrl, language, document)
      .add("author", createPersonSchema(canonicalUrl, globals));
  }

  @Override
  protected TemplateName getHtmlTemplateName() {
    return FreemarkerTemplateName.PRIVACY_POLICY_PAGE;
  }
}
