package pl.miloszgilga.ssg.md;

import pl.miloszgilga.ssg.AbstractFrontmatterReader;
import pl.miloszgilga.ssg.ProcessingHelperBase;

public interface MdHelperBase extends ProcessingHelperBase {
	String inject(String args, AbstractFrontmatterReader reader) throws Exception;

	boolean injectBeforeHtmlProcessing();
}
