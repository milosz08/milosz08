/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg.page;

public record ProcesssedPage(
	String qualifiedName,
	String rawHtmlContent
) {
	@Override
	public String toString() {
		return qualifiedName;
	}
}
