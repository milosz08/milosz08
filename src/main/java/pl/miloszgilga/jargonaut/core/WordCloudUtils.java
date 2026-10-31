package pl.miloszgilga.jargonaut.core;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class WordCloudUtils {
  private static final double MIN_FONT_SIZE = 0.8;
  private static final double MAX_FONT_SIZE = 3.0;
  private static final double MAX_MOBILE_FONT_SIZE = 1.5;

  private WordCloudUtils() {
  }

  public static <V> List<V> buildCloud(List<CloudItemData> rawItems, CloudItemMapper<V> mapper) {
    if (rawItems == null || rawItems.isEmpty()) {
      return Collections.emptyList();
    }
    final int minCount = rawItems.stream().mapToInt(CloudItemData::count).min().orElse(1);
    final int maxCount = rawItems.stream().mapToInt(CloudItemData::count).max().orElse(1);
    return rawItems.stream()
      .sorted(Comparator.comparing(CloudItemData::name, String.CASE_INSENSITIVE_ORDER))
      .map(item -> {
        final String fontSizeCss = generateClampFontSize(item.count(), minCount, maxCount);
        return mapper.map(item, fontSizeCss);
      })
      .toList();
  }

  private static String generateClampFontSize(int count, int min, int max) {
    final double weight = (min == max) ? 0.5 : (double) (count - min) / (max - min);
    final double minSizeRem = MIN_FONT_SIZE + weight * (MAX_MOBILE_FONT_SIZE - MIN_FONT_SIZE);
    final double maxSizeRem = MIN_FONT_SIZE + weight * (MAX_FONT_SIZE - MIN_FONT_SIZE);
    final double fluidVw = 1.5 + (weight * 4.0);
    return String.format(Locale.US, "clamp(%.2frem, %.2fvw, %.2frem)", minSizeRem, fluidVw, maxSizeRem);
  }

  public record CloudItemData(String name, String slug, int count) {
  }
}
