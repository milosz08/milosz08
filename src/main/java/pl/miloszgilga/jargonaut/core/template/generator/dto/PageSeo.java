package pl.miloszgilga.jargonaut.core.template.generator.dto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PageSeo {
  private final String domain;
  private final String htmlLanguage;
  private final String ogLocale;
  private final String pageName;
  private final String pageTitle;
  private final String pageDescription;
  private final String gtmId;
  private final String authorName;
  private final String jobTitle;
  private final String faviconUrl;
  private final String ogImageUrl;
  private final String jsonLdScript;
  private final String canonicalUrl;
  private final Map<String, String> alternateUrls;
  private final List<String> styleFileNames;
  private final List<String> scriptFileNames;
  private final String rssFeedUrl;

  private PageSeo(Builder builder) {
    domain = builder.domain;
    htmlLanguage = builder.htmlLanguage;
    ogLocale = builder.ogLocale;
    pageName = builder.pageName;
    pageTitle = builder.pageTitle;
    pageDescription = builder.pageDescription;
    gtmId = builder.gtmId;
    authorName = builder.authorName;
    jobTitle = builder.jobTitle;
    faviconUrl = builder.faviconUrl;
    ogImageUrl = builder.ogImageUrl;
    jsonLdScript = builder.jsonLdScript;
    canonicalUrl = builder.canonicalUrl;
    alternateUrls = builder.alternateUrls;
    styleFileNames = builder.styleFileNames;
    scriptFileNames = builder.scriptFileNames;
    rssFeedUrl = builder.rssFeedUrl;
  }

  public static Builder builder() {
    return new Builder();
  }

  public String getDomain() {
    return domain;
  }

  public String getHtmlLanguage() {
    return htmlLanguage;
  }

  public String getOgLocale() {
    return ogLocale;
  }

  public String getCanonicalUrl() {
    return canonicalUrl;
  }

  public Map<String, String> getAlternateUrls() {
    return alternateUrls;
  }

  public String getPageName() {
    return pageName;
  }

  public String getPageTitle() {
    return pageTitle;
  }

  public String getPageDescription() {
    return pageDescription;
  }

  public String getFaviconUrl() {
    return faviconUrl;
  }

  public String getOgImageUrl() {
    return ogImageUrl;
  }

  public String getGtmId() {
    return gtmId;
  }

  public String getAuthorName() {
    return authorName;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public String getJsonLdScript() {
    return jsonLdScript;
  }

  public List<String> getStyleFileNames() {
    return styleFileNames;
  }

  public List<String> getScriptFileNames() {
    return scriptFileNames;
  }

  public String getRssFeedUrl() {
    return rssFeedUrl;
  }

  public static class Builder {
    private String domain;
    private String htmlLanguage;
    private String ogLocale;
    private String pageName;
    private String pageTitle;
    private String pageDescription;
    private String gtmId;
    private String authorName;
    private String jobTitle;
    private String faviconUrl;
    private String ogImageUrl;
    private String jsonLdScript;
    private String canonicalUrl;
    private final Map<String, String> alternateUrls = new LinkedHashMap<>();
    private final List<String> styleFileNames = new ArrayList<>();
    private final List<String> scriptFileNames = new ArrayList<>();
    private String rssFeedUrl;

    private Builder() {
    }

    public Builder setDomain(String domain) {
      this.domain = domain;
      return this;
    }

    public Builder setHtmlLanguage(String htmlLanguage) {
      this.htmlLanguage = htmlLanguage;
      return this;
    }

    public Builder setOgLocale(Locale ogLocale) {
      this.ogLocale = ogLocale.toString();
      return this;
    }

    public Builder setLocale(String htmlLang, Locale locale) {
      this.htmlLanguage = htmlLang;
      ogLocale = locale.toString();
      return this;
    }

    public Builder setPageName(String pageName) {
      this.pageName = pageName;
      return this;
    }

    public Builder setPageTitle(String pageTitle) {
      this.pageTitle = pageTitle;
      return this;
    }

    public Builder setPageDescription(String pageDescription) {
      this.pageDescription = pageDescription;
      return this;
    }

    public Builder setGtmId(String gtmId) {
      this.gtmId = gtmId;
      return this;
    }

    public Builder setAuthorName(String authorName) {
      this.authorName = authorName;
      return this;
    }

    public Builder setJobTitle(String jobTitle) {
      this.jobTitle = jobTitle;
      return this;
    }

    public Builder setFaviconUrl(String faviconUrl) {
      this.faviconUrl = faviconUrl;
      return this;
    }

    public Builder setOgImageUrl(String ogImageUrl) {
      this.ogImageUrl = ogImageUrl;
      return this;
    }

    public Builder setJsonLdScript(String jsonLdScript) {
      this.jsonLdScript = jsonLdScript;
      return this;
    }

    public Builder setCanonicalUrl(String canonicalUrl) {
      this.canonicalUrl = canonicalUrl;
      return this;
    }

    public Builder addAlternateUrl(String key, String value) {
      alternateUrls.put(key, value);
      return this;
    }

    public Builder addStyleFileNames(List<String> styleFileNames) {
      this.styleFileNames.addAll(styleFileNames);
      return this;
    }

    public Builder addScriptFileNames(List<String> scriptFileNames) {
      this.scriptFileNames.addAll(scriptFileNames);
      return this;
    }

    public Builder setRssFeedUrl(String rssFeedUrl) {
      this.rssFeedUrl = rssFeedUrl;
      return this;
    }

    public PageSeo build() {
      return new PageSeo(this);
    }
  }
}
