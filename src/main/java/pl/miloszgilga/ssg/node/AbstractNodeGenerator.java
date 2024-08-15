/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg.node;

import pl.miloszgilga.ssg.minifier.HtmlMinifier;
import pl.miloszgilga.ssg.minifier.WebMinifier;

import java.util.Map;

public abstract class AbstractNodeGenerator {
	private final WebMinifier htmlMinifier;

	protected AbstractNodeGenerator() {
		htmlMinifier = new HtmlMinifier();
	}

	public String generate(Map<String, String> args) {
		final WebDocument document = prepareContent(new WebDocument(), args);
		return htmlMinifier.minify(document.html(), null);
	}

	protected abstract WebDocument prepareContent(WebDocument document, Map<String, String> payload);
	public abstract String injectorContentName();
}
