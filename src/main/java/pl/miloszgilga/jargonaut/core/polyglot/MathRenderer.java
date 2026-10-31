package pl.miloszgilga.jargonaut.core.polyglot;

import pl.miloszgilga.jargonaut.core.SilentCloseable;

public interface MathRenderer extends SilentCloseable {
  String renderMath(String tex, boolean isDisplayMode);
}
