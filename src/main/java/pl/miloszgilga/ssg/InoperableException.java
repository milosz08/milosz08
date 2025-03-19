package pl.miloszgilga.ssg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InoperableException extends RuntimeException {
	private final Class<?> clazz;

	public InoperableException(Class<?> clazz, String message, Object... args) {
		super(String.format(message, args));
		this.clazz = clazz;
	}

	public void logError() {
		final Logger logger = LoggerFactory.getLogger(clazz);
		logger.error(getMessage());
	}
}
