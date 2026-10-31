package pl.miloszgilga.jargonaut.core.parser;

import java.util.Locale;

public record ParsedDocument<T>(Locale locale, T metadata, String htmlContent) {
}
