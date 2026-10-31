package pl.miloszgilga.jargonaut.core.processor.md;

import pl.miloszgilga.jargonaut.core.file.FileUtils;
import pl.miloszgilga.jargonaut.core.parser.DocumentParser;
import pl.miloszgilga.jargonaut.core.parser.yaml.YamlParser;
import pl.miloszgilga.jargonaut.core.processor.ProcessorContext;
import pl.miloszgilga.jargonaut.core.processor.ResourceLoaderProcessor;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class MdProcessor extends ResourceLoaderProcessor {
  protected final YamlParser yamlParser;
  protected final DocumentParser documentParser;

  protected MdProcessor(Path resourcePath, ProcessorContext processorContext) {
    super(resourcePath, processorContext.getAssetTransformer());
    yamlParser = processorContext.getYamlParser();
    documentParser = processorContext.getDocumentParser();
  }

  protected final boolean hasIndexFiles(Path dir) {
    return !getIndexFiles(dir).isEmpty();
  }

  protected final List<Path> getIndexFiles(Path dir) {
    return FileUtils.findFilesInDirectory(dir, ".md", p -> p.getFileName().toString().startsWith("index"));
  }

  @Override
  public final void processAfterLoad(SiteBuildContext context, Map<String, List<Path>> processedAssets) {
    process(context, processedAssets, context.getConfig().getDefaultLocale());
  }

  @Override
  protected boolean isBaseResourceExists() {
    return hasIndexFiles(resourcePath);
  }

  protected abstract void process(SiteBuildContext context,
                                  Map<String, List<Path>> processedAssets,
                                  Locale defaultLocale);
}
