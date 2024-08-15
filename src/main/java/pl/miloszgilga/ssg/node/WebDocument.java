/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg.node;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Map;

public class WebDocument extends Document {
	public WebDocument() {
		super(StringUtils.EMPTY);
	}

	public Element createClassNode(String tag, String className) {
		final Element element = createElement(tag);
		element.addClass(className);
		return element;
	}

	public Element createClassWithIdNode(String tag, String id, String className) {
		final Element element = createClassNode(tag, className);
		element.id(id);
		return element;
	}

	public Element createAnchorNode(String href, String className) {
		final Element element = createClassNode("a", className);
		element.attr("href", href);
		return element;
	}

	public Element createAnchorNode(String href, String className, Map<String, Boolean> conditionalClassNames) {
		final Element element = createClassNode("a", className);
		for (final Map.Entry<String, Boolean> conditionalClassName : conditionalClassNames.entrySet()) {
			if (conditionalClassName.getValue()) {
				element.addClass(conditionalClassName.getKey());
			}
		}
		element.attr("href", href);
		return element;
	}

	public Element createImageNode(String src, String alt, String className) {
		final Element element = createClassNode("img", className);
		element.attr("src", src);
		element.attr("alt", alt);
		return element;
	}
}
