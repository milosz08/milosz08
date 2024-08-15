/*
 * Copyright (c) 2024 by MILOSZ GILGA <https://miloszgilga.pl>
 * You can find the licenses for this software in the LICENSE file.
 */
package pl.miloszgilga.ssg.i18n;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.miloszgilga.ssg.InoperableException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class I18nBundlesCache {
	private static final Logger LOG = LoggerFactory.getLogger(I18nBundlesCache.class);
	private static final String RESOURCE_BUNDLE_NAME = "i18n.messages";

	private static volatile I18nBundlesCache instance;

	private final Map<Language, ResourceBundle> resourceBundles;

	private I18nBundlesCache() {
		resourceBundles = loadTranslations();
	}

	public static synchronized I18nBundlesCache getInstance() {
		if (instance == null) {
			instance = new I18nBundlesCache();
		}
		return instance;
	}

	private Map<Language, ResourceBundle> loadTranslations() {
		final Map<Language, ResourceBundle> translations = new HashMap<>();
		try {
			for (final Language language : Language.values()) {
				translations.put(language, ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME, language.toLocale()));
			}
			if (!checkResourceBundleKeysConsistence(translations)) {
				throw new IOException("Inconsistent resource bundle keys count");
			}
			LOG.info("Loaded {} translations with {} keys.", translations.keySet(), getTranslationKeySize(translations));
		} catch (IOException ex) {
			throw new InoperableException(getClass(), "Unable to load translations. Cause: %s.", ex.getMessage());
		}
		return translations;
	}

	private int getTranslationKeySize(Map<Language, ResourceBundle> translations) {
		return translations.get(translations.keySet().stream().toList().get(0)).keySet().size();
	}

	private boolean checkResourceBundleKeysConsistence(Map<Language, ResourceBundle> translations) {
		final Set<String> firstResourceBundle = translations.values().iterator().next().keySet();
		for (ResourceBundle bundle : translations.values()) {
			if (bundle.keySet().size() != firstResourceBundle.size()) {
				return false;
			}
		}
		return true;
	}

	public Map<Language, ResourceBundle> getResourceBundles() {
		return resourceBundles;
	}
}
