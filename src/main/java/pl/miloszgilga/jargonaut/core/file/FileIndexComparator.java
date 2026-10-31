package pl.miloszgilga.jargonaut.core.file;

import java.nio.file.Path;
import java.util.Comparator;

public class FileIndexComparator implements Comparator<Path> {
  @Override
  public int compare(Path p1, Path p2) {
    return Integer.compare(extractIndex(p1), extractIndex(p2));
  }

  private int extractIndex(Path file) {
    final String[] parts = file.getFileName().toString().split("\\.");
    if (parts.length > 2) {
      try {
        return Integer.parseInt(parts[0]);
      } catch (NumberFormatException ignored) {
      }
    }
    return Integer.MAX_VALUE;
  }
}
