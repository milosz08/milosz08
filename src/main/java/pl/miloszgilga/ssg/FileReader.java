/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FileReader {
	private final String resourcePath;
	private final URL resourceUrl;
	private final FileSystemManager fileSystemManager;

	public FileReader(String resourcePath) {
		this.resourcePath = prepareResourcePath(resourcePath);
		this.resourceUrl = getClass().getResource(this.resourcePath);
		fileSystemManager = createFileSystemManager();
	}

	public String readFileToRawStr() {
		try (final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath.substring(1))) {
			return IOUtils.toString(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8);
		} catch (IOException ex) {
			throw new InoperableException(getClass(), "Raw classpath file processing error. Cause: %s.", ex.getMessage());
		}
	}

	public int getCountOfFilesInPath() {
		return getFileNamesInPath().size();
	}

	public List<String> getFileNamesInPath() {
		try {
			final FileObject file = fileSystemManager.resolveFile(resourceUrl);
			if (file.isFile()) {
				throw new IOException("Path represents a file. Expected directory.");
			}
			return Arrays.stream(file.getChildren())
				.map(this::createNameWithoutExtension)
				.toList();
		} catch (IOException ex) {
			throw new InoperableException(getClass(), "Unable to get list of files from path: %s. Cause: %s.",
				resourcePath, ex.getMessage());
		}
	}

	public String getFileName(boolean omitExtension) {
		try {
			final FileObject file = fileSystemManager.resolveFile(resourceUrl);
			final FileName name = file.getName();
			String fileName = name.getBaseName();
			if (omitExtension) {
				fileName = createNameWithoutExtension(file);
			}
			file.close();
			return fileName;
		} catch (IOException ex) {
			throw new InoperableException(getClass(), "Unable to get file name from path: %s. Cause: %s.",
				resourcePath, ex.getMessage());
		}
	}

	private FileSystemManager createFileSystemManager() {
		try {
			return VFS.getManager();
		} catch (FileSystemException ex) {
			throw new InoperableException(getClass(), "Unable to create VFS Manager. Cause: %s.", ex.getMessage());
		}
	}

	private String prepareResourcePath(String resourcePath) {
		if (!resourcePath.startsWith("/")) {
			return "/" + resourcePath;
		}
		return resourcePath;
	}

	private String createNameWithoutExtension(FileObject fileObject) {
		final FileName name = fileObject.getName();
		return name.getBaseName().replace("." + name.getExtension(), StringUtils.EMPTY);
	}
}
