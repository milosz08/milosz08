package pl.miloszgilga.jargonaut.core.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BuildEventBus {
  private final List<EventBusListener> eventBusListeners = new CopyOnWriteArrayList<>();

  public void register(EventBusListener eventBusListener) {
    eventBusListeners.add(eventBusListener);
  }

  public void publish(BuildEvent event) {
    for (final EventBusListener eventBusListener : eventBusListeners) {
      eventBusListener.onEvent(event);
    }
  }
}
