package pl.miloszgilga.jargonaut.core;

import pl.miloszgilga.jargonaut.core.processor.MergeableMetadata;

import java.util.ArrayList;
import java.util.List;

public class DataUtils {
  private DataUtils() {
  }

  public static <T extends MergeableMetadata<T>> T mergeObject(T base, T override) {
    if (base == null) {
      return override;
    }
    if (override == null) {
      return base;
    }
    return base.merge(override);
  }

  public static <T extends MergeableMetadata<T>> List<T> mergeList(List<T> base, List<T> override) {
    if (base == null) {
      return override;
    }
    if (override == null) {
      return base;
    }
    final List<T> result = new ArrayList<>();
    final int maxSize = Math.max(base.size(), override.size());
    for (int i = 0; i < maxSize; i++) {
      final T baseItem = i < base.size() ? base.get(i) : null;
      final T overrideItem = i < override.size() ? override.get(i) : null;
      if (baseItem == null) {
        result.add(overrideItem);
      } else if (overrideItem == null) {
        result.add(baseItem);
      } else {
        result.add(baseItem.merge(overrideItem));
      }
    }
    return result;
  }

  public static <T> T mergeValues(T base, T override) {
    if (override != null) {
      return override;
    }
    return base;
  }
}
