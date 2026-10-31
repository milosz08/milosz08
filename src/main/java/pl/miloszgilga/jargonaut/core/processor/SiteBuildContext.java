package pl.miloszgilga.jargonaut.core.processor;

import pl.miloszgilga.jargonaut.asset.AssetTarget;
import pl.miloszgilga.jargonaut.config.model.Config;
import pl.miloszgilga.jargonaut.core.CastUtils;
import pl.miloszgilga.jargonaut.core.TextContentType;
import pl.miloszgilga.jargonaut.core.event.BuildEventBus;
import pl.miloszgilga.jargonaut.core.parser.GenericPage;
import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.template.generator.RenderedTextContent;
import pl.miloszgilga.jargonaut.globals.model.GlobalsMetadata;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class SiteBuildContext {
  private final BuildEventBus eventBus;

  private Config config;

  private final Map<Locale, GlobalsMetadata> globals = new HashMap<>();
  private final Map<Locale, Map<String, Object>> dictionaries = new HashMap<>();
  private final Set<Locale> activeLocales = new HashSet<>();

  private final Map<Class<?>, Object> sharedPageData = new HashMap<>();
  private final Map<Class<?>, PageRepresentation<?>> singlePages = new HashMap<>();
  private final Map<Class<?>, List<PageRepresentation<?>>> collectionPages = new HashMap<>();

  private final Map<TextContentType, List<RenderedTextContent>> renderedTextContents = new HashMap<>();
  private final Map<Path, AssetTarget> assetTargets = new HashMap<>();
  private final List<String> inlineScripts = new ArrayList<>();

  public SiteBuildContext(BuildEventBus eventBus) {
    this.eventBus = eventBus;
  }

  public BuildEventBus getEventBus() {
    return eventBus;
  }

  public Config getConfig() {
    return config;
  }

  public void setConfig(Config config) {
    this.config = config;
  }

  public Map<Locale, GlobalsMetadata> getGlobals() {
    return globals;
  }

  public Map<Locale, Map<String, Object>> getDictionaries() {
    return dictionaries;
  }

  public Set<Locale> getActiveLocales() {
    return activeLocales;
  }

  public <T> void setSharedData(Class<T> dataType, T data) {
    sharedPageData.put(dataType, data);
  }

  public <T> T getSharedData(Class<T> dataType) {
    final Object data = sharedPageData.get(dataType);
    return data != null ? dataType.cast(data) : null;
  }

  public <T> T computeSharedDataIfAbsent(Class<T> dataType, java.util.function.Supplier<T> supplier) {
    return dataType.cast(sharedPageData.computeIfAbsent(dataType, k -> supplier.get()));
  }

  public <T extends GenericPage> void setSinglePage(Class<T> metadataClass, PageRepresentation<T> page) {
    singlePages.put(metadataClass, page);
  }

  public <T extends GenericPage> List<PageRepresentation<T>> getCollectionPages(Class<T> metadataClass) {
    return CastUtils.unsafeCast(collectionPages.computeIfAbsent(metadataClass, k -> new ArrayList<>()));
  }

  public <T extends GenericPage> void setCollectionPages(Class<T> metadataClass,
                                                         List<PageRepresentation<T>> pages) {
    collectionPages.put(metadataClass, new ArrayList<>(pages));
  }

  public Map<Class<?>, PageRepresentation<?>> getAllSinglePages() {
    return singlePages;
  }

  public Map<Class<?>, List<PageRepresentation<?>>> getAllCollectionPages() {
    return collectionPages;
  }

  public Map<TextContentType, List<RenderedTextContent>> getRenderedTextContents() {
    return renderedTextContents;
  }

  public Map<Path, AssetTarget> getAssetTargets() {
    return assetTargets;
  }

  public List<String> getInlineScripts() {
    return inlineScripts;
  }
}
