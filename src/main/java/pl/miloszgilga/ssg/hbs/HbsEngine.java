/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg.hbs;

import com.github.jknack.handlebars.Handlebars;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.miloszgilga.ssg.hbs.helper.AbstractHbsHelperBase;

class HbsEngine extends Handlebars {
	private static final Logger LOG = LoggerFactory.getLogger(HbsEngine.class);

	void addHelper(AbstractHbsHelperBase hbsHelperBase) {
		registerHelper(hbsHelperBase.helperName(), hbsHelperBase);
		LOG.debug("Add HBS renderer helper: {}.", hbsHelperBase.getClass().getName());
	}
}
