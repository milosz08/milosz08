package pl.miloszgilga.jargonaut.core.bootstrap;

import pl.miloszgilga.jargonaut.asset.AssetTransformer;
import pl.miloszgilga.jargonaut.asset.converter.AssetFileConverter;
import pl.miloszgilga.jargonaut.asset.resolver.AssetAliasResolver;
import pl.miloszgilga.jargonaut.asset.resolver.AssetResolutionStrategy;
import pl.miloszgilga.jargonaut.config.ConfigProcessor;
import pl.miloszgilga.jargonaut.core.cli.Ansi;
import pl.miloszgilga.jargonaut.core.cli.CliArgumentsParser;
import pl.miloszgilga.jargonaut.core.cli.ConsoleUi;
import pl.miloszgilga.jargonaut.core.event.BuildEvent;
import pl.miloszgilga.jargonaut.core.event.BuildEventBus;
import pl.miloszgilga.jargonaut.core.extractor.contributions.UserContributionsExtractor;
import pl.miloszgilga.jargonaut.core.extractor.stats.RepoStatsExtractor;
import pl.miloszgilga.jargonaut.core.minifier.html.HtmlMinifier;
import pl.miloszgilga.jargonaut.core.minifier.js.InlineJsMinifier;
import pl.miloszgilga.jargonaut.core.minifier.js.JsMinifier;
import pl.miloszgilga.jargonaut.core.minifier.styles.StylesMinifier;
import pl.miloszgilga.jargonaut.core.minifier.xml.XmlMinifier;
import pl.miloszgilga.jargonaut.core.parser.DocumentParser;
import pl.miloszgilga.jargonaut.core.parser.UrlNormalizer;
import pl.miloszgilga.jargonaut.core.parser.json.JsonParser;
import pl.miloszgilga.jargonaut.core.parser.yaml.YamlParser;
import pl.miloszgilga.jargonaut.core.polyglot.MathRenderer;
import pl.miloszgilga.jargonaut.core.polyglot.SyntaxHighlighter;
import pl.miloszgilga.jargonaut.core.processor.ProcessorContext;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.processor.TaskProcessor;
import pl.miloszgilga.jargonaut.core.template.TemplateEngine;
import pl.miloszgilga.jargonaut.core.template.generator.GeneratorContext;
import pl.miloszgilga.jargonaut.core.template.generator.HtmlPostProcessor;
import pl.miloszgilga.jargonaut.core.template.renderer.HtmlRendererProcessor;
import pl.miloszgilga.jargonaut.core.writer.AssetDiskWriter;
import pl.miloszgilga.jargonaut.core.writer.PrepareDirectoryDiskWriter;
import pl.miloszgilga.jargonaut.core.writer.TextContentDiskWriter;
import pl.miloszgilga.jargonaut.rss.RssProcessor;
import pl.miloszgilga.jargonaut.scripts.InlineScriptsProcessor;
import pl.miloszgilga.jargonaut.scripts.ScriptsProcessor;
import pl.miloszgilga.jargonaut.styles.StylesProcessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class JArgonautBootstrapper {
  private static final String CONFIG_FILE_NAME = "config.yml";

  private final CliArgumentsParser cliArgumentsParser;
  private final BuildEventBus eventBus;
  private final JArgonautEntrypoint entrypoint;
  private final SiteBuildContext siteBuildContext;
  private final ZonedDateTime buildDateTime;

  private Path baseDir;

  private AssetAliasResolver assetAliasResolver;
  private YamlParser yamlParser;
  private AssetTransformer assetTransformer;
  private AssetTransformer noopAssetTransformer;
  private StylesMinifier stylesMinifier;
  private JsMinifier jsMinifier;
  private HtmlMinifier htmlMinifier;
  private XmlMinifier xmlMinifier;
  private InlineJsMinifier inlineJsMinifier;
  private JsonParser jsonParser;
  private TemplateEngine templateEngine;
  private UrlNormalizer urlNormalizer;
  private SyntaxHighlighter syntaxHighlighter;
  private MathRenderer mathRenderer;
  private DocumentParser documentParser;
  private RssProcessor rssProcessor;

  private List<HtmlPostProcessor> htmlPostProcessors;
  private List<RepoStatsExtractor> repoStatsExtractors;
  private List<UserContributionsExtractor> userContributionsExtractors;

  private JArgonautBootstrapper(String[] args, JArgonautEntrypoint entrypoint) {
    this.entrypoint = entrypoint;
    cliArgumentsParser = new CliArgumentsParser(args);
    eventBus = new BuildEventBus();
    siteBuildContext = new SiteBuildContext(eventBus);
    buildDateTime = ZonedDateTime.now(ZoneOffset.UTC);
    start();
  }

  public static void run(String[] args, JArgonautEntrypoint entrypoint) {
    new JArgonautBootstrapper(args, entrypoint);
  }

  private void start() {
    try {
      eventBus.register(new ConsoleUi(cliArgumentsParser.getArg(CliArgumentsParser.Arg.VERBOSE)));

      assignBaseDir();
      runInitializationStage();
      readGlobalConfiguration();
      initializeDataExtractors();
      initializeGraalVmEngine();
      initializeMarkdownRenderer();
      runPreParsingAndPreparation();
      runParsingContent();
      runRenderingContent();
      generateAndSaveOutputFiles();

      eventBus.publish(BuildEvent.completeBuild());

      syntaxHighlighter.close();
      mathRenderer.close();
    } catch (Exception ex) {
      eventBus.publish(BuildEvent.failedBuild(ex));
      System.err.println("\n" + Ansi.RED + "Error: " + ex.getMessage() + Ansi.RESET);
      System.exit(1);
    }
  }

  private void assignBaseDir() throws IOException {
    final String inputPath = cliArgumentsParser.getArg(CliArgumentsParser.Arg.INPUT);
    baseDir = Path.of(inputPath);
    if (!Files.exists(baseDir)) {
      Files.createDirectory(baseDir);
    }
  }

  private void runInitializationStage() {
    eventBus.publish(BuildEvent.startStage("initialization"));
    eventBus.publish(BuildEvent.startTask("Creating base system components..."));

    final List<AssetResolutionStrategy> assetResolutionStrategies = new ArrayList<>();
    entrypoint.attachAssetResolutionStrategies(this, assetResolutionStrategies);
    assetAliasResolver = new AssetAliasResolver(baseDir, assetResolutionStrategies, eventBus);
    yamlParser = entrypoint.yamlParser(this);

    final List<AssetFileConverter> assetFileConverters = new ArrayList<>();
    entrypoint.attachAssetFileConverters(this, assetFileConverters);
    assetTransformer = entrypoint.assetTransformer(this, assetFileConverters);

    noopAssetTransformer = entrypoint.noopAssetTransformer(this);
    stylesMinifier = entrypoint.stylesMinifier(this);
    jsMinifier = entrypoint.jsMinifier(this);
    htmlMinifier = entrypoint.htmlMinifier(this);
    xmlMinifier = entrypoint.xmlMinifier(this);
    inlineJsMinifier = entrypoint.inlineJsMinifier(this);
    jsonParser = entrypoint.jsonParser(this);
    templateEngine = entrypoint.templateEngine(this);
    urlNormalizer = entrypoint.urlNormalizer(this);

    htmlPostProcessors = new ArrayList<>();
    entrypoint.attachHtmlPostProcessors(this, htmlPostProcessors);

    eventBus.publish(BuildEvent.endTask("Created base system components"));
  }

  private void readGlobalConfiguration() {
    eventBus.publish(BuildEvent.startTask("Read global SSG configurations..."));
    final ConfigProcessor configProcessor = new ConfigProcessor(baseDir.resolve(CONFIG_FILE_NAME), yamlParser);
    configProcessor.process(siteBuildContext);
    eventBus.publish(BuildEvent.endTask("Read global SSG configurations"));
  }

  private void initializeDataExtractors() {
    eventBus.publish(BuildEvent.startTask("Initializing data extractors..."));
    repoStatsExtractors = new ArrayList<>();
    entrypoint.attachRepoStatsExtractor(this, repoStatsExtractors);
    userContributionsExtractors = new ArrayList<>();
    entrypoint.attachUserContributionsExtractor(this, userContributionsExtractors);
    eventBus.publish(BuildEvent.endTask("Initialized data extractors"));
  }

  private void initializeGraalVmEngine() {
    eventBus.publish(BuildEvent.startTask("Initialize GraalVM JavaScript engine..."));
    syntaxHighlighter = entrypoint.syntaxHighlighter(this);
    mathRenderer = entrypoint.mathRenderer(this);
    eventBus.publish(BuildEvent.endTask("Initialized GraalVM JavaScript engine"));
  }

  private void initializeMarkdownRenderer() {
    eventBus.publish(BuildEvent.startTask("Initialize Markdown renderer..."));
    documentParser = entrypoint.documentParser(this);
    eventBus.publish(BuildEvent.endTask("Initialized Markdown renderer"));
  }

  private void runPreParsingAndPreparation() {
    eventBus.publish(BuildEvent.startStage("pre-parsing & preparation"));
    final List<TaskProcessor> preParsers = new ArrayList<>();
    entrypoint.attachPreParsers(this, preParsers);
    eventBus.publish(BuildEvent.startPhase("Pre-parsing", preParsers.size()));
    for (final TaskProcessor preParser : preParsers) {
      preParser.process(siteBuildContext);
      eventBus.publish(BuildEvent.progress());
    }
    eventBus.publish(BuildEvent.endPhase("Pre-parsing"));
  }

  private void runParsingContent() {
    eventBus.publish(BuildEvent.startStage("parsing & rendering content"));
    final ProcessorContext processorContext = ProcessorContext.builder()
      .baseSitesDir(baseDir.resolve(siteBuildContext.getConfig().getDir().getSites()))
      .yamlParser(yamlParser)
      .documentParser(documentParser)
      .assetTransformer(assetTransformer)
      .urlNormalizer(urlNormalizer)
      .build();
    final List<TaskProcessor> pageParsers = new ArrayList<>();
    entrypoint.attachPageParsers(this, processorContext, pageParsers);
    pageParsers.sort(TaskProcessor.COMPARATOR);
    eventBus.publish(BuildEvent.startPhase("Parse HTML content", pageParsers.size()));
    for (final TaskProcessor pageParser : pageParsers) {
      pageParser.process(siteBuildContext);
      eventBus.publish(BuildEvent.progress());
    }
    eventBus.publish(BuildEvent.endPhase("Parse HTML content"));

    eventBus.publish(BuildEvent.startTask("Parse RSS+XML content..."));
    rssProcessor = entrypoint.rssProcessor(this);
    rssProcessor.process(siteBuildContext);
    eventBus.publish(BuildEvent.endTask("Parsed RSS+XML content"));
  }

  private void runRenderingContent() {
    eventBus.publish(BuildEvent.startStage("Render content"));

    eventBus.publish(BuildEvent.startTask("Merging and render styles..."));
    final TaskProcessor stylesProcessor = new StylesProcessor(
      baseDir,
      siteBuildContext,
      stylesMinifier,
      assetAliasResolver);
    stylesProcessor.process(siteBuildContext);
    eventBus.publish(BuildEvent.endTask("Merged and rendered styles"));

    eventBus.publish(BuildEvent.startTask("Render scripts..."));
    final List<TaskProcessor> scriptsProcessors = List.of(
      new ScriptsProcessor(baseDir, siteBuildContext, jsMinifier),
      new InlineScriptsProcessor(baseDir, siteBuildContext)
    );
    for (final TaskProcessor scriptsProcessor : scriptsProcessors) {
      scriptsProcessor.process(siteBuildContext);
    }
    eventBus.publish(BuildEvent.endTask("Rendered scripts"));

    final GeneratorContext generatorContext = GeneratorContext.builder()
      .buildDateTime(buildDateTime)
      .jsonParser(jsonParser)
      .templateEngine(templateEngine)
      .htmlMinifier(htmlMinifier)
      .inlineJsMinifier(inlineJsMinifier)
      .assetAliasResolver(assetAliasResolver)
      .siteBuildContext(siteBuildContext)
      .addPostProcessors(htmlPostProcessors)
      .rssProcessor(rssProcessor)
      .build();

    final HtmlRendererProcessor htmlRendererProcessor = new HtmlRendererProcessor(generatorContext, eventBus);
    final int totalRenderers = htmlRendererProcessor.countTotalRenders(siteBuildContext);
    eventBus.publish(BuildEvent.startPhase("Render HTML content", totalRenderers));
    htmlRendererProcessor.process(siteBuildContext);
    eventBus.publish(BuildEvent.endPhase("Render HTML content"));
  }

  private void generateAndSaveOutputFiles() {
    eventBus.publish(BuildEvent.startStage("Generate and save output files"));

    eventBus.publish(BuildEvent.startTask("Prepare output directory..."));
    final TaskProcessor prepareDirectory = new PrepareDirectoryDiskWriter();
    prepareDirectory.process(siteBuildContext);
    eventBus.publish(BuildEvent.endTask("Prepared output directory"));

    final List<TaskProcessor> postProcessors = new ArrayList<>();
    postProcessors.add(new TextContentDiskWriter());
    postProcessors.add(new AssetDiskWriter());
    for (final TaskProcessor taskProcessor : postProcessors) {
      taskProcessor.process(siteBuildContext);
    }
  }

  public BuildEventBus getEventBus() {
    return eventBus;
  }

  public SiteBuildContext getSiteBuildContext() {
    return siteBuildContext;
  }

  public ZonedDateTime getBuildDateTime() {
    return buildDateTime;
  }

  public Path getBaseDir() {
    return baseDir;
  }

  public AssetAliasResolver getAssetAliasResolver() {
    return assetAliasResolver;
  }

  public YamlParser getYamlParser() {
    return yamlParser;
  }

  public AssetTransformer getAssetTransformer() {
    return assetTransformer;
  }

  public AssetTransformer getNoopAssetTransformer() {
    return noopAssetTransformer;
  }

  public StylesMinifier getStylesMinifier() {
    return stylesMinifier;
  }

  public JsMinifier getJsMinifier() {
    return jsMinifier;
  }

  public HtmlMinifier getHtmlMinifier() {
    return htmlMinifier;
  }

  public XmlMinifier getXmlMinifier() {
    return xmlMinifier;
  }

  public InlineJsMinifier getInlineJsMinifier() {
    return inlineJsMinifier;
  }

  public JsonParser getJsonParser() {
    return jsonParser;
  }

  public TemplateEngine getTemplateEngine() {
    return templateEngine;
  }

  public UrlNormalizer getUrlNormalizer() {
    return urlNormalizer;
  }

  public SyntaxHighlighter getSyntaxHighlighter() {
    return syntaxHighlighter;
  }

  public MathRenderer getMathRenderer() {
    return mathRenderer;
  }

  public DocumentParser getDocumentParser() {
    return documentParser;
  }

  public List<RepoStatsExtractor> getRepoStatsExtractors() {
    return repoStatsExtractors;
  }

  public List<UserContributionsExtractor> getUserContributionsExtractors() {
    return userContributionsExtractors;
  }
}
