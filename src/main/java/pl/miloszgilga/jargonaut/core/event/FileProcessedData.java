package pl.miloszgilga.jargonaut.core.event;

public record FileProcessedData(String targetPath, long sizeBytes, boolean fromCache) {
}
