package pl.miloszgilga.jargonaut.core.extractor.stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record LanguageStat(String name, long bytes, String color, double percentage) {
  private static final String DEFAULT_COLOR = "#CCCCCC";

  public LanguageStat {
    if (color == null || color.isBlank()) {
      color = DEFAULT_COLOR;
    }
  }

  public LanguageStat(String name, long bytes, String color) {
    this(name, bytes, color, 0.0);
  }

  public LanguageStat merge(LanguageStat other) {
    final String resolvedColor = !DEFAULT_COLOR.equals(color) ? color : other.color;
    return new LanguageStat(name, bytes + other.bytes, resolvedColor, 0.0);
  }

  public static List<LanguageStat> aggregate(List<List<LanguageStat>> listsOfStats) {
    final Map<String, LanguageStat> merged = new HashMap<>();
    for (final List<LanguageStat> stats : listsOfStats) {
      for (final LanguageStat stat : stats) {
        merged.merge(stat.name(), stat, LanguageStat::merge);
      }
    }
    return calculateExactPercentages(new ArrayList<>(merged.values()));
  }

  // hare-niemeyer algorithm
  public static List<LanguageStat> calculateExactPercentages(List<LanguageStat> rawStats) {
    if (rawStats.isEmpty()) {
      return rawStats;
    }
    final long totalBytes = rawStats.stream().mapToLong(LanguageStat::bytes).sum();
    if (totalBytes == 0) {
      return rawStats;
    }
    final int targetSum = 1000;
    int currentSum = 0;
    final List<StatWrapper> wrappers = new ArrayList<>();
    for (LanguageStat stat : rawStats) {
      final double exactPercentage = (stat.bytes() * targetSum) / (double) totalBytes;
      final int floorPercentage = (int) Math.floor(exactPercentage);
      final StatWrapper wrapper = new StatWrapper();
      wrapper.stat = stat;
      wrapper.rounded = floorPercentage;
      wrapper.remainder = exactPercentage - floorPercentage;
      wrappers.add(wrapper);
      currentSum += floorPercentage;
    }
    final int deficit = targetSum - currentSum;
    wrappers.sort((a, b) -> Double.compare(b.remainder, a.remainder));
    for (int i = 0; i < deficit && i < wrappers.size(); i++) {
      wrappers.get(i).rounded += 1;
    }
    return wrappers.stream()
      .filter(w -> w.rounded > 0)
      .sorted((a, b) -> Long.compare(b.stat.bytes(), a.stat.bytes()))
      .map(w -> new LanguageStat(w.stat.name(), w.stat.bytes(), w.stat.color(), w.rounded / 10.0))
      .toList();
  }

  private static class StatWrapper {
    LanguageStat stat;
    int rounded;
    double remainder;
  }
}
