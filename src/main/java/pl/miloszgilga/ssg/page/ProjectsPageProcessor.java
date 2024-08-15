/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg.page;

import pl.miloszgilga.ssg.CommandLineHandler;
import pl.miloszgilga.ssg.hbs.HbsExtractor;
import pl.miloszgilga.ssg.hbs.PathTraversalData;
import pl.miloszgilga.ssg.hbs.ProjectData;
import pl.miloszgilga.ssg.i18n.Language;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class ProjectsPageProcessor extends AbstractPageProcessor {
	public ProjectsPageProcessor(CommandLineHandler commandLineHandler) {
		super(commandLineHandler);
	}

	@Override
	protected Language[] availableLanguages() {
		return Language.values();
	}

	@Override
	protected PathTraversalData pageTraversal(Map<String, Object> pageData) {
		return PathTraversalData.ofPath("projects/index");
	}

	@Override
	protected List<Map<String, Object>> hbsPagesData(HbsExtractor hbsExtractor) {
		final Map<String, List<ProjectData>> projects = hbsExtractor.extractProjects();

		final Map<String, Object> pageData = new HashMap<>();
		pageData.put("projects", projects);
		return List.of(pageData);
	}

	@Override
	protected List<String> additionalCssResources() {
		return List.of("projects");
	}
}
