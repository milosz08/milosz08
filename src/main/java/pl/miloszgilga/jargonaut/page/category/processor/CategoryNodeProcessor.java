package pl.miloszgilga.jargonaut.page.category.processor;

import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.processor.ProcessorContext;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.processor.md.DynamicPageData;
import pl.miloszgilga.jargonaut.core.processor.md.DynamicPageMergingMdProcessor;
import pl.miloszgilga.jargonaut.core.template.generator.GeneratorContext;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlGenerator;
import pl.miloszgilga.jargonaut.page.category.generator.CategoryNodeHtmlGenerator;
import pl.miloszgilga.jargonaut.page.category.model.CategoryNodeMetadata;
import pl.miloszgilga.jargonaut.page.project.model.Category;
import pl.miloszgilga.jargonaut.page.project.model.ProjectSharedData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CategoryNodeProcessor extends DynamicPageMergingMdProcessor<CategoryNodeMetadata> {
  public CategoryNodeProcessor(ProcessorContext processorContext) {
    super("category", processorContext);
  }

  @Override
  protected List<DynamicPageData> provideDynamicValues(SiteBuildContext context, Locale locale) {
    final ProjectSharedData projectData = context.computeSharedDataIfAbsent(
      ProjectSharedData.class,
      ProjectSharedData::new
    );
    final Category rootCategory = projectData.getRootProjectCategory();
    final List<DynamicPageData> dynamicValues = new ArrayList<>();
    if (rootCategory == null) {
      return dynamicValues;
    }
    for (final Category category : rootCategory.getSubCategories()) {
      if (category.getProjects().isEmpty()) {
        continue;
      }
      final String name = getCategoryName(category, locale);
      dynamicValues.add(new DynamicPageData(category.getId(), name));
    }
    if (!rootCategory.getProjects().isEmpty()) {
      final String name = getCategoryName(rootCategory, locale);
      dynamicValues.add(new DynamicPageData(rootCategory.getId(), name));
    }
    return dynamicValues;
  }

  @Override
  protected HtmlGenerator<CategoryNodeMetadata> createHtmlGenerator(PageRepresentation<CategoryNodeMetadata> page,
                                                                    GeneratorContext context,
                                                                    String slug) {
    return new CategoryNodeHtmlGenerator(page, context, slug);
  }

  @Override
  protected Class<CategoryNodeMetadata> getTargetClass() {
    return CategoryNodeMetadata.class;
  }

  @Override
  public int order() {
    return HIGHEST_PRIORITY - 50;
  }

  private String getCategoryName(Category category, Locale locale) {
    return category.getLocalizedNames().get(locale);
  }
}
