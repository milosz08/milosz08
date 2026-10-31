package pl.miloszgilga.jargonaut.page.tag.model;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TagSharedData {
  private final Map<Locale, List<TagItem>> tagItems = new HashMap<>();

  public Map<Locale, List<TagItem>> getTagItems() {
    return tagItems;
  }
}
