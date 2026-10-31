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
    final Stream<PageRepresentation<?>> singlePages = context.getAllSinglePages().values().stream();
    final Stream<PageRepresentation<?>> collectionPages = context.getAllCollectionPages().values().stream()
      .filter(Objects::nonNull)
      .flatMap(List::stream);

    final List<RenderedTextContent> allContents = Stream.concat(singlePages, collectionPages)
      .filter(page -> page != null && page.generatorFactory() != null)
      .parallel()
      .flatMap(page -> renderPageHelper(page, generatorContext).stream())
      .toList();

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

  private <T extends GenericPage> List<RenderedTextContent> renderPageHelper(PageRepresentation<T> page,
                                                                             GeneratorContext context) {
    final HtmlGeneratorFactory<T> factory = page.generatorFactory();
    final HtmlGenerator<T> generator = factory.create(page, context);
    final List<RenderedTextContent> rendered = generator.render();
    eventBus.publish(BuildEvent.progress());
    return rendered;
  }
}
