package pl.miloszgilga.jargonaut.core.cli;

public class Ansi {
  private Ansi() {
  }

  public static final String RESET = "\033[0m";
  public static final String RED = "\u001b[31m";
  public static final String BOLD = "\033[1m";
  public static final String DIM = "\u001b[90m";
  public static final String GREEN = "\033[32m";
  public static final String CYAN = "\033[36m";
  public static final String MAGENTA = "\033[35m";
}
