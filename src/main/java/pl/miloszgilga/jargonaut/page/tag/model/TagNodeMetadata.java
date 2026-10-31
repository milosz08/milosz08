package pl.miloszgilga.jargonaut.page.tag.model;

import pl.miloszgilga.jargonaut.core.parser.GenericMdDocument;

public class TagNodeMetadata extends GenericMdDocument<TagNodeMetadata> {
  @Override
  protected TagNodeMetadata createInstance() {
    return new TagNodeMetadata();
  }
}
