package pl.miloszgilga.jargonaut.core.template.generator;

import java.nio.file.Path;

public record RenderedTextContent(Path outputPath, String textContent) {
}
