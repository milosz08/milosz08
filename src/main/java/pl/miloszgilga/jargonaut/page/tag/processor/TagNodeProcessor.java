package pl.miloszgilga.jargonaut.page.tag.processor;

import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.parser.ParsedDocument;
import pl.miloszgilga.jargonaut.core.parser.UrlNormalizer;
import pl.miloszgilga.jargonaut.core.processor.ProcessorContext;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.processor.md.DynamicPageData;
import pl.miloszgilga.jargonaut.core.processor.md.DynamicPageMergingMdProcessor;
import pl.miloszgilga.jargonaut.core.template.generator.GeneratorContext;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlGenerator;
import pl.miloszgilga.jargonaut.page.project.model.ProjectNodeMetadata;
import pl.miloszgilga.jargonaut.page.tag.TagComparator;
import pl.miloszgilga.jargonaut.page.tag.generator.TagNodeHtmlGenerator;
import pl.miloszgilga.jargonaut.page.tag.model.TagItem;
import pl.miloszgilga.jargonaut.page.tag.model.TagNodeMetadata;
import pl.miloszgilga.jargonaut.page.tag.model.TagSharedData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TagNodeProcessor extends DynamicPageMergingMdProcessor<TagNodeMetadata> {
  private final UrlNormalizer urlNormalizer;
  private final Comparator<TagItem> tagCloudItemComparator;

  public TagNodeProcessor(ProcessorContext processorContext) {
    super("tag", processorContext);
    urlNormalizer = processorContext.getUrlNormalizer();
    tagCloudItemComparator = new TagComparator();
  }

  @Override
  protected List<DynamicPageData> provideDynamicValues(SiteBuildContext context, Locale locale) {
    final List<PageRepresentation<ProjectNodeMetadata>> projects = context.getCollectionPages(ProjectNodeMetadata.class);
    if (projects == null || projects.isEmpty()) {
      return List.of();
    }
    final TagSharedData tagData = context.computeSharedDataIfAbsent(TagSharedData.class, TagSharedData::new);
    if (tagData.getTagItems().isEmpty()) {
      final Map<String, Map<Locale, TagItem>> tagsBySlug = extractTagsBySlug(projects);
      final Map<Locale, List<TagItem>> tagItemsLocale = new HashMap<>();
      for (final Map<Locale, TagItem> translations : tagsBySlug.values()) {
        populateTagItems(translations, tagItemsLocale);
      }
      for (final List<TagItem> tagItems : tagItemsLocale.values()) {
        tagItems.sort(tagCloudItemComparator);
      }
      tagData.getTagItems().putAll(tagItemsLocale);
    }
    final List<TagItem> currentLocaleTags = tagData.getTagItems().get(locale);
    if (currentLocaleTags == null || currentLocaleTags.isEmpty()) {
      return List.of();
    }
    final List<DynamicPageData> dynamicPages = new ArrayList<>();
    for (final TagItem item : currentLocaleTags) {
      dynamicPages.add(new DynamicPageData(item.slug(), item.name()));
    }
    return dynamicPages;
  }

  @Override
  protected HtmlGenerator<TagNodeMetadata> createHtmlGenerator(PageRepresentation<TagNodeMetadata> page,
                                                               GeneratorContext context,
                                                               String slug) {
    return new TagNodeHtmlGenerator(page, context, slug);
  }

  @Override
  protected Class<TagNodeMetadata> getTargetClass() {
    return TagNodeMetadata.class;
  }

  @Override
  public int order() {
    return HIGHEST_PRIORITY - 30;
  }

  private Map<String, Map<Locale, TagItem>> extractTagsBySlug(List<PageRepresentation<ProjectNodeMetadata>> projects) {
    final Map<String, Map<Locale, TagItem>> tagsBySlug = new HashMap<>();
    for (final PageRepresentation<ProjectNodeMetadata> project : projects) {
      if (project.translations() == null) {
        continue;
      }
      for (final Map.Entry<Locale, ParsedDocument<ProjectNodeMetadata>> entry : project.translations().entrySet()) {
        final Locale locale = entry.getKey();
        final ParsedDocument<ProjectNodeMetadata> doc = entry.getValue();
        if (doc == null || doc.metadata() == null || doc.metadata().getTech() == null) {
          continue;
        }
        for (final String tag : doc.metadata().getTech()) {
          if (tag != null && !tag.isBlank()) {
            registerSingleTag(tag, locale, tagsBySlug);
          }
        }
      }
    }
    return tagsBySlug;
  }

  private void registerSingleTag(String tag, Locale locale, Map<String, Map<Locale, TagItem>> tagsBySlug) {
    final String normalizedName = tag.trim();
    final String slug = urlNormalizer.normalize(normalizedName);
    tagsBySlug
      .computeIfAbsent(slug, k -> new HashMap<>())
      .compute(locale, (loc, existingTag) -> existingTag == null
        ? new TagItem(normalizedName, slug, 1)
        : new TagItem(normalizedName, slug, existingTag.count() + 1)
      );
  }

  private void populateTagItems(Map<Locale, TagItem> translations, Map<Locale, List<TagItem>> tagClouds) {
    for (final Map.Entry<Locale, TagItem> translation : translations.entrySet()) {
      final Locale locale = translation.getKey();
      final TagItem tagItem = translation.getValue();
      tagClouds.computeIfAbsent(locale, k -> new ArrayList<>()).add(tagItem);
    }
  }
}
