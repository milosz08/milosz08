package pl.miloszgilga.jargonaut.page.error.generator;

import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.parser.ParsedDocument;
import pl.miloszgilga.jargonaut.core.template.FreemarkerTemplateName;
import pl.miloszgilga.jargonaut.core.template.TemplateName;
import pl.miloszgilga.jargonaut.core.template.generator.GeneratorContext;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlGenerator;
import pl.miloszgilga.jargonaut.core.template.generator.JsonLdMap;
import pl.miloszgilga.jargonaut.globals.model.GlobalsMetadata;
import pl.miloszgilga.jargonaut.page.error.model.ErrorMetadata;

import java.util.List;

public class ErrorHtmlGenerator extends HtmlGenerator<ErrorMetadata> {
  public ErrorHtmlGenerator(PageRepresentation<ErrorMetadata> pageRepresentation,
                            GeneratorContext generatorContext) {
    super(pageRepresentation, generatorContext);
  }

  @Override
  protected Object generateJsonLdScript(String canonicalUrl,
                                        String language,
                                        GlobalsMetadata globals,
                                        ParsedDocument<ErrorMetadata> document) {
    final JsonLdMap websiteSchema = createBaseSchema("WebSite", canonicalUrl, language, document);
    final JsonLdMap personSchema = createPersonSchema(canonicalUrl, globals)
      .add("@context", "https://schema.org");
    return List.of(websiteSchema, personSchema);
  }

  @Override
  protected TemplateName getHtmlTemplateName() {
    return FreemarkerTemplateName.ERROR_PAGE;
  }
}
