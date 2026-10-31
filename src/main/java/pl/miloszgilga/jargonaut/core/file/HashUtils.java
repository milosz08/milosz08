package pl.miloszgilga.jargonaut.core.file;

import pl.miloszgilga.jargonaut.core.GenerationHaltedException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;

public class HashUtils {
  private HashUtils() {
  }

  public static String generateTextHash(String content) {
    return generateHash(content.getBytes(StandardCharsets.UTF_8));
  }

  public static String generateFileHash(Path file) {
    return generateHash(FileUtils.readAllBytes(file));
  }

  private static String generateHash(byte[] content) {
    try {
      final MessageDigest digest = MessageDigest.getInstance("SHA-256");
      final byte[] hashBytes = digest.digest(content);
      final StringBuilder hexString = new StringBuilder();
      for (final byte hashByte : hashBytes) {
        final String hex = Integer.toHexString(0xff & hashByte);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }
      return hexString.substring(0, 8);
    } catch (Exception ex) {
      throw new GenerationHaltedException(ex);
    }
  }
}
