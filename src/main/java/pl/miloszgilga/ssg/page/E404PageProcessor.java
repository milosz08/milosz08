package pl.miloszgilga.ssg.page;

import pl.miloszgilga.ssg.CommandLineHandler;
import pl.miloszgilga.ssg.hbs.HbsExtractor;
import pl.miloszgilga.ssg.hbs.PathTraversalData;
import pl.miloszgilga.ssg.i18n.Language;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class E404PageProcessor extends AbstractPageProcessor {
	public E404PageProcessor(CommandLineHandler commandLineHandler) {
		super(commandLineHandler);
	}

	@Override
	protected Language[] availableLanguages() {
		return new Language[]{Language.EN};
	}

	@Override
	protected PathTraversalData pageTraversal(Map<String, Object> pageData) {
		return PathTraversalData.ofPath("404/index");
	}

	@Override
	protected List<Map<String, Object>> hbsPagesData(HbsExtractor hbsExtractor) {
		return List.of();
	}

	@Override
	protected List<String> additionalCssResources() {
		return List.of("not-found");
	}
}
