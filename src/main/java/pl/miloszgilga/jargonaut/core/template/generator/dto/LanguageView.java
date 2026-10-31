package pl.miloszgilga.jargonaut.core.template.generator.dto;

import java.util.Locale;

public class LanguageView {
  private final String url;
  private final String displayName;
  private final boolean isActive;

  public LanguageView(String url, String langCode, Locale altLocale, Locale currentLocale) {
    this.url = url;
    displayName = langCode.contains("-")
      ? altLocale.getLanguage().toUpperCase(Locale.ROOT) + " (" + altLocale.getCountry().toUpperCase(Locale.ROOT) + ")"
      : langCode.toUpperCase(Locale.ROOT);
    isActive = altLocale.equals(currentLocale);
  }

  public String getUrl() {
    return url;
  }

  public String getDisplayName() {
    return displayName;
  }

  public boolean isActive() {
    return isActive;
  }
}
