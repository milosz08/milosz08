package pl.miloszgilga.jargonaut.page.education;

import pl.miloszgilga.jargonaut.page.education.model.DateNode;

import java.util.Comparator;

public class TimelineItemComparator implements Comparator<TimelineItem> {
  @Override
  public int compare(TimelineItem item1, TimelineItem item2) {
    final int endCompare = compareDatesDesc(item1.getEnd(), item2.getEnd());
    if (endCompare != 0) {
      return endCompare;
    }
    return compareDatesDesc(item1.getStart(), item2.getStart());
  }

  private int compareDatesDesc(DateNode d1, DateNode d2) {
    if (d1 == null && d2 == null) {
      return 0;
    }
    if (d1 == null) {
      return -1;
    }
    if (d2 == null) {
      return 1;
    }
    final int y1 = d1.getYear() != null ? d1.getYear() : 0;
    final int y2 = d2.getYear() != null ? d2.getYear() : 0;
    if (y1 != y2) {
      return Integer.compare(y2, y1);
    }
    final int m1 = d1.getMonth() != null ? d1.getMonth() : 0;
    final int m2 = d2.getMonth() != null ? d2.getMonth() : 0;
    return Integer.compare(m2, m1);
  }
}
