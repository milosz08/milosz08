package pl.miloszgilga.jargonaut.page.home.model;

import pl.miloszgilga.jargonaut.core.DataUtils;
import pl.miloszgilga.jargonaut.core.parser.GenericMdDocument;
import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;

import java.util.List;

public class HomeMetadata extends GenericMdDocument<HomeMetadata> {
  @YamlRequired
  private List<String> contributionsGraph;

  @Override
  protected void mergeAdditionalFields(HomeMetadata merged, HomeMetadata override) {
    merged.contributionsGraph = DataUtils.mergeValues(contributionsGraph, override.contributionsGraph);
  }

  @Override
  protected HomeMetadata createInstance() {
    return new HomeMetadata();
  }

  public List<String> getContributionsGraph() {
    return contributionsGraph;
  }
}
