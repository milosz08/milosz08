package pl.miloszgilga.jargonaut.page.category.generator;

import pl.miloszgilga.jargonaut.core.WordCloudUtils;
import pl.miloszgilga.jargonaut.core.dto.AlphabeticalGroup;
import pl.miloszgilga.jargonaut.core.dto.AlphabeticalGroupView;
import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.parser.ParsedDocument;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.template.FreemarkerTemplateName;
import pl.miloszgilga.jargonaut.core.template.TemplateName;
import pl.miloszgilga.jargonaut.core.template.generator.GeneratorContext;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlGenerator;
import pl.miloszgilga.jargonaut.globals.model.GlobalsMetadata;
import pl.miloszgilga.jargonaut.page.category.dto.CategoryCloudView;
import pl.miloszgilga.jargonaut.page.category.dto.CategoryView;
import pl.miloszgilga.jargonaut.page.category.model.CategoryMetadata;
import pl.miloszgilga.jargonaut.page.project.model.Category;
import pl.miloszgilga.jargonaut.page.project.model.ProjectSharedData;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

public class CategoryHtmlGenerator extends HtmlGenerator<CategoryMetadata> {
  public CategoryHtmlGenerator(PageRepresentation<CategoryMetadata> pageRepresentation,
                               GeneratorContext generatorContext) {
    super(pageRepresentation, generatorContext);
  }

  @Override
  protected Object generateJsonLdScript(String canonicalUrl,
                                        String language,
                                        GlobalsMetadata globals,
                                        ParsedDocument<CategoryMetadata> document) {
    return createBaseSchema("CollectionPage", canonicalUrl, language, document)
      .add("author", createPersonSchema(canonicalUrl, globals));
  }

  @Override
  protected TemplateName getHtmlTemplateName() {
    return FreemarkerTemplateName.CATEGORY_PAGE;
  }

  @Override
  protected void insertPageAttributes(Map<String, Object> pageAttributes,
                                      Locale locale,
                                      ParsedDocument<CategoryMetadata> document,
                                      SiteBuildContext context) {
    pageAttributes.put("groupedCategories", createGroupedCategoryViews(context, locale));
    pageAttributes.put("categoryCloud", createCategoryCloudViews(context, locale));
  }

  private List<AlphabeticalGroup<CategoryView>> createGroupedCategoryViews(SiteBuildContext context,
                                                                           Locale locale) {
    final ProjectSharedData projectData = context.getSharedData(ProjectSharedData.class);
    if (projectData == null) {
      return Collections.emptyList();
    }
    final Category root = projectData.getRootProjectCategory();
    if (root == null) {
      return Collections.emptyList();
    }
    final List<CategoryView> allCategories = Stream.concat(
        root.getSubCategories().stream(),
        root.getProjects().isEmpty() ? Stream.empty() : Stream.of(root)
      )
      .map(category -> new CategoryView(
        category.getLocalizedNames().get(locale),
        category.getId(),
        category.getProjects().size()
      ))
      .toList();
    final AlphabeticalGroupView<CategoryView> view = new AlphabeticalGroupView<>(
      allCategories,
      CategoryView::name,
      CategoryView::projectsCount
    );
    return view.getAlphabeticalGroups();
  }

  private List<CategoryCloudView> createCategoryCloudViews(SiteBuildContext context, Locale locale) {
    final ProjectSharedData projectData = context.getSharedData(ProjectSharedData.class);
    if (projectData == null) {
      return Collections.emptyList();
    }
    final Category root = projectData.getRootProjectCategory();
    if (root == null) {
      return Collections.emptyList();
    }
    final List<WordCloudUtils.CloudItemData> tempCategories = Stream.concat(
        root.getSubCategories().stream(),
        root.getProjects().isEmpty() ? Stream.empty() : Stream.of(root)
      )
      .map(category -> new WordCloudUtils.CloudItemData(
        category.getLocalizedNames().get(locale),
        category.getId(),
        category.getProjects().size()
      ))
      .toList();
    return WordCloudUtils.buildCloud(tempCategories, (item, fontSize) ->
      new CategoryCloudView(item.name(), item.slug(), fontSize)
    );
  }
}
