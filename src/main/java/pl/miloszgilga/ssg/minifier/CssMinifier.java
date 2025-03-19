package pl.miloszgilga.ssg.minifier;

import net.logicsquad.minifier.MinificationException;
import net.logicsquad.minifier.Minifier;
import net.logicsquad.minifier.css.CSSMinifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.miloszgilga.ssg.InoperableException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class CssMinifier implements WebMinifier {
	private static final Logger LOG = LoggerFactory.getLogger(CssMinifier.class);

	@Override
	public String minify(String rawContent, File fileHandler) {
		String minifiedContent;
		try (final StringWriter output = new StringWriter()) {
			final Minifier minifier = new CSSMinifier(new StringReader(rawContent));
			minifier.minify(output);
			output.flush();
			minifiedContent = output.toString();
			LOG.info("Minified CSS file: {}.", fileHandler.getPath());
		} catch (IOException | MinificationException ex) {
			throw new InoperableException(getClass(), "Unable to minify CSS file %s. Cause: %s.",
				fileHandler.getPath(), ex.getMessage());
		}
		return minifiedContent;
	}
}
