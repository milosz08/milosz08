package pl.miloszgilga.jargonaut.config;

import pl.miloszgilga.jargonaut.config.model.Config;
import pl.miloszgilga.jargonaut.core.GenerationHaltedException;
import pl.miloszgilga.jargonaut.core.parser.yaml.YamlParser;
import pl.miloszgilga.jargonaut.core.processor.ResourceCheckerProcessor;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigProcessor extends ResourceCheckerProcessor {
  private static final Pattern ENV_PATTERN = Pattern.compile("\\$\\{env:([A-Za-z0-9_]+)}");

  private final YamlParser yamlParser;
  private final Dotenv dotenv;

  public ConfigProcessor(Path configFile, YamlParser yamlParser) {
    super(configFile);
    this.yamlParser = yamlParser;
    dotenv = Dotenv.configure().ignoreIfMissing().load();
  }

  @Override
  protected void processAfterCheck(SiteBuildContext context) {
    try {
      final String rawYamlContent = Files.readString(resourcePath);
      final String substitutedYaml = substituteEnvVariables(rawYamlContent);
      context.setConfig(yamlParser.parseAndValidateYamlString(substitutedYaml, Config.class));
    } catch (IOException ex) {
      throw new GenerationHaltedException(ex);
    }
  }

  @Override
  protected boolean isBaseResourceExists() {
    return Files.exists(resourcePath);
  }

  private String substituteEnvVariables(String content) {
    final Matcher matcher = ENV_PATTERN.matcher(content);
    final StringBuilder result = new StringBuilder();
    while (matcher.find()) {
      final String envKey = matcher.group(1);
      final String envValue = dotenv.get(envKey);
      if (envValue == null) {
        throw new GenerationHaltedException("Missing environment variable: '" + envKey + "' declared in file: " +
          resourcePath);
      }
      matcher.appendReplacement(result, Matcher.quoteReplacement(envValue));
    }
    matcher.appendTail(result);
    return result.toString();
  }
}
