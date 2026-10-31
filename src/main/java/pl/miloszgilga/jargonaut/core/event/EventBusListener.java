package pl.miloszgilga.jargonaut.core.event;

public interface EventBusListener {
  void onEvent(BuildEvent event);
}
