package pl.miloszgilga.jargonaut.core.minifier.js;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pl.miloszgilga.jargonaut.core.event.BuildEventBus;
import pl.miloszgilga.jargonaut.core.minifier.GenericMinifier;

import java.util.Set;

public class InlineJsInHtmlMinifier extends GenericMinifier implements InlineJsMinifier {
  private static final Set<String> EXECUTABLE_JS_TYPES = Set.of(
    "",
    "text/javascript",
    "application/javascript",
    "module"
  );

  private final JsMinifier jsMinifier;

  public InlineJsInHtmlMinifier(BuildEventBus eventBus, JsMinifier jsMinifier) {
    super(eventBus);
    this.jsMinifier = jsMinifier;
  }

  @Override
  public String minify(String htmlContent) {
    if (htmlContent == null || htmlContent.isBlank()) {
      return htmlContent;
    }
    final Document document = Jsoup.parse(htmlContent);
    document.outputSettings().prettyPrint(false);
    final Elements inlineScripts = document.select("script:not([src])");
    for (final Element script : inlineScripts) {
      final String type = script.attr("type").trim().toLowerCase();
      if (!EXECUTABLE_JS_TYPES.contains(type)) {
        continue;
      }
      final String rawJs = script.data();
      if (rawJs.isBlank()) {
        continue;
      }
      final String minifiedJs = jsMinifier.minify(rawJs).trim();
      script.empty();
      script.appendChild(new DataNode(minifiedJs));
    }
    return document.html();
  }
}
