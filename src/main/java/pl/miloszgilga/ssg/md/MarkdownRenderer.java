/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg.md;

import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.miloszgilga.ssg.AbstractFrontmatterReader;
import pl.miloszgilga.ssg.InoperableException;
import pl.miloszgilga.ssg.i18n.I18n;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownRenderer {
	private static final Logger LOG = LoggerFactory.getLogger(MarkdownRenderer.class);

	private static final Pattern HELPER_SEGMENT = Pattern.compile("\\{\\{(\\w+)\\s'([^']*)'}}");
	private static final String INJECT_DELIMITER = "{{%s '%s'}}";

	private final I18n i18n;
	private final Parser markdownParser;
	private final HtmlRenderer htmlRenderer;
	private final Map<String, MdHelperBase> helpers;

	public MarkdownRenderer(I18n i18n) {
		this.i18n = i18n;
		markdownParser = Parser.builder().build();
		htmlRenderer = HtmlRenderer.builder().build();
		helpers = new HashMap<>();
	}

	public void registerHelper(MdHelperBase helper) {
		this.helpers.put(helper.helperName(), helper);
		LOG.debug("Add markdown renderer helper: {}.", helper.getClass().getName());
	}

	public String render(String markdownRelativePath) {
		try {
			final AbstractFrontmatterReader frontmatterReader = new FrontmatterMdPageReader(
				i18n.getLanguage().getCode() + "/" + markdownRelativePath
			);
			final String rawContent = frontmatterReader.extractSections();
			String content = runContentInjector(rawContent, frontmatterReader, MdHelperBase::injectBeforeHtmlProcessing);
			final String rendered = htmlRenderer.render(markdownParser.parse(content));
			LOG.debug("Rendered markdown content: {}.", rendered);
			content = runContentInjector(rendered, frontmatterReader, helper -> !helper.injectBeforeHtmlProcessing());
			return content;
		} catch (Exception ex) {
			throw new InoperableException(getClass(), "Unable to process markdown template: %s. Cause: %s.",
				markdownRelativePath, ex.getMessage());
		}
	}

	private String runContentInjector(String rawContent, AbstractFrontmatterReader reader,
																		Function<MdHelperBase, Boolean> passCondition) throws Exception {
		final Matcher matcher = HELPER_SEGMENT.matcher(rawContent);
		String content = rawContent;
		while (matcher.find()) {
			final String helperName = matcher.group(1);
			final String helperArgs = matcher.group(2);
			final MdHelperBase helper = helpers.get(helperName);
			if (helper != null) {
				if (passCondition.apply(helper)) {
					final String rendered = helper.inject(helperArgs, reader);
					content = content.replace(String.format(INJECT_DELIMITER, helperName, helperArgs), rendered);
				}
			}
		}
		return content;
	}
}
