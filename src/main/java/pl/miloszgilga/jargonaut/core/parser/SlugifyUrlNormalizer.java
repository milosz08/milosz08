package pl.miloszgilga.jargonaut.core.parser;

import com.github.slugify.Slugify;

import pl.miloszgilga.jargonaut.core.event.BuildEvent;
import pl.miloszgilga.jargonaut.core.event.BuildEventBus;

import java.util.Map;

public class SlugifyUrlNormalizer implements UrlNormalizer {
  private static final Map<String, String> DEFAULT_REPLACEMENTS = Map.ofEntries(
    Map.entry("+", "plus"),
    Map.entry("#", "sharp"),
    Map.entry(".", "dot"),
    Map.entry("*", "star"),
    Map.entry("&", "and"),
    Map.entry("@", "at"),
    Map.entry("$", "dollar"),
    Map.entry("%", "percent"),
    Map.entry("=", "equals"),
    Map.entry("!", "bang"),
    Map.entry("?", "question"),
    Map.entry("<", "less"),
    Map.entry(">", "greater")
  );

  private final Slugify slugify;

  public SlugifyUrlNormalizer(Map<String, String> additionalReplacements, BuildEventBus eventBus) {
    final Slugify.SlugifyBuilder builder = Slugify.builder();
    for (final Map.Entry<String, String> entry : DEFAULT_REPLACEMENTS.entrySet()) {
      builder.customReplacement(entry.getKey(), entry.getValue());
    }
    int allReplacementsCount = DEFAULT_REPLACEMENTS.size();
    if (additionalReplacements != null && !additionalReplacements.isEmpty()) {
      for (Map.Entry<String, String> entry : additionalReplacements.entrySet()) {
        builder.customReplacement(entry.getKey(), entry.getValue());
      }
      allReplacementsCount += additionalReplacements.size();
    }
    eventBus.publish(BuildEvent.spinnerDetail("Registering URL normalizer: " + getClass().getSimpleName() + " with "
      + allReplacementsCount + " custom replacements"));
    slugify = builder.build();
  }

  public SlugifyUrlNormalizer(BuildEventBus eventBus) {
    this(null, eventBus);
  }

  @Override
  public String normalize(String url) {
    return slugify.slugify(url);
  }
}
