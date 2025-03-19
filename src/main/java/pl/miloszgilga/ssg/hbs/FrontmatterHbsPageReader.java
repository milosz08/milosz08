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
