/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg.md;

import pl.miloszgilga.ssg.AbstractFrontmatterReader;
import pl.miloszgilga.ssg.ProcessingHelperBase;

public interface MdHelperBase extends ProcessingHelperBase {
	String inject(String args, AbstractFrontmatterReader reader) throws Exception;
	boolean injectBeforeHtmlProcessing();
}
