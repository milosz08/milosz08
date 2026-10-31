package pl.miloszgilga.jargonaut.core.template.generator;

import pl.miloszgilga.jargonaut.core.template.generator.dto.SitemapNodeView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MutableSitemapNode {
  private String label;
  private String url;
  private boolean isCurrent;
  private boolean isExpanded;
  private final Map<String, MutableSitemapNode> children = new LinkedHashMap<>();

  public MutableSitemapNode(String label, String url) {
    this.label = label;
    this.url = url;
    this.isCurrent = false;
    this.isExpanded = false;
  }

  public SitemapNodeView toView(String idPrefix) {
    final List<SitemapNodeView> childViews = new ArrayList<>();
    int idx = 0;
    for (final MutableSitemapNode child : children.values()) {
      childViews.add(child.toView(idPrefix + "_" + idx++));
    }
    return new SitemapNodeView(idPrefix, label, url, isCurrent, isExpanded, childViews);
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public boolean isCurrent() {
    return isCurrent;
  }

  public void setCurrent(boolean current) {
    isCurrent = current;
  }

  public boolean isExpanded() {
    return isExpanded;
  }

  public void setExpanded(boolean expanded) {
    isExpanded = expanded;
  }

  public Map<String, MutableSitemapNode> getChildren() {
    return children;
  }
}
