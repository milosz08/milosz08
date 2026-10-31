package pl.miloszgilga.jargonaut.page.education;

import pl.miloszgilga.jargonaut.page.education.model.DateNode;

public interface TimelineItem {
  DateNode getStart();

  DateNode getEnd();
}
