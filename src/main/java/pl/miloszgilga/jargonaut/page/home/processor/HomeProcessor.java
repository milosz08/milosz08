package pl.miloszgilga.jargonaut.page.home.processor;

import pl.miloszgilga.jargonaut.core.extractor.contributions.ContributionCalendar;
import pl.miloszgilga.jargonaut.core.extractor.contributions.UserContributionsExtractor;
import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.parser.ParsedDocument;
import pl.miloszgilga.jargonaut.core.processor.ProcessorContext;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.processor.md.PageMergingMdProcessor;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlGeneratorFactory;
import pl.miloszgilga.jargonaut.page.home.generator.HomeHtmlGenerator;
import pl.miloszgilga.jargonaut.page.home.model.HomeMetadata;
import pl.miloszgilga.jargonaut.page.home.model.HomeSharedData;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeProcessor extends PageMergingMdProcessor<HomeMetadata> {
  private final List<UserContributionsExtractor> extractors;

  private PageRepresentation<HomeMetadata> parsedHomePage;

  public HomeProcessor(ProcessorContext processorContext, List<UserContributionsExtractor> extractors) {
    super("", processorContext);
    this.extractors = extractors;
  }

  @Override
  protected void persistPageRepresentation(SiteBuildContext context, PageRepresentation<HomeMetadata> page) {
    super.persistPageRepresentation(context, page);
    parsedHomePage = page;
  }

  @Override
  protected void process(SiteBuildContext context, Map<String, List<Path>> processedAssets, Locale defaultLocale) {
    super.process(context, processedAssets, defaultLocale);
    if (parsedHomePage == null || parsedHomePage.translations() == null) {
      return;
    }
    final Map<Locale, ContributionCalendar> localeCalendars = buildLocaleCalendars();
    saveCalendarsToSharedData(context, localeCalendars);
  }

  private Map<Locale, ContributionCalendar> buildLocaleCalendars() {
    final Map<String, ContributionCalendar> cachedCalendarsByUrl = new HashMap<>();
    final Map<Locale, ContributionCalendar> localeCalendars = new HashMap<>();
    for (final Map.Entry<Locale, ParsedDocument<HomeMetadata>> entry : parsedHomePage.translations().entrySet()) {
      final Locale locale = entry.getKey();
      final ParsedDocument<HomeMetadata> document = entry.getValue();
      if (document == null || document.metadata() == null) {
        continue;
      }
      final List<String> profileLinks = document.metadata().getContributionsGraph();
      if (profileLinks == null || profileLinks.isEmpty()) {
        continue;
      }
      final List<ContributionCalendar> extractedCalendars = fetchCalendarsForLinks(profileLinks, cachedCalendarsByUrl);
      if (!extractedCalendars.isEmpty()) {
        localeCalendars.put(locale, ContributionCalendar.mergeCalendars(extractedCalendars));
      }
    }
    return localeCalendars;
  }

  private List<ContributionCalendar> fetchCalendarsForLinks(List<String> profileLinks,
                                                            Map<String, ContributionCalendar> cache) {
    final List<ContributionCalendar> extractedCalendars = new ArrayList<>();
    for (final UserContributionsExtractor extractor : extractors) {
      for (final String link : profileLinks) {
        if (link != null && extractor.isUrlMatchPattern(link)) {
          ContributionCalendar calendar = cache.get(link);
          if (calendar == null) {
            final String principalId = extractor.extractPrincipalId(link);
            calendar = extractor.extractUserCalendar(principalId);
            if (calendar != null && calendar.totalContributions() > 0) {
              cache.put(link, calendar);
            }
          }
          if (calendar != null) {
            extractedCalendars.add(calendar);
          }
        }
      }
    }
    return extractedCalendars;
  }

  private void saveCalendarsToSharedData(SiteBuildContext context, Map<Locale, ContributionCalendar> localeCalendars) {
    if (localeCalendars.isEmpty()) {
      return;
    }
    HomeSharedData sharedData = context.getSharedData(HomeSharedData.class);
    if (sharedData == null) {
      sharedData = new HomeSharedData();
      context.setSharedData(HomeSharedData.class, sharedData);
    }
    sharedData.setContributionCalendars(localeCalendars);
  }

  @Override
  protected Class<HomeMetadata> getTargetClass() {
    return HomeMetadata.class;
  }

  @Override
  protected HtmlGeneratorFactory<HomeMetadata> getHtmlRendererFactory() {
    return HomeHtmlGenerator::new;
  }

  @Override
  public int order() {
    return HIGHEST_PRIORITY - 70;
  }
}
