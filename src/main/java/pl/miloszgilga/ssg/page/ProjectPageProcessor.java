/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg.page;

import pl.miloszgilga.ssg.CommandLineHandler;
import pl.miloszgilga.ssg.hbs.HbsExtractor;
import pl.miloszgilga.ssg.hbs.PathTraversalData;
import pl.miloszgilga.ssg.hbs.ProjectRawData;
import pl.miloszgilga.ssg.i18n.Language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class ProjectPageProcessor extends AbstractPageProcessor {
	public ProjectPageProcessor(CommandLineHandler commandLineHandler) {
		super(commandLineHandler);
	}

	@Override
	protected Language[] availableLanguages() {
		return Language.values();
	}

	@Override
	protected PathTraversalData pageTraversal(Map<String, Object> pageData) {
		final String slug = (String) pageData.get("projectSlug");
		return PathTraversalData.ofPathWithParams("project/[projectSlug]/index", Map.of("projectSlug", slug));
	}

	@Override
	protected List<Map<String, Object>> hbsPagesData(HbsExtractor hbsExtractor) {
		final List<Map<String, Object>> pagesData = new ArrayList<>();
		for (final ProjectRawData project : hbsExtractor.getListOfProjects()) {
			final Map<String, Object> pageData = new HashMap<>();
			pageData.put("projectName", project.name());
			pageData.put("projectSlug", project.slug());
			pagesData.add(pageData);
		}
		return pagesData;
	}

	@Override
	protected List<String> additionalCssResources() {
		return List.of("gallery");
	}
}
