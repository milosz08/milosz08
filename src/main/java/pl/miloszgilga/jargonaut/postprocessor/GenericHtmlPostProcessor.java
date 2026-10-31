package pl.miloszgilga.jargonaut.postprocessor;

import pl.miloszgilga.jargonaut.core.event.BuildEvent;
import pl.miloszgilga.jargonaut.core.event.BuildEventBus;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlPostProcessor;

import java.util.Map;

public abstract class GenericHtmlPostProcessor implements HtmlPostProcessor {
  public GenericHtmlPostProcessor(BuildEventBus eventBus) {
    eventBus.publish(BuildEvent.spinnerDetail("Registering HTML post-processor: " + getClass().getSimpleName()));
  }

  @Override
  public void insertPageAttributesBeforeRender(Map<String, Object> pageAttributes) {
  }
}
