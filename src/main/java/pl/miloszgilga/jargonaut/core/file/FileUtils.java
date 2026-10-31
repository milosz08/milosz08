package pl.miloszgilga.jargonaut.core.file;

import pl.miloszgilga.jargonaut.core.GenerationHaltedException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FileUtils {
  private FileUtils() {
  }

  public static List<Path> listAllFiles(Path dir) {
    if (!Files.exists(dir) || !Files.isDirectory(dir)) {
      return List.of();
    }
    try (final Stream<Path> paths = Files.list(dir)) {
      return paths.filter(Files::isRegularFile).toList();
    } catch (IOException ignored) {
      return List.of();
    }
  }

  public static List<Path> findFilesInDirectory(Path dir, String extension, Predicate<Path> filter) {
    if (!Files.exists(dir) || !Files.isDirectory(dir)) {
      return List.of();
    }
    try (final Stream<Path> paths = Files.list(dir)) {
      return paths.filter(Files::isRegularFile)
        .filter(p -> p.getFileName().toString().endsWith(extension))
        .filter(filter)
        .toList();
    } catch (IOException ignored) {
      return List.of();
    }
  }

  public static byte[] readAllBytes(Path file) {
    try {
      return Files.readAllBytes(file);
    } catch (IOException ex) {
      throw new GenerationHaltedException(ex);
    }
  }

  public static String readAsString(Path file) {
    try {
      return Files.readString(file, StandardCharsets.UTF_8);
    } catch (IOException ex) {
      throw new GenerationHaltedException(ex);
    }
  }
}
