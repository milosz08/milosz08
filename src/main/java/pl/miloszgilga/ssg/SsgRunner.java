package pl.miloszgilga.ssg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SsgRunner {
	public static final String OUTPUT_DIR = "output";
	public static final String PUBLIC_DIR = "/public";
	private static final Logger LOG = LoggerFactory.getLogger(SsgRunner.class);

	public static void main(String[] args) {
		final long startTime = System.currentTimeMillis();

		final CommandLineHandler commandLineHandler = new CommandLineHandler(args);
		final CopyResourcesThread copyResourcesThread = new CopyResourcesThread();
		final MinifyResourcesThread minifyResourcesThread = new MinifyResourcesThread();
		final ResourcesGeneratorThread resourcesGeneratorThread = new ResourcesGeneratorThread(commandLineHandler);
		try {
			copyResourcesThread.startAndJoin();
			minifyResourcesThread.startAndJoin();
			resourcesGeneratorThread.startAndJoin();

			final double endTime = (System.currentTimeMillis() - startTime) / 1000.0;
			LOG.info("Finished with no errors. Ended up with {}s.", endTime);
		} catch (InterruptedException ex) {
			LOG.error("Interrupted thread. Cause: {}", ex.getMessage());
			System.exit(-1);
		}
	}
}
