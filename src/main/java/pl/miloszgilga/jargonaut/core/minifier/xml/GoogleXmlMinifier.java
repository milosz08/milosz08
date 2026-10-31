package pl.miloszgilga.jargonaut.core.minifier.xml;

import com.googlecode.htmlcompressor.compressor.XmlCompressor;

import pl.miloszgilga.jargonaut.core.event.BuildEventBus;
import pl.miloszgilga.jargonaut.core.minifier.GenericMinifier;

public class GoogleXmlMinifier extends GenericMinifier implements XmlMinifier {
  private final XmlCompressor xmlCompressor;

  public GoogleXmlMinifier(BuildEventBus eventBus) {
    super(eventBus);
    xmlCompressor = new XmlCompressor();
    xmlCompressor.setRemoveComments(true);
    xmlCompressor.setRemoveIntertagSpaces(true);
  }

  @Override
  public String minify(String rawContent) {
    return xmlCompressor.compress(rawContent);
  }
}
