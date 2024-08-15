/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg.hbs.helper;

import pl.miloszgilga.ssg.i18n.I18n;

public class I18nHbsHelper extends AbstractHbsHelperBase {
	private final I18n i18n;

	public I18nHbsHelper(I18n i18n) {
		this.i18n = i18n;
	}

	@Override
	String inject(String args) {
		return i18n.t(args, true);
	}

	@Override
	public String helperName() {
		return "i18n";
	}
}
