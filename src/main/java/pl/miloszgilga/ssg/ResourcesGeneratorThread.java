package pl.miloszgilga.ssg;

import org.apache.commons.io.FileUtils;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.miloszgilga.ssg.page.AbstractPageProcessor;
import pl.miloszgilga.ssg.page.ProcesssedPage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class ResourcesGeneratorThread extends ProcessThread {
	private static final Logger LOG = LoggerFactory.getLogger(ResourcesGeneratorThread.class);
	private static final String SCANNING_PACKAGE = "pl.miloszgilga.ssg.page";

	private final CommandLineHandler commandLineHandler;

	ResourcesGeneratorThread(CommandLineHandler commandLineHandler) {
		super("GenThread");
		this.commandLineHandler = commandLineHandler;
	}

	@Override
	protected void runWithExceptionPreserve() throws Exception {
		for (final AbstractPageProcessor pageDeclaration : dynamicallyLoadPages()) {
			final List<ProcesssedPage> pages = pageDeclaration.processPages();
			for (final ProcesssedPage page : pages) {
				addTask(() -> appendFile(page.qualifiedName(), page.rawHtmlContent()));
			}
		}
	}

	@Override
	protected int threadPoolSize() {
		final String[] possibleProcessingsDirs = {"/layouts", "/markdown", "/templates"};
		int totalFilesCount = 0;
		for (final String possibleProcessingsDir : possibleProcessingsDirs) {
			totalFilesCount += getCountOfProcessingContent(possibleProcessingsDir);
		}
		return (int) Math.ceil(totalFilesCount / DEF_FACTOR);
	}

	@Override
	protected List<AutoCloseable> closeables() {
		return List.of();
	}

	private List<AbstractPageProcessor> dynamicallyLoadPages() throws Exception {
		final List<AbstractPageProcessor> loadedPages = new ArrayList<>();

		final Reflections reflections = new Reflections(SCANNING_PACKAGE, Scanners.SubTypes);
		final Set<Class<? extends AbstractPageProcessor>> pagesClazzes = reflections
			.getSubTypesOf(AbstractPageProcessor.class);

		for (final Class<? extends AbstractPageProcessor> clazz : pagesClazzes) {
			final Constructor<? extends AbstractPageProcessor> ctor = clazz.getConstructor(CommandLineHandler.class);
			loadedPages.add(ctor.newInstance(commandLineHandler));
			LOG.info("Loaded and instantiated: {}.", clazz.getName());
		}
		return loadedPages;
	}

	public void appendFile(String filePath, String content) throws Exception {
		final File generatedFile = new File(SsgRunner.OUTPUT_DIR + File.separator + filePath);
		final File parentDir = generatedFile.getParentFile();
		if (parentDir != null && !parentDir.exists()) {
			if (!parentDir.mkdirs()) {
				throw new IOException("Unable to create directories");
			}
		}
		FileUtils.writeStringToFile(generatedFile, content, "UTF-8");
		LOG.info("Generated file: {}.", filePath);
	}
}
