/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg.i18n;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public enum Language {
	PL("pl", false),
	EN("en", true),
	;

	private final String code;
	private final boolean isDefault;

	Language(String code, boolean isDefault) {
		this.code = code;
		this.isDefault = isDefault;
	}

	public String getCode() {
		return code;
	}

	Locale toLocale() {
		return Locale.forLanguageTag(code);
	}

	public String insertUrlCode(String baseUrl) {
		return isDefault ? baseUrl : "/" + code + baseUrl;
	}

	public Language getNext() {
		final Language[] languages = values();
		for (int i = 0; i < languages.length; i++) {
			if (languages[i].code.equals(code)) {
				return languages[(i + 1) % languages.length];
			}
		}
		return Language.EN;
	}

	public String addPrefix(String page) {
		return (isDefault ? StringUtils.EMPTY : code + "/") + page;
	}
}
