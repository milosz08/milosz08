/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg.hbs;

import com.github.jknack.handlebars.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.miloszgilga.ssg.AbstractFrontmatterReader;
import pl.miloszgilga.ssg.CommandLineHandler;
import pl.miloszgilga.ssg.FileReader;
import pl.miloszgilga.ssg.InoperableException;
import pl.miloszgilga.ssg.hbs.helper.I18nHbsHelper;
import pl.miloszgilga.ssg.hbs.helper.MarkdownInjectorHbsHelper;
import pl.miloszgilga.ssg.hbs.helper.PageTitleHbsHelper;
import pl.miloszgilga.ssg.hbs.helper.UrlHbsHelper;
import pl.miloszgilga.ssg.i18n.I18n;
import pl.miloszgilga.ssg.i18n.Language;
import pl.miloszgilga.ssg.minifier.HtmlMinifier;
import pl.miloszgilga.ssg.minifier.WebMinifier;

import java.io.IOException;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

public class HbsRenderer extends AbstractHbsBase {
	private static final Logger LOG = LoggerFactory.getLogger(HbsRenderer.class);

	private static final String DEFAULT_LAYOUT = "main";
	private static final String LAYOUTS_DIR = "layouts/";
	private static final String PAGES_DIR = "templates/";
	private static final String DEFAULT_PAGE_TITLE = "Miłosz Gilga";

	private final CommandLineHandler commandLineHandler;
	private final PathTraversalData pathTraversalData;
	private final String pageRawContent;
	private final String layoutRawContent;
	private final WebMinifier htmlMinifier;
	private final HbsEngine hbsEngine;

	public HbsRenderer(I18n i18n, CommandLineHandler commandLineHandler, String layoutName,
										 PathTraversalData pathTraversalData) {
		super(i18n);
		this.commandLineHandler = commandLineHandler;
		this.pathTraversalData = pathTraversalData;
		pageRawContent = new FileReader(PAGES_DIR + pathTraversalData.filePath() + HBS_EXT).readFileToRawStr();
		layoutRawContent = new FileReader(LAYOUTS_DIR + layoutName + HBS_EXT).readFileToRawStr();
		htmlMinifier = new HtmlMinifier();
		hbsEngine = new HbsEngine();
		hbsEngine.addHelper(new I18nHbsHelper(i18n));
		hbsEngine.addHelper(new MarkdownInjectorHbsHelper(i18n));
		hbsEngine.addHelper(new PageTitleHbsHelper());
		hbsEngine.addHelper(new UrlHbsHelper(i18n));
	}

	public HbsRenderer(I18n i18N, CommandLineHandler commandLineHandler, PathTraversalData pathTraversalData) {
		this(i18N, commandLineHandler, DEFAULT_LAYOUT, pathTraversalData);
	}

	public String parse(Map<String, Object> pageContext, Map<String, Object> layoutContext, boolean minifyContent) {
		final Language language = i18n.getLanguage();
		String htmlContent;
		try {
			final String pageContent = hbsEngine.compileInline(pageRawContent).apply(pageContext);
			final AbstractFrontmatterReader reader = new FrontmatterHbsPageReader(pathTraversalData.filePath(), pageContent);

			final Map<String, Object> layoutVariables = new HashMap<>(layoutContext);
			layoutVariables.put("language", language.getCode());
			layoutVariables.put("body", reader.extractSections());
			layoutVariables.put("domain", commandLineHandler.getArg(CommandLineHandler.Arg.DOMAIN));
			layoutVariables.put("pageUrl", pathTraversalData.mappingPath());
			layoutVariables.put("year", Year.now().getValue());
			layoutVariables.put("languageButton", determinateLanguageButton());
			layoutVariables.put("pageTitle", reader.getSectionValue("title", DEFAULT_PAGE_TITLE));

			final Template layoutTemplate = hbsEngine.compileInline(layoutRawContent);
			htmlContent = layoutTemplate.apply(layoutVariables);
			if (minifyContent) {
				htmlContent = htmlMinifier.minify(htmlContent, null);
			}
			LOG.debug("Parsed HBS page: {} with lang: {}.", pathTraversalData.mappingPath(), language);
		} catch (IOException ex) {
			throw new InoperableException(getClass(), "Unable to parse HBS file. Cause: %s.", ex.getMessage());
		}
		return htmlContent;
	}

	public String parse(Map<String, Object> pageContext, Map<String, Object> layoutContext) {
		return parse(pageContext, layoutContext, true);
	}

	private LanguageButtonData determinateLanguageButton() {
		final Language nextLanguage = i18n.getLanguage().getNext();
		return new LanguageButtonData(nextLanguage.addPrefix(pathTraversalData.withParams()), nextLanguage.getCode());
	}
}
