package pl.miloszgilga.jargonaut.page.tag;

import pl.miloszgilga.jargonaut.page.tag.model.TagItem;

import java.util.Comparator;

public class TagComparator implements Comparator<TagItem> {
  @Override
  public int compare(TagItem t1, TagItem t2) {
    final int countCompare = Integer.compare(t1.count(), t2.count());
    if (countCompare != 0) {
      return countCompare;
    }
    return t1.name().compareToIgnoreCase(t2.name());
  }
}
