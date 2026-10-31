package pl.miloszgilga.jargonaut.core;

public class CastUtils {
  private CastUtils() {
  }

  @SuppressWarnings("unchecked")
  public static <T> T unsafeCast(Object object) {
    return (T) object;
  }
}
