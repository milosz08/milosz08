package pl.miloszgilga.jargonaut.core.template.generator;

import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;

import java.util.Locale;
import java.util.Map;

public interface HtmlPostProcessor {
  String process(String rawHtml, Locale locale, SiteBuildContext context);

  void insertPageAttributesBeforeRender(Map<String, Object> pageAttributes);
}
