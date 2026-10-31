package pl.miloszgilga.jargonaut.core;

import org.jsoup.Jsoup;

public class ReadingTimeUtils {
  private static final int WORDS_PER_MINUTE = 200;

  private ReadingTimeUtils() {
  }

  public static int calculateMinutesFromHtml(String htmlContent) {
    if (htmlContent == null || htmlContent.isBlank()) {
      return 1;
    }
    final String plainText = Jsoup.parse(htmlContent).text();
    return calculateMinutesFromText(plainText);
  }

  public static int calculateMinutesFromText(String text) {
    if (text == null || text.isBlank()) {
      return 1;
    }
    final String[] words = text.trim().split("\\s+");
    final int wordCount = words.length;
    final int minutes = (int) Math.ceil((double) wordCount / WORDS_PER_MINUTE);
    return Math.max(1, minutes);
  }
}
