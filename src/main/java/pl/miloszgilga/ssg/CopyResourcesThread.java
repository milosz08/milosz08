package pl.miloszgilga.ssg;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

class CopyResourcesThread extends ProcessThread {
	private static final Logger LOG = LoggerFactory.getLogger(CopyResourcesThread.class);

	private final File outputDirectory;
	private final StandardFileSystemManager fsManager;

	CopyResourcesThread() {
		super("CopyThread");
		outputDirectory = new File(SsgRunner.OUTPUT_DIR);
		fsManager = new StandardFileSystemManager();
	}

	@Override
	protected void runWithExceptionPreserve() throws Exception {
		prepareDir();
		fsManager.init();
		final URL resourceUrl = getClass().getResource(SsgRunner.PUBLIC_DIR);
		final FileObject srcDir = fsManager.resolveFile(resourceUrl);
		if (!outputDirectory.exists()) {
			return;
		}
		final Deque<FileObject> deque = new ArrayDeque<>();
		deque.push(srcDir);
		while (!deque.isEmpty()) {
			final FileObject currentDir = deque.pop();
			for (final FileObject input : currentDir.getChildren()) {
				if (input.isFile()) {
					addTask(() -> copyFile(input, srcDir));
				} else if (input.isFolder()) {
					deque.push(input);
				}
			}
			currentDir.close();
		}
	}

	@Override
	protected int threadPoolSize() {
		return (int) Math.ceil(getCountOfProcessingContent(SsgRunner.PUBLIC_DIR) / DEF_FACTOR);
	}

	@Override
	protected List<AutoCloseable> closeables() {
		return List.of(fsManager);
	}

	private void prepareDir() throws Exception {
		if (outputDirectory.exists()) {
			FileUtils.cleanDirectory(outputDirectory);
		}
		if (outputDirectory.mkdirs()) {
			LOG.info("Init result directory");
		}
	}

	private void copyFile(FileObject input, FileObject output) throws Exception {
		final String relativePath = input.getName().getPath().substring(output.getName().getPath().length());
		final File destFile = new File(outputDirectory, relativePath);
		final FileObject destFileObject = fsManager.resolveFile(destFile.getAbsolutePath());
		destFileObject.copyFrom(input, Selectors.SELECT_SELF);
		LOG.info("Copy file: {}.", SsgRunner.PUBLIC_DIR + StringUtils.substringAfter(input.getName().getPath(),
			SsgRunner.PUBLIC_DIR));
	}
}
