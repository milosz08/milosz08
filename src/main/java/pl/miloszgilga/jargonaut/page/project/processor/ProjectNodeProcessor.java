package pl.miloszgilga.jargonaut.page.project.processor;

import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.processor.ProcessorContext;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.processor.md.PageMergingMdProcessor;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlGeneratorFactory;
import pl.miloszgilga.jargonaut.page.project.generator.ProjectNodeHtmlGenerator;
import pl.miloszgilga.jargonaut.page.project.model.Category;
import pl.miloszgilga.jargonaut.page.project.model.ProjectNodeMetadata;

import java.nio.file.Path;
import java.util.List;

public class ProjectNodeProcessor extends PageMergingMdProcessor<ProjectNodeMetadata> {
  private final Category parentCategory;

  public ProjectNodeProcessor(Path subDir, Category parentCategory, ProcessorContext processorContext) {
    super(processorContext.getBaseSitesDir().relativize(subDir).toString(), processorContext);
    this.parentCategory = parentCategory;
  }

  @Override
  protected void persistPageRepresentation(SiteBuildContext context,
                                           PageRepresentation<ProjectNodeMetadata> page) {
    parentCategory.getProjects().add(page);
  }

  @Override
  protected Class<ProjectNodeMetadata> getTargetClass() {
    return ProjectNodeMetadata.class;
  }

  @Override
  protected HtmlGeneratorFactory<ProjectNodeMetadata> getHtmlRendererFactory() {
    return ProjectNodeHtmlGenerator::new;
  }

  @Override
  protected List<String> loadAdditionalAssets() {
    return List.of("gallery");
  }
}
