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
