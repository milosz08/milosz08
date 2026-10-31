package pl.miloszgilga.jargonaut.asset.resolver;

import pl.miloszgilga.jargonaut.asset.AssetTarget;
import pl.miloszgilga.jargonaut.core.GenerationHaltedException;
import pl.miloszgilga.jargonaut.core.event.BuildEvent;
import pl.miloszgilga.jargonaut.core.event.BuildEventBus;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AssetAliasResolver {
  private static final Map<String, String> DEFAULT_TARGET_PATTERNS = Map.of(
    "src", "\\s*=\\s*[\"']",
    "href", "\\s*=\\s*[\"']",
    "content", "\\s*=\\s*[\"']",
    "poster", "\\s*=\\s*[\"']",
    "data", "\\s*=\\s*[\"']",
    "action", "\\s*=\\s*[\"']",
    "url", "\\(\\s*[\"']?"
  );

  private final Path baseAssetsDir;
  private final Map<String, String> targetPatterns;
  private final List<AssetResolutionStrategy> strategies;
  private final Pattern assetPattern;

  public AssetAliasResolver(Path baseAssetsDir,
                            List<AssetResolutionStrategy> strategies,
                            Map<String, String> targetPatterns,
                            BuildEventBus eventBus) {
    this.baseAssetsDir = baseAssetsDir;
    this.strategies = strategies;
    for (final AssetResolutionStrategy strategy : strategies) {
      eventBus.publish(BuildEvent.spinnerDetail(
        "Registering assets alias resolver: " + strategy.getClass().getSimpleName() +
          " (prefix: " + strategy.getPrefix() + ")"
      ));
    }
    this.targetPatterns = new LinkedHashMap<>(DEFAULT_TARGET_PATTERNS);
    if (targetPatterns != null && !targetPatterns.isEmpty()) {
      this.targetPatterns.putAll(targetPatterns);
    }
    assetPattern = createAssetPattern();
  }

  public AssetAliasResolver(Path baseAssetsDir, List<AssetResolutionStrategy> strategies, BuildEventBus eventBus) {
    this(baseAssetsDir, strategies, null, eventBus);
  }

  public String resolveAliases(String content, Path resourcePath, Map<Path, AssetTarget> assetTarget) {
    if (content == null || content.isEmpty()) {
      return content;
    }
    final Matcher matcher = assetPattern.matcher(content);
    final StringBuilder sb = new StringBuilder();
    while (matcher.find()) {
      final String prefix = matcher.group(1);
      final String aliasPath = matcher.group(2);
      final String suffix = matcher.group(3);

      final AssetResolutionStrategy strategy = strategies.stream()
        .filter(s -> s.supports(aliasPath))
        .findFirst()
        .orElse(null);

      AssetTarget target = null;
      if (strategy != null) {
        final Path key = strategy.resolveKey(aliasPath, resourcePath, baseAssetsDir);
        if (key != null) {
          target = assetTarget.get(key);
        }
      }
      if (target == null) {
        throw new GenerationHaltedException(
          String.format("Resource '%s' was not found in the asset manifest, " +
            "verify the path and ensure the file exists.", aliasPath)
        );
      }
      final String hashedWebPath = target.fullPath();
      final String replacementPath = strategy.formatReplacementPath(hashedWebPath);

      final String replacement = prefix + replacementPath + suffix;
      matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
    }
    matcher.appendTail(sb);
    return sb.toString();
  }

  private Pattern createAssetPattern() {
    final String group1 = targetPatterns.entrySet().stream()
      .map(entry -> "(?:" + Pattern.quote(entry.getKey()) + entry.getValue() + ")")
      .collect(Collectors.joining("|", "(", ")"));

    final String prefixesRegex = strategies.stream()
      .map(s -> Pattern.quote(s.getPrefix()))
      .sorted((a, b) -> Integer.compare(b.length(), a.length()))
      .collect(Collectors.joining("|", "(?:", ")"));

    final String group2 = "(" + prefixesRegex + "[^\"')\\s]+)";
    final String group3 = "([\"']?\\s*\\)|[\"'])";

    return Pattern.compile(group1 + group2 + group3, Pattern.CASE_INSENSITIVE);
  }
}
