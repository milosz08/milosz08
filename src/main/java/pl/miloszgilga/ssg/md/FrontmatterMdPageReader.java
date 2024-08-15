/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg.md;

import pl.miloszgilga.ssg.AbstractFrontmatterReader;

public class FrontmatterMdPageReader extends AbstractFrontmatterReader {
	public FrontmatterMdPageReader(String relativeFilePath) {
		super(relativeFilePath);
	}

	@Override
	protected String setRootDir() {
		return "markdown/";
	}

	@Override
	protected String setExtension() {
		return ".md";
	}
}
