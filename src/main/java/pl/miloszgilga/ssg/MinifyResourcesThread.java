package pl.miloszgilga.ssg;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.miloszgilga.ssg.minifier.CssMinifier;
import pl.miloszgilga.ssg.minifier.WebMinifier;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

public class MinifyResourcesThread extends ProcessThread {
	private static final Logger LOG = LoggerFactory.getLogger(MinifyResourcesThread.class);

	private final WebMinifier cssMinifier;
	private final File cssFilesDirectory;

	MinifyResourcesThread() {
		super("MinifyThread");
		cssMinifier = new CssMinifier();
		cssFilesDirectory = new File(SsgRunner.OUTPUT_DIR + File.separator + "css");
	}

	@Override
	protected void runWithExceptionPreserve() {
		if (!cssFilesDirectory.exists() || !cssFilesDirectory.isDirectory()) {
			return;
		}
		for (final File cssFile : Objects.requireNonNullElse(cssFilesDirectory.listFiles(), new File[0])) {
			addTask(() -> minifyCssFile(cssFile));
		}
	}

	@Override
	protected int threadPoolSize() {
		return (int) Math.ceil(getCountOfProcessingContent(SsgRunner.PUBLIC_DIR + File.separator + "css") / DEF_FACTOR);
	}

	@Override
	protected List<AutoCloseable> closeables() {
		return List.of();
	}

	private void minifyCssFile(File cssFile) throws Exception {
		final String rawCssFileContent = Files.readString(cssFile.toPath());
		final String minifiedContent = cssMinifier.minify(rawCssFileContent, cssFile);
		FileUtils.writeStringToFile(cssFile, minifiedContent, StandardCharsets.UTF_8);
		LOG.info("Minify CSS file: {}.", cssFile.getPath());
	}
}
