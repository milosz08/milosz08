package pl.miloszgilga.jargonaut.core.processor;

import java.util.Comparator;

public interface TaskProcessor {
  int HIGHEST_PRIORITY = Integer.MAX_VALUE;
  Comparator<TaskProcessor> COMPARATOR = Comparator.comparingInt(TaskProcessor::order).reversed();

  void process(SiteBuildContext context);

  // lower = started after
  default int order() {
    return HIGHEST_PRIORITY;
  }
}
