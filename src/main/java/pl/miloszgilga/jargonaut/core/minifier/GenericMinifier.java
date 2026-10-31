package pl.miloszgilga.jargonaut.core.minifier;

import pl.miloszgilga.jargonaut.core.event.BuildEvent;
import pl.miloszgilga.jargonaut.core.event.BuildEventBus;

public abstract class GenericMinifier implements WebMinifier {
  protected GenericMinifier(BuildEventBus eventBus) {
    eventBus.publish(BuildEvent.spinnerDetail("Registering minifier: " + getClass().getSimpleName()));
  }
}
