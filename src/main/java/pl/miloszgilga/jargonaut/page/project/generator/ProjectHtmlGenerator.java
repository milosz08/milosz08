package pl.miloszgilga.jargonaut.page.project.generator;

import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.parser.ParsedDocument;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.template.FreemarkerTemplateName;
import pl.miloszgilga.jargonaut.core.template.TemplateName;
import pl.miloszgilga.jargonaut.core.template.generator.GeneratorContext;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlGenerator;
import pl.miloszgilga.jargonaut.globals.model.GlobalsMetadata;
import pl.miloszgilga.jargonaut.page.project.CategoryComparator;
import pl.miloszgilga.jargonaut.page.project.dto.ProjectCategoriesTableView;
import pl.miloszgilga.jargonaut.page.project.model.Category;
import pl.miloszgilga.jargonaut.page.project.model.ProjectMetadata;
import pl.miloszgilga.jargonaut.page.project.model.ProjectSharedData;
import pl.miloszgilga.jargonaut.page.tag.model.TagItem;
import pl.miloszgilga.jargonaut.page.tag.model.TagSharedData;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

public class ProjectHtmlGenerator extends HtmlGenerator<ProjectMetadata> {
  public ProjectHtmlGenerator(PageRepresentation<ProjectMetadata> pageRepresentation,
                              GeneratorContext generatorContext) {
    super(pageRepresentation, generatorContext);
  }

  @Override
  protected Object generateJsonLdScript(String canonicalUrl,
                                        String language,
                                        GlobalsMetadata globals,
                                        ParsedDocument<ProjectMetadata> document) {
    return createBaseSchema("CollectionPage", canonicalUrl, language, document)
      .add("author", createPersonSchema(canonicalUrl, globals));
  }

  @Override
  protected TemplateName getHtmlTemplateName() {
    return FreemarkerTemplateName.PROJECT_PAGE;
  }

  @Override
  protected void insertPageAttributes(Map<String, Object> pageAttributes,
                                      Locale locale,
                                      ParsedDocument<ProjectMetadata> document,
                                      SiteBuildContext context) {
    pageAttributes.put("projectCategories", createProjectCategories(context, locale));
  }

  private List<ProjectCategoriesTableView> createProjectCategories(SiteBuildContext context, Locale locale) {
    final ProjectSharedData projectData = context.getSharedData(ProjectSharedData.class);
    final TagSharedData tagsData = context.getSharedData(TagSharedData.class);
    if (projectData != null && tagsData != null) {
      final Category root = projectData.getRootProjectCategory();
      if (root == null) {
        return List.of();
      }
      final List<TagItem> tags = tagsData.getTagItems().get(locale);
      return Stream.concat(
          root.getSubCategories().stream(),
          root.getProjects().isEmpty() ? Stream.empty() : Stream.of(root)
        )
        .sorted(new CategoryComparator())
        .map(category -> new ProjectCategoriesTableView(category, locale, tags, context.getConfig(), projectData))
        .toList();
    }
    return Collections.emptyList();
  }
}
