package pl.miloszgilga.jargonaut.postprocessor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pl.miloszgilga.jargonaut.core.event.BuildEventBus;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;

public class EmailManglingHtmlPostProcessor extends GenericHtmlPostProcessor {
  public EmailManglingHtmlPostProcessor(BuildEventBus eventBus) {
    super(eventBus);
  }

  @Override
  public String process(String rawHtml, Locale locale, SiteBuildContext context) {
    if (rawHtml == null || !rawHtml.contains("mailto:")) {
      return rawHtml;
    }
    final Document doc = Jsoup.parse(rawHtml);
    doc.outputSettings().prettyPrint(false);
    final Elements mailtoLinks = doc.select("a[href^=mailto:]");
    if (mailtoLinks.isEmpty()) {
      return rawHtml;
    }
    final String revealPlaceholder = (String) context.getDictionaries().get(locale).get("revealEmail");
    for (final Element link : mailtoLinks) {
      final String href = link.attr("href");
      final String rawEmail = href.substring(7).split("\\?")[0].trim();
      if (rawEmail.isEmpty()) {
        continue;
      }
      final String encodedEmail = Base64.getEncoder().encodeToString(rawEmail.getBytes(StandardCharsets.UTF_8));
      link.attr("href", "#");
      link.attr("data-obfuscated-email", encodedEmail);
      link.attr("rel", "nofollow noopener");
      final String linkText = link.text().trim();
      if (linkText.contains(rawEmail) || linkText.contains("@")) {
        link.text("[" + revealPlaceholder + "]");
      }
    }
    return doc.html();
  }
}
