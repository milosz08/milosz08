package pl.miloszgilga.jargonaut.core;

@FunctionalInterface
public interface CloudItemMapper<V> {
  V map(WordCloudUtils.CloudItemData item, String fontSize);
}
