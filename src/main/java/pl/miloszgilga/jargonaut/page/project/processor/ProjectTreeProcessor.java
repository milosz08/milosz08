package pl.miloszgilga.jargonaut.page.project.processor;

import pl.miloszgilga.jargonaut.config.model.Project;
import pl.miloszgilga.jargonaut.core.GenerationHaltedException;
import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.processor.ProcessorContext;
import pl.miloszgilga.jargonaut.core.processor.SiteBuildContext;
import pl.miloszgilga.jargonaut.core.processor.TaskProcessor;
import pl.miloszgilga.jargonaut.core.processor.md.MdProcessor;
import pl.miloszgilga.jargonaut.page.project.model.Category;
import pl.miloszgilga.jargonaut.page.project.model.CategoryMetadata;
import pl.miloszgilga.jargonaut.page.project.model.ProjectNodeMetadata;
import pl.miloszgilga.jargonaut.page.project.model.ProjectSharedData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

public class ProjectTreeProcessor extends MdProcessor {
  private final Path projectDir;
  private final ProcessorContext processorContext;

  public ProjectTreeProcessor(ProcessorContext processorContext) {
    super(processorContext.getBaseSitesDir(), processorContext);
    projectDir = processorContext.getBaseSitesDir().resolve("project");
    this.processorContext = processorContext;
  }

  @Override
  protected void process(SiteBuildContext context, Map<String, List<Path>> processedAssets, Locale defaultLocale) {
    final Project project = context.getConfig().getProject();
    try {
      final Category rootCategory = scanDirectory(projectDir, project.getRootCategoryId(), context, defaultLocale);
      final List<PageRepresentation<ProjectNodeMetadata>> allProjects = getAllProjectsSorted(rootCategory);
      final ProjectSharedData projectData = context.computeSharedDataIfAbsent(
        ProjectSharedData.class,
        ProjectSharedData::new
      );
      projectData.setRootProjectCategory(rootCategory);
      projectData.getAllProjects().addAll(allProjects);
      context.setCollectionPages(ProjectNodeMetadata.class, allProjects);
    } catch (IOException ex) {
      throw new GenerationHaltedException(ex);
    }
  }

  @Override
  protected boolean isBaseResourceExists() {
    return Files.exists(resourcePath);
  }

  private Category scanDirectory(Path dir,
                                 String categoryId,
                                 SiteBuildContext context,
                                 Locale defaultLocale) throws IOException {
    final Category category = new Category(categoryId);
    loadCategoryYaml(dir, category, context, defaultLocale);
    try (final Stream<Path> paths = Files.list(dir)) {
      final List<Path> subDirs = paths.filter(Files::isDirectory).toList();
      for (final Path subDir : subDirs) {
        if (hasIndexFiles(subDir)) {
          final TaskProcessor nodeProcessor = new ProjectNodeProcessor(subDir, category, processorContext);
          nodeProcessor.process(context);
        } else {
          final String subDirName = subDir.getFileName().toString().replace("[", "").replace("]", "");
          final Category subCategory = scanDirectory(subDir, subDirName, context, defaultLocale);
          if (!subCategory.getProjects().isEmpty() || !subCategory.getSubCategories().isEmpty()) {
            category.getSubCategories().add(subCategory);
          }
        }
      }
    }
    return category;
  }

  private List<PageRepresentation<ProjectNodeMetadata>> getAllProjectsSorted(Category category) {
    final List<PageRepresentation<ProjectNodeMetadata>> list = new ArrayList<>(category.getProjects());
    for (final Category subCategory : category.getSubCategories()) {
      list.addAll(getAllProjectsSorted(subCategory));
    }
    return list;
  }

  private void loadCategoryYaml(Path dir, Category category, SiteBuildContext context, Locale defaultLocale) {
    final Path defaultFile = dir.resolve("category.yml");
    if (!Files.exists(defaultFile)) {
      return;
    }
    final CategoryMetadata defaultConfig = yamlParser
      .parseAndValidateYamlFile(defaultFile, CategoryMetadata.class, true);
    if (defaultConfig == null) {
      return;
    }
    category.setWeight(defaultConfig.getWeight());
    category.getLocalizedNames().put(defaultLocale, defaultConfig.getName());
    for (final Locale locale : context.getActiveLocales()) {
      if (locale.equals(defaultLocale)) {
        continue;
      }
      final Path localizedFile = dir.resolve("category-" + locale + ".yml");
      if (Files.exists(localizedFile)) {
        final CategoryMetadata localizedConfig = yamlParser
          .parseAndValidateYamlFile(localizedFile, CategoryMetadata.class, false);
        if (localizedConfig != null) {
          final CategoryMetadata mergedConfig = defaultConfig.merge(localizedConfig);
          category.getLocalizedNames().put(locale, mergedConfig.getName());
          continue;
        }
      }
      category.getLocalizedNames().put(locale, defaultConfig.getName());
    }
  }

  @Override
  protected boolean isLoadingAssets() {
    return false;
  }

  @Override
  protected String getTargetWebDir() {
    return null;
  }
}
