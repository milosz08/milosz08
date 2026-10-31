package pl.miloszgilga.jargonaut.core.processor;

import pl.miloszgilga.jargonaut.asset.AssetTransformer;
import pl.miloszgilga.jargonaut.core.parser.DocumentParser;
import pl.miloszgilga.jargonaut.core.parser.UrlNormalizer;
import pl.miloszgilga.jargonaut.core.parser.yaml.YamlParser;

import java.nio.file.Path;

public class ProcessorContext {
  private final Path baseSitesDir;
  private final YamlParser yamlParser;
  private final DocumentParser documentParser;
  private final AssetTransformer assetTransformer;
  private final UrlNormalizer urlNormalizer;

  private ProcessorContext(Builder builder) {
    baseSitesDir = builder.baseSitesDir;
    yamlParser = builder.yamlParser;
    documentParser = builder.documentParser;
    assetTransformer = builder.assetTransformer;
    urlNormalizer = builder.urlNormalizer;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Path getBaseSitesDir() {
    return baseSitesDir;
  }

  public YamlParser getYamlParser() {
    return yamlParser;
  }

  public DocumentParser getDocumentParser() {
    return documentParser;
  }

  public AssetTransformer getAssetTransformer() {
    return assetTransformer;
  }

  public UrlNormalizer getUrlNormalizer() {
    return urlNormalizer;
  }

  public static class Builder {
    private Path baseSitesDir;
    private YamlParser yamlParser;
    private DocumentParser documentParser;
    private AssetTransformer assetTransformer;
    private UrlNormalizer urlNormalizer;

    public Builder baseSitesDir(Path baseSitesDir) {
      this.baseSitesDir = baseSitesDir;
      return this;
    }

    public Builder yamlParser(YamlParser yamlParser) {
      this.yamlParser = yamlParser;
      return this;
    }

    public Builder documentParser(DocumentParser documentParser) {
      this.documentParser = documentParser;
      return this;
    }

    public Builder assetTransformer(AssetTransformer assetTransformer) {
      this.assetTransformer = assetTransformer;
      return this;
    }

    public Builder urlNormalizer(UrlNormalizer urlNormalizer) {
      this.urlNormalizer = urlNormalizer;
      return this;
    }

    public ProcessorContext build() {
      return new ProcessorContext(this);
    }
  }
}
