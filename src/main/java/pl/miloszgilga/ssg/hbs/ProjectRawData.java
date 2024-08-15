/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg.hbs;

import pl.miloszgilga.ssg.AbstractFrontmatterReader;

public record ProjectRawData(
	String name,
	String slug,
	String type
) {
	public ProjectRawData(AbstractFrontmatterReader reader, String slug) {
		this(
			reader.getSectionValue("title"),
			slug,
			reader.getSectionValue("type")
		);
	}
}
