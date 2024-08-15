/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg.minifier;

import java.io.File;

public interface WebMinifier {
	String minify(String rawContent, File fileHandler);
}
