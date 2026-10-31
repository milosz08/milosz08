package pl.miloszgilga.jargonaut.core.polyglot;

import pl.miloszgilga.jargonaut.core.SilentCloseable;

public interface SyntaxHighlighter extends SilentCloseable {
  String renderHighlight(String code, String language);
}
