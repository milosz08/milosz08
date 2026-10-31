package pl.miloszgilga.jargonaut.core.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.help.HelpFormatter;

import pl.miloszgilga.jargonaut.core.CastUtils;
import pl.miloszgilga.jargonaut.core.GenerationHaltedException;

import java.io.IOException;

public class CliArgumentsParser {
  private final String[] args;
  private final CommandLine commandLine;

  public CliArgumentsParser(String[] args) {
    this.args = args;
    commandLine = parseCommandLineArgs();
  }

  public <T> T getArg(Arg option) {
    try {
      if (option.hasArg) {
        return CastUtils.unsafeCast(commandLine.getParsedOptionValue(option.longOption, option.defaultValue));
      }
      return CastUtils.unsafeCast(commandLine.hasOption(option.longOption));
    } catch (ParseException ex) {
      throw new RuntimeException(ex);
    }
  }

  private CommandLine parseCommandLineArgs() {
    final Options options = new Options();
    for (final Arg option : Arg.values()) {
      options.addOption(option.createOption());
    }
    final CommandLineParser parser = new DefaultParser();
    final HelpFormatter formatter = HelpFormatter.builder().get();
    CommandLine cmd = null;
    try {
      cmd = parser.parse(options, args);
    } catch (ParseException ex) {
      try {
        formatter.printOptions(options);
      } catch (IOException exc) {
        throw new GenerationHaltedException(exc);
      }
    }
    return cmd;
  }

  public enum Arg {
    INPUT("i", "input", "Processing input dir (default: 'content')", false, "content", true, String.class),
    VERBOSE("v", "verbose", "Enable verbose logging (default: false)"),
    ;

    private final String option;
    private final String longOption;
    private final String description;
    private final boolean required;
    private final Object defaultValue;
    private final boolean hasArg;
    private final Class<?> type;

    Arg(String option,
        String longOption,
        String description,
        boolean required,
        Object defaultValue,
        boolean hasArg,
        Class<?> type) {
      this.option = option;
      this.longOption = longOption;
      this.description = description;
      this.required = required;
      this.defaultValue = defaultValue;
      this.hasArg = hasArg;
      this.type = type;
    }

    Arg(String option, String longOption, String description) {
      this(option, longOption, description, false, false, false, Boolean.class);
    }

    private Option createOption() {
      final Option optionInstance = new Option(option, longOption, hasArg, description);
      optionInstance.setRequired(required);
      optionInstance.setType(type);
      return optionInstance;
    }
  }
}
