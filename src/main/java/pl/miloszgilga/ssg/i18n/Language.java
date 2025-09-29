package pl.miloszgilga.ssg.i18n;

import org.apache.commons.lang3.StringUtils;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public enum Language {
	PL("pl", false, "dd.MM.yyyy, HH:mm:ss"),
	EN("en", true, "MM.dd.yyyy, hh:mm:ss a"),
	;

	private final String code;
	private final boolean isDefault;
	private final String dtfPattern;

	Language(String code, boolean isDefault, String dtfPattern) {
		this.code = code;
		this.isDefault = isDefault;
		this.dtfPattern = dtfPattern;
	}

	public String getCode() {
		return code;
	}

	Locale toLocale() {
		return Locale.forLanguageTag(code);
	}

	public DateTimeFormatter toDtfPattern() {
		return DateTimeFormatter.ofPattern(dtfPattern);
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
