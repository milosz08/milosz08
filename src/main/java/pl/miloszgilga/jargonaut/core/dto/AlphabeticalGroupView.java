package pl.miloszgilga.jargonaut.core.dto;

import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class AlphabeticalGroupView<T> {
  private final List<AlphabeticalGroup<T>> alphabeticalGroups;

  public AlphabeticalGroupView(List<T> items, Function<T, String> nameExtractor, ToIntFunction<T> countExtractor) {
    alphabeticalGroups = items.stream()
      .sorted(Comparator.comparing(nameExtractor, String.CASE_INSENSITIVE_ORDER))
      .collect(Collectors.groupingBy(
        item -> getGroupKey(nameExtractor.apply(item)),
        TreeMap::new,
        Collectors.toList()
      ))
      .entrySet().stream()
      .map(entry -> {
        final int aggregatedCount = entry.getValue().stream()
          .mapToInt(countExtractor)
          .sum();
        return new AlphabeticalGroup<>(entry.getKey(), aggregatedCount, entry.getValue());
      })
      .toList();
  }

  public List<AlphabeticalGroup<T>> getAlphabeticalGroups() {
    return alphabeticalGroups;
  }

  private String getGroupKey(String name) {
    if (name == null || name.isBlank()) {
      return "#";
    }
    final char firstChar = name.trim().toUpperCase().charAt(0);
    return Character.isLetter(firstChar) ? String.valueOf(firstChar) : "#";
  }
}
