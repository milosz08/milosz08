package pl.miloszgilga.jargonaut.page.education.processor;

import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.parser.ParsedDocument;
import pl.miloszgilga.jargonaut.core.processor.ProcessorContext;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.processor.md.PageMergingMdProcessor;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlGeneratorFactory;
import pl.miloszgilga.jargonaut.page.education.TimelineItem;
import pl.miloszgilga.jargonaut.page.education.TimelineItemComparator;
import pl.miloszgilga.jargonaut.page.education.generator.EducationHtmlGenerator;
import pl.miloszgilga.jargonaut.page.education.model.EducationMetadata;
import pl.miloszgilga.jargonaut.page.education.model.EducationSharedData;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EducationProcessor extends PageMergingMdProcessor<EducationMetadata> {
  private final Comparator<TimelineItem> timelineComparator;

  private PageRepresentation<EducationMetadata> parsedEducationPage;

  public EducationProcessor(ProcessorContext processorContext) {
    super("education", processorContext);
    timelineComparator = new TimelineItemComparator();
  }

  @Override
  protected void persistPageRepresentation(SiteBuildContext context, PageRepresentation<EducationMetadata> page) {
    super.persistPageRepresentation(context, page);
    parsedEducationPage = page;
  }

  @Override
  protected void process(SiteBuildContext context, Map<String, List<Path>> processedAssets, Locale defaultLocale) {
    super.process(context, processedAssets, defaultLocale);
    final EducationSharedData educationData = context.computeSharedDataIfAbsent(
      EducationSharedData.class,
      EducationSharedData::new
    );
    if (parsedEducationPage == null || parsedEducationPage.translations() == null) {
      return;
    }
    final Set<Map.Entry<Locale, ParsedDocument<EducationMetadata>>> translations = parsedEducationPage
      .translations().entrySet();
    educationData.setEducationItems(translations.stream()
      .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().metadata().getEducation()))
    );
    educationData.setResearchItems(translations.stream()
      .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().metadata().getResearch()))
    );
  }

  @Override
  protected Class<EducationMetadata> getTargetClass() {
    return EducationMetadata.class;
  }

  @Override
  protected HtmlGeneratorFactory<EducationMetadata> getHtmlRendererFactory() {
    return EducationHtmlGenerator::new;
  }

  @Override
  protected void onFileParsedHook(ParsedDocument<EducationMetadata> parsedDoc) {
    if (parsedDoc.metadata() != null) {
      parsedDoc.metadata().validateChronology();
    }
  }

  @Override
  protected void onPostMergeHook(EducationMetadata mergedMetadata) {
    mergedMetadata.sortItems(timelineComparator);
  }

  @Override
  public int order() {
    return HIGHEST_PRIORITY - 60;
  }
}
