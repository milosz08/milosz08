package pl.miloszgilga.ssg.hbs;

import pl.miloszgilga.ssg.i18n.I18n;

abstract class AbstractHbsBase {
	protected static final String HBS_EXT = ".hbs";

	protected final I18n i18n;

	AbstractHbsBase(I18n i18n) {
		this.i18n = i18n;
	}
}
