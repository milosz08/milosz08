package pl.miloszgilga.jargonaut.page.education.generator;

import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.parser.ParsedDocument;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.template.FreemarkerTemplateName;
import pl.miloszgilga.jargonaut.core.template.TemplateName;
import pl.miloszgilga.jargonaut.core.template.generator.GeneratorContext;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlGenerator;
import pl.miloszgilga.jargonaut.core.template.generator.JsonLdMap;
import pl.miloszgilga.jargonaut.globals.model.GlobalsMetadata;
import pl.miloszgilga.jargonaut.page.education.dto.EducationView;
import pl.miloszgilga.jargonaut.page.education.dto.ResearchView;
import pl.miloszgilga.jargonaut.page.education.model.EducationItem;
import pl.miloszgilga.jargonaut.page.education.model.EducationMetadata;
import pl.miloszgilga.jargonaut.page.education.model.ResearchItem;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EducationHtmlGenerator extends HtmlGenerator<EducationMetadata> {
  public EducationHtmlGenerator(PageRepresentation<EducationMetadata> pageRepresentation,
                                GeneratorContext generatorContext) {
    super(pageRepresentation, generatorContext);
  }

  @Override
  protected Object generateJsonLdScript(String canonicalUrl,
                                        String language,
                                        GlobalsMetadata globals,
                                        ParsedDocument<EducationMetadata> document) {
    return createBaseSchema("ProfilePage", canonicalUrl, language, document)
      .add("mainEntity", createPersonSchema(canonicalUrl, globals)
        .addList("hasCredential", document.metadata().getEducation(), edu -> new JsonLdMap()
          .add("@type", "EducationalOccupationalCredential")
          .add("credentialCategory", edu.getType())
          .add("educationalLevel", edu.getType())
          .add("about", edu.getSpec())
          .add("recognizedBy", edu.getOrg() != null ? new JsonLdMap()
            .add("@type", "EducationalOrganization")
            .add("name", edu.getOrg())
            : null)
        )
      );
  }

  @Override
  protected TemplateName getHtmlTemplateName() {
    return FreemarkerTemplateName.EDUCATION_PAGE;
  }

  @Override
  protected void insertPageAttributes(Map<String, Object> pageAttributes,
                                      Locale locale,
                                      ParsedDocument<EducationMetadata> document,
                                      SiteBuildContext context) {
    pageAttributes.put("educationList", createEducationView(document));
    pageAttributes.put("researchList", createResearchView(document));
    pageAttributes.put("resume", document.metadata().getResume());

    System.out.println();
  }

  private List<EducationView> createEducationView(ParsedDocument<EducationMetadata> document) {
    final List<EducationItem> educationItems = document.metadata().getEducation();
    if (educationItems == null || educationItems.isEmpty()) {
      return Collections.emptyList();
    }
    return educationItems.stream().map(EducationView::new).toList();
  }

  private List<ResearchView> createResearchView(ParsedDocument<EducationMetadata> document) {
    final List<ResearchItem> researchItems = document.metadata().getResearch();
    if (researchItems == null || researchItems.isEmpty()) {
      return Collections.emptyList();
    }
    return researchItems.stream().map(ResearchView::new).toList();
  }
}
