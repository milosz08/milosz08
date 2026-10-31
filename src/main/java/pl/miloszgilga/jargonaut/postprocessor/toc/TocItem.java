package pl.miloszgilga.jargonaut.postprocessor.toc;

import java.util.ArrayList;
import java.util.List;

public class TocItem {
  private final String id;
  private final String text;
  private final List<TocItem> children = new ArrayList<>();

  public TocItem(String id, String text) {
    this.id = id;
    this.text = text;
  }

  public String getId() {
    return id;
  }

  public String getText() {
    return text;
  }

  public List<TocItem> getChildren() {
    return children;
  }
}
