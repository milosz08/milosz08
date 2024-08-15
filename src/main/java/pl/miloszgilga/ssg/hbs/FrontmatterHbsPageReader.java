/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg.hbs;

import pl.miloszgilga.ssg.AbstractFrontmatterReader;

class FrontmatterHbsPageReader extends AbstractFrontmatterReader {
	FrontmatterHbsPageReader(String relativeFilePath, String fileRawContent) {
		super(relativeFilePath, fileRawContent);
	}

	@Override
	protected String setRootDir() {
		return "templates/";
	}

	@Override
	protected String setExtension() {
		return ".hbs";
	}
}
