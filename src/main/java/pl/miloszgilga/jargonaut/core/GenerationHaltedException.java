package pl.miloszgilga.jargonaut.core;

public class GenerationHaltedException extends RuntimeException {
  public GenerationHaltedException(String message) {
    super(message);
  }

  public GenerationHaltedException(String message, Throwable cause) {
    super(message, cause);
  }

  public GenerationHaltedException(Throwable cause) {
    super(cause);
  }
}
