package pl.miloszgilga.ssg.hbs;

public record ProjectData(
	String name,
	String slug
) {
	public ProjectData(ProjectRawData projectRawData) {
		this(projectRawData.name(), projectRawData.slug());
	}
}
