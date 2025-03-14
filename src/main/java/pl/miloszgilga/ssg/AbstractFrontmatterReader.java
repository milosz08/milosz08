/*
 * Copyright (c) 2025 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractFrontmatterReader {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractFrontmatterReader.class);

	private static final Pattern FRONTMATTER_DELIMITER = Pattern.compile("---(.*?)---", Pattern.DOTALL);
	private static final String LINE_DELIMITER = "\\r?\\n|\\r";
	private static final String SEGMENT_DELIMITER = ": ";

	private final String fileRawContent;
	private final String fileName;
	private final Map<String, String> sections;

	private AbstractFrontmatterReader(String relativeFilePath, Function<FileReader, String> fileRawContent) {
		final FileReader fileReader = new FileReader(setRootDir() + relativeFilePath + setExtension());
		this.fileRawContent = fileRawContent.apply(fileReader);
		fileName = fileReader.getFileName(true);
		sections = new HashMap<>();
	}

	protected AbstractFrontmatterReader(String relativeFilePath, String fileRawContent) {
		this(relativeFilePath, ignored -> fileRawContent);
	}

	protected AbstractFrontmatterReader(String relativeFilePath) {
		this(relativeFilePath, FileReader::readFileToRawStr);
	}

	public String extractSections() {
		final Matcher matcher = FRONTMATTER_DELIMITER.matcher(fileRawContent);
		while (matcher.find()) {
			final String[] lines = matcher.group(1).trim().split(LINE_DELIMITER);
			for (final String line : lines) {
				final String[] segments = line.split(SEGMENT_DELIMITER);
				sections.put(segments[0], segments[1]);
			}
		}
		sections.put("fileNameWithoutExt", fileName);
		LOG.debug("Extracted: {} frontmatter sections.", sections);
		return FRONTMATTER_DELIMITER.matcher(fileRawContent)
			.replaceAll(StringUtils.EMPTY)
			.trim();
	}

	public String getSectionValue(String key) {
		final String value = sections.get(key);
		if (value == null) {
			throw new InoperableException(getClass(), "Unable to find frontmatter section value for key: %s.", key);
		}
		return value;
	}

	public String getSectionValue(String key, String defaultValue) {
		if (defaultValue == null) {
			throw new InoperableException(getClass(), "Default value cannot be null.");
		}
		final String value = sections.get(key);
		return value == null ? defaultValue : value;
	}

	public Map<String, String> getSections() {
		return sections;
	}

	protected abstract String setRootDir();

	protected abstract String setExtension();
}
