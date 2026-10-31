package pl.miloszgilga.jargonaut.postprocessor.toc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pl.miloszgilga.jargonaut.core.event.BuildEventBus;
import pl.miloszgilga.jargonaut.core.parser.UrlNormalizer;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.template.FreemarkerTemplateName;
import pl.miloszgilga.jargonaut.core.template.TemplateEngine;
import pl.miloszgilga.jargonaut.postprocessor.GenericHtmlPostProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TocHtmlPostProcessor extends GenericHtmlPostProcessor {
  private static final String TOC_PLACEHOLDER_ID = "toc-placeholder";

  private final UrlNormalizer urlNormalizer;
  private final TemplateEngine templateEngine;

  public TocHtmlPostProcessor(UrlNormalizer urlNormalizer, TemplateEngine templateEngine, BuildEventBus eventBus) {
    super(eventBus);
    this.urlNormalizer = urlNormalizer;
    this.templateEngine = templateEngine;
  }

  @Override
  public String process(String rawHtml, Locale locale, SiteBuildContext context) {
    final Document doc = Jsoup.parse(rawHtml);
    final Elements headings = doc.select("h2, h3, h4, h5, h6");
    final List<TocItem> rootItems = buildTocTree(headings);
    injectRenderedToc(doc, rootItems, context, locale);
    return doc.html();
  }

  @Override
  public void insertPageAttributesBeforeRender(Map<String, Object> pageAttributes) {
    pageAttributes.put("tocPlaceholderId", TOC_PLACEHOLDER_ID);
  }

  private List<TocItem> buildTocTree(Elements headings) {
    final List<TocItem> rootItems = new ArrayList<>();
    final TocItem[] lastAtLevel = new TocItem[7];
    for (final Element heading : headings) {
      final String text = heading.text();
      String id = heading.id();
      if (id.isEmpty()) {
        id = urlNormalizer.normalize(text);
        heading.attr("id", id);
      }
      final TocItem currentItem = new TocItem(id, text);
      final int currentLevel = Integer.parseInt(heading.tagName().substring(1));
      TocItem parent = null;
      for (int i = currentLevel - 1; i >= 1; i--) {
        if (lastAtLevel[i] != null) {
          parent = lastAtLevel[i];
          break;
        }
      }
      if (parent != null) {
        parent.getChildren().add(currentItem);
      } else {
        rootItems.add(currentItem);
      }
      lastAtLevel[currentLevel] = currentItem;
      for (int i = currentLevel + 1; i <= 6; i++) {
        lastAtLevel[i] = null;
      }
    }
    return rootItems;
  }

  private void injectRenderedToc(Document document, List<TocItem> rootItems, SiteBuildContext context, Locale locale) {
    final Element placeholder = document.getElementById(TOC_PLACEHOLDER_ID);
    if (placeholder != null) {
      if (!rootItems.isEmpty()) {
        final String renderedToc = templateEngine
          .render(FreemarkerTemplateName.TOC_PLACEHOLDER, Map.of(
            "i18n", context.getDictionaries().get(locale),
            "toc", rootItems
          ), locale);
        placeholder.replaceWith(Jsoup.parseBodyFragment(renderedToc).body().child(0));
      } else {
        placeholder.remove();
      }
    }
  }
}
