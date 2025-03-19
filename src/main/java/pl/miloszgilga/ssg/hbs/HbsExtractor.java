package pl.miloszgilga.ssg.hbs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.miloszgilga.ssg.AbstractFrontmatterReader;
import pl.miloszgilga.ssg.FileReader;
import pl.miloszgilga.ssg.i18n.I18n;
import pl.miloszgilga.ssg.md.FrontmatterMdPageReader;

import java.util.*;
import java.util.stream.Collectors;

public class HbsExtractor extends AbstractHbsBase {
	private static final Logger LOG = LoggerFactory.getLogger(HbsExtractor.class);

	public HbsExtractor(I18n i18n) {
		super(i18n);
	}

	public Map<String, List<ProjectData>> extractProjects() {
		final Map<String, List<ProjectRawData>> projectSections = new HashMap<>();
		for (final ProjectRawData project : getListOfProjects()) {
			projectSections.computeIfAbsent(project.type(), data -> new ArrayList<>()).add(project);
		}
		for (final Map.Entry<String, List<ProjectRawData>> projectsSection : projectSections.entrySet()) {
			final List<ProjectRawData> projectSectionList = projectsSection.getValue();
			projectSectionList.sort(Comparator.comparing(ProjectRawData::name));
		}
		LOG.debug("Loaded {} projects pages.", projectSections.size());
		return projectSections.entrySet().stream()
			.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().stream()
				.map(ProjectData::new)
				.toList())
			);
	}

	public List<ProjectRawData> getListOfProjects() {
		final List<ProjectRawData> projectsRawData = new ArrayList<>();
		final String path = i18n.getLanguage().getCode() + "/projects";
		final FileReader fileReader = new FileReader("markdown/" + path);
		for (final String fileName : fileReader.getFileNamesInPath()) {
			final AbstractFrontmatterReader reader = new FrontmatterMdPageReader(path + "/" + fileName);
			reader.extractSections();
			projectsRawData.add(new ProjectRawData(reader, fileName));
		}
		return projectsRawData;
	}
}
