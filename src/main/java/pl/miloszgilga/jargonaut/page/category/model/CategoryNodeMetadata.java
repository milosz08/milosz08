package pl.miloszgilga.jargonaut.page.category.model;

import pl.miloszgilga.jargonaut.core.parser.GenericMdDocument;

public class CategoryNodeMetadata extends GenericMdDocument<CategoryNodeMetadata> {
  @Override
  protected CategoryNodeMetadata createInstance() {
    return new CategoryNodeMetadata();
  }
}
