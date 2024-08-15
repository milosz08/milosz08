/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg.hbs.helper;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.apache.commons.lang3.StringUtils;
import pl.miloszgilga.ssg.ProcessingHelperBase;

import java.util.StringJoiner;

public abstract class AbstractHbsHelperBase implements Helper<String>, ProcessingHelperBase {
	@Override
	public Object apply(String context, Options options) {
		final StringJoiner joiner = new StringJoiner(StringUtils.EMPTY);
		joiner.add(context);
		for (final Object param : options.params) {
			joiner.add(param.toString());
		}
		return inject(joiner.toString());
	}

	abstract String inject(String args);
}
