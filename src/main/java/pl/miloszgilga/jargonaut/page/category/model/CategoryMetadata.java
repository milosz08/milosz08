package pl.miloszgilga.jargonaut.page.category.model;

import pl.miloszgilga.jargonaut.core.parser.GenericMdDocument;

public class CategoryMetadata extends GenericMdDocument<CategoryMetadata> {
  @Override
  protected CategoryMetadata createInstance() {
    return new CategoryMetadata();
  }
}
