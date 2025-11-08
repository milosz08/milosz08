package pl.miloszgilga.ssg.hbs;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Map;

public record PathTraversalData(
	String filePath,
	String mappingPath,
	Map<String, String> params
) {
	private static final String DELIMITER = "[%s]";
	private static final String MAPPING_PATH_TRANSFORM = "(^index$)|(/index$)";
	private static final String DEFAULT_FILE_NAME = "index";

	private PathTraversalData(String path, Map<String, String> params) {
		this(path, path.replaceAll(MAPPING_PATH_TRANSFORM, StringUtils.EMPTY), params);
	}

	private PathTraversalData(String path) {
		this(path, path.replaceAll(MAPPING_PATH_TRANSFORM, StringUtils.EMPTY), Map.of());
	}

	public static PathTraversalData ofPath(String path) {
		return new PathTraversalData(path);
	}

	public static PathTraversalData ofPathWithParams(String path, Map<String, String> params) {
		return new PathTraversalData(path, params);
	}

	public String withParams() {
		return replaceParams(mappingPath);
	}

	public String withParamsAsRawFilePath(boolean separated) {
		String onEmptyReplacer = DEFAULT_FILE_NAME;
		String suffix = "";
		if (separated) {
			onEmptyReplacer = "";
			suffix = (mappingPath.isEmpty() ? "" : File.separator) + DEFAULT_FILE_NAME;
		}
		return replaceParams(mappingPath.isEmpty() ? onEmptyReplacer : mappingPath) + suffix;
	}

	private String replaceParams(String initVariant) {
		String withParams = initVariant;
		for (final Map.Entry<String, String> param : params.entrySet()) {
			withParams = withParams.replace(String.format(DELIMITER, param.getKey()), param.getValue());
		}
		return withParams;
	}
}
