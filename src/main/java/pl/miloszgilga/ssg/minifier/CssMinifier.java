package pl.miloszgilga.ssg.minifier;

import net.logicsquad.minifier.MinificationException;
import net.logicsquad.minifier.Minifier;
import net.logicsquad.minifier.css.CSSMinifier;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class CssMinifier implements WebMinifier {
	@Override
	public String minify(String rawContent, File fileHandler) {
		String minifiedContent;
		try (final StringWriter output = new StringWriter()) {
			final Minifier minifier = new CSSMinifier(new StringReader(rawContent));
			minifier.minify(output);
			output.flush();
			minifiedContent = output.toString();
		} catch (IOException | MinificationException ex) {
			minifiedContent = rawContent;
		}
		return minifiedContent;
	}
}
