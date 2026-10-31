package pl.miloszgilga.jargonaut.page.tag.generator;

import pl.miloszgilga.jargonaut.core.WordCloudUtils;
import pl.miloszgilga.jargonaut.core.dto.AlphabeticalGroup;
import pl.miloszgilga.jargonaut.core.dto.AlphabeticalGroupView;
import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.parser.ParsedDocument;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.template.FreemarkerTemplateName;
import pl.miloszgilga.jargonaut.core.template.TemplateName;
import pl.miloszgilga.jargonaut.core.template.generator.GeneratorContext;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlGenerator;
import pl.miloszgilga.jargonaut.globals.model.GlobalsMetadata;
import pl.miloszgilga.jargonaut.page.project.model.ProjectSharedData;
import pl.miloszgilga.jargonaut.page.tag.dto.TagCloudView;
import pl.miloszgilga.jargonaut.page.tag.dto.TagView;
import pl.miloszgilga.jargonaut.page.tag.model.TagMetadata;
import pl.miloszgilga.jargonaut.page.tag.model.TagSharedData;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TagHtmlGenerator extends HtmlGenerator<TagMetadata> {
  public TagHtmlGenerator(PageRepresentation<TagMetadata> pageRepresentation,
                          GeneratorContext generatorContext) {
    super(pageRepresentation, generatorContext);
  }

  @Override
  protected Object generateJsonLdScript(String canonicalUrl,
                                        String language,
                                        GlobalsMetadata globals,
                                        ParsedDocument<TagMetadata> document) {
    return createBaseSchema("CollectionPage", canonicalUrl, language, document)
      .add("author", createPersonSchema(canonicalUrl, globals));
  }

  @Override
  protected TemplateName getHtmlTemplateName() {
    return FreemarkerTemplateName.TAG_PAGE;
  }

  @Override
  protected void insertPageAttributes(Map<String, Object> pageAttributes,
                                      Locale locale,
                                      ParsedDocument<TagMetadata> document,
                                      SiteBuildContext context) {
    pageAttributes.put("groupedTags", createGroupedTagViews(context, locale));
    pageAttributes.put("tagCloud", createTagCloudViews(context, locale));
  }

  private List<AlphabeticalGroup<TagView>> createGroupedTagViews(SiteBuildContext context,
                                                                 Locale locale) {
    final ProjectSharedData projectData = context.getSharedData(ProjectSharedData.class);
    final TagSharedData tagData = context.getSharedData(TagSharedData.class);
    if (projectData == null || tagData == null) {
      return Collections.emptyList();
    }
    final List<TagView> allTags = tagData.getTagItems().get(locale).stream()
      .map(tag -> {
        final long count = projectData.getAllProjects().stream()
          .filter(p -> p.translations().get(locale).metadata().getTech().contains(tag.name()))
          .count();
        return new TagView(tag, (int) count);
      })
      .toList();
    final AlphabeticalGroupView<TagView> view = new AlphabeticalGroupView<>(allTags, TagView::name,
      TagView::projectsCount);
    return view.getAlphabeticalGroups();
  }

  private List<TagCloudView> createTagCloudViews(SiteBuildContext context, Locale locale) {
    final ProjectSharedData projectData = context.getSharedData(ProjectSharedData.class);
    final TagSharedData tagData = context.getSharedData(TagSharedData.class);
    if (projectData == null || tagData == null) {
      return Collections.emptyList();
    }
    final List<WordCloudUtils.CloudItemData> tempTags = tagData.getTagItems().get(locale).stream()
      .map(tag -> {
        final long count = projectData.getAllProjects().stream()
          .filter(p -> p.translations().get(locale).metadata().getTech().contains(tag.name()))
          .count();
        return new WordCloudUtils.CloudItemData(tag.name(), tag.slug(), (int) count);
      })
      .toList();
    return WordCloudUtils.buildCloud(tempTags, (item, fontSize) ->
      new TagCloudView(item.name(), item.slug(), fontSize)
    );
  }
}
