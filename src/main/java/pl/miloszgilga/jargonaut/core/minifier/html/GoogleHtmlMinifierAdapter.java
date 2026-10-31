package pl.miloszgilga.jargonaut.core.minifier.html;

import com.googlecode.htmlcompressor.compressor.HtmlCompressor;

import pl.miloszgilga.jargonaut.core.event.BuildEventBus;
import pl.miloszgilga.jargonaut.core.minifier.GenericMinifier;

public class GoogleHtmlMinifierAdapter extends GenericMinifier implements HtmlMinifier {
  private final HtmlCompressor compressor;

  public GoogleHtmlMinifierAdapter(BuildEventBus eventBus) {
    super(eventBus);
    compressor = new HtmlCompressor();
    compressor.setRemoveComments(true);
    compressor.setRemoveMultiSpaces(true);
    compressor.setPreserveLineBreaks(false);
    compressor.setRemoveIntertagSpaces(true);
  }

  @Override
  public String minify(String rawHtml) {
    if (rawHtml == null || rawHtml.isEmpty()) {
      return rawHtml;
    }
    return compressor.compress(rawHtml);
  }
}
