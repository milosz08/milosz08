package pl.miloszgilga.ssg.minifier;

import java.io.File;

public interface WebMinifier {
	String minify(String rawContent, File fileHandler);
}
