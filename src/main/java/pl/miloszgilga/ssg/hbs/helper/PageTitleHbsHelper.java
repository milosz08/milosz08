/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg.hbs.helper;

public class PageTitleHbsHelper extends AbstractHbsHelperBase {
	private static final String DEFAULT_PAGE_TITLE = "Miłosz Gilga";

	@Override
	String inject(String title) {
		if (title.equals(DEFAULT_PAGE_TITLE)) {
			return title;
		}
		return String.format("%s | %s", title, DEFAULT_PAGE_TITLE);
	}

	@Override
	public String helperName() {
		return "title";
	}
}
