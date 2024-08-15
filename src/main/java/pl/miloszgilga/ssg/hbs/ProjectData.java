/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg.hbs;

public record ProjectData(
	String name,
	String slug
) {
	public ProjectData(ProjectRawData projectRawData) {
		this(projectRawData.name(), projectRawData.slug());
	}
}
