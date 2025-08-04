package pl.miloszgilga.ssg.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.miloszgilga.ssg.CommandLineHandler;
import pl.miloszgilga.ssg.hbs.HbsExtractor;
import pl.miloszgilga.ssg.hbs.HbsRenderer;
import pl.miloszgilga.ssg.hbs.PathTraversalData;
import pl.miloszgilga.ssg.i18n.I18n;
import pl.miloszgilga.ssg.i18n.Language;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractPageProcessor {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractPageProcessor.class);
	private static final String GEN_FILE_EXT = ".html";

	private final CommandLineHandler commandLineHandler;

	protected AbstractPageProcessor(CommandLineHandler commandLineHandler) {
		this.commandLineHandler = commandLineHandler;
	}

	public List<ProcesssedPage> processPages() {
		final List<ProcesssedPage> pages = new ArrayList<>();
		for (final Language language : availableLanguages()) {
			final I18n i18n = new I18n(language);
			final HbsExtractor hbsExtractor = new HbsExtractor(i18n);
			// convert immutable list to mutable (for adding empty hashmap)
			final List<Map<String, Object>> hbsPagesData = new ArrayList<>(hbsPagesData(hbsExtractor));
			if (hbsPagesData.isEmpty()) {
				hbsPagesData.add(new HashMap<>());
			}
			final Map<String, Object> layoutData = new HashMap<>();
			layoutData.put("cssResources", additionalCssResources());
			layoutData.put("enabledLngBtn", availableLanguages().length > 1);
			for (final Map<String, Object> pageData : hbsPagesData) {
				final PathTraversalData pathTraversal = pageTraversal(pageData);
				// parse generate HTML
				final HbsRenderer hbsRenderer = new HbsRenderer(i18n, commandLineHandler, pathTraversal);
				final String rawMinifiedHtml = hbsRenderer.parse(pageData, layoutData);
				// fabricate output path and save into output directory
				final String fileNameWithoutExt = i18n.getLanguage().addPrefix(pathTraversal.withParamsAsRawFilePath());
				final ProcesssedPage processsedPage = new ProcesssedPage(File.separator + fileNameWithoutExt + GEN_FILE_EXT,
					rawMinifiedHtml);
				pages.add(processsedPage);
				LOG.debug("Processed page: {}.", processsedPage);
			}
		}
		return pages;
	}

	protected abstract Language[] availableLanguages();

	protected abstract PathTraversalData pageTraversal(Map<String, Object> pageData);

	protected abstract List<Map<String, Object>> hbsPagesData(HbsExtractor hbsExtractor);

	protected abstract List<String> additionalCssResources();
}
