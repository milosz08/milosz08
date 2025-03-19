package pl.miloszgilga.ssg.minifier;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;

import java.io.File;

public class HtmlMinifier implements WebMinifier {
	private final HtmlCompressor htmlCompressor;

	public HtmlMinifier() {
		htmlCompressor = new HtmlCompressor();
	}

	@Override
	public String minify(String rawContent, File fileHandler) {
		return htmlCompressor.compress(rawContent);
	}
}
