package pl.miloszgilga.jargonaut.page.privacypolicy.model;

import pl.miloszgilga.jargonaut.core.parser.GenericMdDocument;

public class PrivacyPolicyMetadata extends GenericMdDocument<PrivacyPolicyMetadata> {
  @Override
  protected PrivacyPolicyMetadata createInstance() {
    return new PrivacyPolicyMetadata();
  }
}
