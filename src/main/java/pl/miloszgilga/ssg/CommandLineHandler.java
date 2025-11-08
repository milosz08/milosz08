package pl.miloszgilga.ssg;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandLineHandler {
	private static final Logger LOG = LoggerFactory.getLogger(CommandLineHandler.class);

	private final String[] args;
	private final org.apache.commons.cli.CommandLine commandLine;

	public CommandLineHandler(String[] args) {
		this.args = args;
		commandLine = parseCommandLineArgs();
		printLoadedArguments();
	}

	public <T> T getArg(Arg option, Class<T> type) {
		return type.cast(option.hasArg
			? commandLine.getOptionValue(option.longOption)
			: commandLine.hasOption(option.longOption));
	}

	public String getArg(Arg option) {
		return getArg(option, String.class);
	}

	private CommandLine parseCommandLineArgs() {
		final Options options = new Options();
		for (final Arg option : Arg.values()) {
			options.addOption(option.createOption());
		}
		final CommandLineParser parser = new DefaultParser();
		final HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException ex) {
			formatter.printHelp("Syntax", options);
			System.exit(-1);
		}
		return cmd;
	}

	private void printLoadedArguments() {
		for (final Arg option : Arg.values()) {
			LOG.info("Loaded argument: {},{} = {}: {}",
				option.longOption, option.option, getArg(option, option.type), option.description);
		}
	}

	public enum Arg {
		DOMAIN("d", "domain", true, "Reverse-proxy domain qualifier", true, String.class),
		ANALYTICS("a", "analytics", false, "Turn on/off analytics scripts", false, Boolean.class),
		SEPARATED("s", "separated", false, "If on, always generate index.html in subdirectories", false, Boolean.class),
		;

		private final String option;
		private final String longOption;
		private final boolean hasArg;
		private final String description;
		private final boolean required;
		private final Class<?> type;

		Arg(String option, String longOption, boolean hasArg, String description, boolean required, Class<?> type) {
			this.option = option;
			this.longOption = longOption;
			this.hasArg = hasArg;
			this.description = description;
			this.required = required;
			this.type = type;
		}

		private Option createOption() {
			final Option optionInstance = new Option(option, longOption, hasArg, description);
			optionInstance.setRequired(required);
			return optionInstance;
		}
	}
}
