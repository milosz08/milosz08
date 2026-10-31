package pl.miloszgilga.jargonaut.core.template;

import pl.miloszgilga.jargonaut.core.GenerationHaltedException;
import pl.miloszgilga.jargonaut.core.event.BuildEvent;
import pl.miloszgilga.jargonaut.core.event.BuildEventBus;

import freemarker.core.Environment;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

public class FreeMarkerTemplateEngine implements TemplateEngine {
  private final Configuration configuration;

  public FreeMarkerTemplateEngine(BuildEventBus eventBus) {
    eventBus.publish(BuildEvent.spinnerDetail("Registering template engine: " + getClass().getSimpleName()));
    configuration = new Configuration(Configuration.VERSION_2_3_35);
    configuration.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "templates");
    configuration.setDefaultEncoding("UTF-8");
    configuration.setOutputFormat(HTMLOutputFormat.INSTANCE);
    configuration.setLogTemplateExceptions(false);
    configuration.setWrapUncheckedExceptions(true);
    configuration.setFallbackOnNullLoopVariable(false);
  }

  @Override
  public String render(TemplateName templateName, Map<String, Object> context, Locale locale) {
    try {
      final Template template = configuration.getTemplate(templateName.getTemplateName());
      final StringWriter writer = new StringWriter();
      Environment env = template.createProcessingEnvironment(context, writer);
      env.setLocale(locale);
      env.process();
      return writer.toString();
    } catch (IOException | TemplateException ex) {
      throw new GenerationHaltedException(ex);
    }
  }
}
