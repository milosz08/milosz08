package pl.miloszgilga.jargonaut.core.processor;

public interface MergeableMetadata<T> {
  T merge(T override);
}
