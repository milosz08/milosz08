package pl.miloszgilga.jargonaut.core.template.renderer;

import pl.miloszgilga.jargonaut.core.TextContentType;
import pl.miloszgilga.jargonaut.core.event.BuildEvent;
import pl.miloszgilga.jargonaut.core.event.BuildEventBus;
import pl.miloszgilga.jargonaut.core.parser.GenericPage;
import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.processor.TaskProcessor;
import pl.miloszgilga.jargonaut.core.template.generator.GeneratorContext;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlGenerator;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlGeneratorFactory;
import pl.miloszgilga.jargonaut.core.template.generator.RenderedTextContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class HtmlRendererProcessor implements TaskProcessor {
  private final GeneratorContext generatorContext;
  private final BuildEventBus eventBus;

  public HtmlRendererProcessor(GeneratorContext generatorContext, BuildEventBus eventBus) {
    this.generatorContext = generatorContext;
    this.eventBus = eventBus;
  }

  @Override
  public void process(SiteBuildContext context) {
    final List<RenderedTextContent> allContents = new ArrayList<>();
    for (final PageRepresentation<?> page : context.getAllSinglePages().values()) {
      renderPage(page, generatorContext, allContents);
    }
    for (final List<PageRepresentation<?>> pages : context.getAllCollectionPages().values()) {
      if (pages == null) {
        continue;
      }
      for (final PageRepresentation<?> page : pages) {
        renderPage(page, generatorContext, allContents);
      }
    }
    context.getRenderedTextContents().put(TextContentType.HTML, allContents);
  }

  public int countTotalRenders(SiteBuildContext context) {
    return (int) Stream.concat(
        context.getAllSinglePages().values().stream(),
        context.getAllCollectionPages().values().stream()
          .filter(Objects::nonNull)
          .flatMap(List::stream)
      )
      .filter(page -> page != null && page.generatorFactory() != null)
      .count();
  }

  private void renderPage(PageRepresentation<?> page, GeneratorContext context, List<RenderedTextContent> output) {
    if (page == null || page.generatorFactory() == null) {
      return;
    }
    renderPageHelper(page, context, output);
  }

  private <T extends GenericPage> void renderPageHelper(PageRepresentation<T> page,
                                                        GeneratorContext context,
                                                        List<RenderedTextContent> output) {
    final HtmlGeneratorFactory<T> factory = page.generatorFactory();
    final HtmlGenerator<T> generator = factory.create(page, context);
    eventBus.publish(BuildEvent.progress());
    output.addAll(generator.render());
  }
}
