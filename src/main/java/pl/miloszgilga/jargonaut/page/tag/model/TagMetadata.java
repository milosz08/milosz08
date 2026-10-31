package pl.miloszgilga.jargonaut.page.tag.model;

import pl.miloszgilga.jargonaut.core.parser.GenericMdDocument;

public class TagMetadata extends GenericMdDocument<TagMetadata> {
  @Override
  protected TagMetadata createInstance() {
    return new TagMetadata();
  }
}
