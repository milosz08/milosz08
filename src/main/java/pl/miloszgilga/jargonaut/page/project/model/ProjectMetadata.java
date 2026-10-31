package pl.miloszgilga.jargonaut.page.project.model;

import pl.miloszgilga.jargonaut.core.parser.GenericMdDocument;

public class ProjectMetadata extends GenericMdDocument<ProjectMetadata> {
  @Override
  protected ProjectMetadata createInstance() {
    return new ProjectMetadata();
  }
}
