/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SsgRunner {
	public static final String OUTPUT_DIR = "output";
	private static final Logger LOG = LoggerFactory.getLogger(SsgRunner.class);

	public static void main(String[] args) {
		new SsgRunner().start(args);
	}

	private void start(String[] args) {
		long startTime = System.currentTimeMillis();

		final CommandLineHandler commandLineHandler = new CommandLineHandler(args);
		final CopyResourcesThread copyResourcesThread = new CopyResourcesThread();
		final ResourcesGeneratorThread resourcesGeneratorThread = new ResourcesGeneratorThread(commandLineHandler);
		try {
			copyResourcesThread.startAndJoin();
			resourcesGeneratorThread.startAndJoin();

			LOG.info("Finished with no errors. Ended up with {}s.", (System.currentTimeMillis() - startTime) / 1000.0);
		} catch (InterruptedException ex) {
			LOG.error("Interrupted thread. Cause: {}", ex.getMessage());
			System.exit(-1);
		}
	}
}
