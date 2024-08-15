/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.miloszgilga.ssg.minifier.CssMinifier;
import pl.miloszgilga.ssg.minifier.WebMinifier;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

class CopyResourcesThread extends ProcessThread {
	private static final Logger LOG = LoggerFactory.getLogger(CopyResourcesThread.class);
	private static final String PUBLIC_DIR = "/public";

	private final WebMinifier cssMinifier;
	private final File outputDirectory;
	private final File cssFilesDirectory;
	private final StandardFileSystemManager fsManager;

	CopyResourcesThread() {
		super("CopyThread");
		cssMinifier = new CssMinifier();
		outputDirectory = new File(SsgRunner.OUTPUT_DIR);
		cssFilesDirectory = new File(SsgRunner.OUTPUT_DIR + File.separator + "css");
		fsManager = new StandardFileSystemManager();
	}

	@Override
	protected void runWithExceptionPreserve() throws Exception {
		prepareDir();
		fsManager.init();
		final URL resourceUrl = getClass().getResource(PUBLIC_DIR);
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
		addTask(this::minifyCssFiles);
	}

	@Override
	protected int threadPoolSize() {
		return (int) Math.ceil(getCountOfProcessingContent(PUBLIC_DIR) / DEF_FACTOR);
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
		LOG.info("Copy file: {}.", PUBLIC_DIR + StringUtils.substringAfter(input.getName().getPath(), PUBLIC_DIR));
	}

	private void minifyCssFiles() throws Exception {
		if (cssFilesDirectory.exists() && cssFilesDirectory.isDirectory()) {
			for (final File cssFile : Objects.requireNonNullElse(cssFilesDirectory.listFiles(), new File[0])) {
				final String rawCssFileContent = Files.readString(cssFile.toPath());
				final String minifiedContent = cssMinifier.minify(rawCssFileContent, cssFile);
				FileUtils.writeStringToFile(cssFile, minifiedContent, StandardCharsets.UTF_8);
			}
		}
	}
}
