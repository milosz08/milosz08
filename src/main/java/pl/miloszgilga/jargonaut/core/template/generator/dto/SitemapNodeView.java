package pl.miloszgilga.jargonaut.core.template.generator.dto;

import java.util.List;

public record SitemapNodeView(
  String id,
  String label,
  String url,
  boolean current,
  boolean expanded,
  List<SitemapNodeView> children
) {
  public boolean hasChildren() {
    return children != null && !children.isEmpty();
  }
}
