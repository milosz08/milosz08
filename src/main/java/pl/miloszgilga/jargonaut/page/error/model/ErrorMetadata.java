package pl.miloszgilga.jargonaut.page.error.model;

import pl.miloszgilga.jargonaut.core.parser.GenericMdDocument;

public class ErrorMetadata extends GenericMdDocument<ErrorMetadata> {
  @Override
  protected ErrorMetadata createInstance() {
    return new ErrorMetadata();
  }
}
