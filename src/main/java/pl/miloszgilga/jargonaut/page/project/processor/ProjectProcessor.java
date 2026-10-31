package pl.miloszgilga.jargonaut.page.project.processor;

import pl.miloszgilga.jargonaut.core.processor.ProcessorContext;
import pl.miloszgilga.jargonaut.core.processor.md.PageMergingMdProcessor;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlGeneratorFactory;
import pl.miloszgilga.jargonaut.page.project.generator.ProjectHtmlGenerator;
import pl.miloszgilga.jargonaut.page.project.model.ProjectMetadata;

public class ProjectProcessor extends PageMergingMdProcessor<ProjectMetadata> {
  public ProjectProcessor(ProcessorContext processorContext) {
    super("project", processorContext);
  }

  @Override
  protected Class<ProjectMetadata> getTargetClass() {
    return ProjectMetadata.class;
  }

  @Override
  protected HtmlGeneratorFactory<ProjectMetadata> getHtmlRendererFactory() {
    return ProjectHtmlGenerator::new;
  }

  @Override
  public int order() {
    return HIGHEST_PRIORITY - 10;
  }
}
