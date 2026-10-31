package pl.miloszgilga.jargonaut.core.template;

import java.util.Locale;
import java.util.Map;

public interface TemplateEngine {
  String render(TemplateName templateName, Map<String, Object> context, Locale locale);
}
