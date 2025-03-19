package pl.miloszgilga.ssg.page;

import pl.miloszgilga.ssg.CommandLineHandler;
import pl.miloszgilga.ssg.hbs.HbsExtractor;
import pl.miloszgilga.ssg.hbs.PathTraversalData;
import pl.miloszgilga.ssg.i18n.Language;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class RootPageProcessor extends AbstractPageProcessor {
	public RootPageProcessor(CommandLineHandler commandLineHandler) {
		super(commandLineHandler);
	}

	@Override
	protected Language[] availableLanguages() {
		return Language.values();
	}

	@Override
	protected PathTraversalData pageTraversal(Map<String, Object> pageData) {
		return PathTraversalData.ofPath("index");
	}

	@Override
	protected List<Map<String, Object>> hbsPagesData(HbsExtractor hbsExtractor) {
		return List.of();
	}

	@Override
	protected List<String> additionalCssResources() {
		return List.of();
	}
}
