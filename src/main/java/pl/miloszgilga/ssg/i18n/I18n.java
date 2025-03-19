package pl.miloszgilga.ssg.i18n;

import pl.miloszgilga.ssg.InoperableException;

import java.util.ResourceBundle;

public class I18n {
	private final I18nBundlesCache i18nBundlesCache;
	private final Language language;

	public I18n(Language language) {
		this.language = language;
		i18nBundlesCache = I18nBundlesCache.getInstance();
	}

	public String t(String key, boolean allowNullable) {
		final ResourceBundle bundle = i18nBundlesCache.getResourceBundles().get(language);
		if (allowNullable && !bundle.containsKey(key)) {
			return key;
		}
		if (!bundle.containsKey(key)) {
			throw new InoperableException(getClass(), "Unable to find translation key: %s for language: %s.", key, language);
		}
		return bundle.getString(key);
	}

	public String t(String key) {
		return t(key, false);
	}

	public Language getLanguage() {
		return language;
	}
}
