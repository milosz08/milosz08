/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg.hbs.helper;

import org.apache.commons.lang3.StringUtils;
import pl.miloszgilga.ssg.i18n.I18n;

public class UrlHbsHelper extends AbstractHbsHelperBase {
	private final I18n i18n;

	public UrlHbsHelper(I18n i18n) {
		this.i18n = i18n;
	}

	@Override
	String inject(String args) {
		if (args != null) {
			return i18n.getLanguage().insertUrlCode(args);
		}
		return StringUtils.EMPTY;
	}

	@Override
	public String helperName() {
		return "url";
	}
}
