package pl.miloszgilga.jargonaut.page.project;

import pl.miloszgilga.jargonaut.config.model.Config;
import pl.miloszgilga.jargonaut.core.GenerationHaltedException;
import pl.miloszgilga.jargonaut.core.parser.PageRepresentation;
import pl.miloszgilga.jargonaut.core.parser.ParsedDocument;
import pl.miloszgilga.jargonaut.page.project.model.ProjectNodeMetadata;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ProjectComparator implements Comparator<PageRepresentation<ProjectNodeMetadata>> {
  private final Locale locale;
  private final List<String> modifierOrder;
  private final List<String> statusOrder;

  public ProjectComparator(Locale locale, Config config) {
    this.locale = locale;
    modifierOrder = sanitizeList(config.getProject().getModifierOrder());
    statusOrder = sanitizeList(config.getProject().getStatusOrder());
  }

  private List<String> sanitizeList(List<String> list) {
    return list != null
      ? list.stream().map(String::trim).map(String::toLowerCase).collect(Collectors.toList())
      : List.of();
  }

  @Override
  public int compare(PageRepresentation<ProjectNodeMetadata> p1, PageRepresentation<ProjectNodeMetadata> p2) {
    final ProjectNodeMetadata m1 = getPrimaryMetadata(p1);
    final ProjectNodeMetadata m2 = getPrimaryMetadata(p2);
    // 1. modifiers
    final int modPri1 = getModifierPriority(m1);
    final int modPri2 = getModifierPriority(m2);
    if (modPri1 != modPri2) {
      return Integer.compare(modPri1, modPri2);
    }
    // 2. project status
    final int statusPri1 = getStatusPriority(m1);
    final int statusPri2 = getStatusPriority(m2);
    if (statusPri1 != statusPri2) {
      return Integer.compare(statusPri1, statusPri2);
    }
    // 3. project weight
    final int w1 = m1 != null && m1.getWeight() != null ? m1.getWeight() : 0;
    final int w2 = m2 != null && m2.getWeight() != null ? m2.getWeight() : 0;
    final int weightComparison = Integer.compare(w1, w2);
    if (weightComparison != 0) {
      return weightComparison;
    }
    // 4. alphabetically
    final String title1 = m1 != null && m1.getTitle() != null ? m1.getTitle() : p1.id();
    final String title2 = m2 != null && m2.getTitle() != null ? m2.getTitle() : p2.id();
    return title1.compareToIgnoreCase(title2);
  }

  private int getModifierPriority(ProjectNodeMetadata metadata) {
    if (metadata == null || metadata.getModifiers() == null || metadata.getModifiers().isEmpty()) {
      return Integer.MAX_VALUE;
    }
    int highestPriority = Integer.MAX_VALUE;
    for (final String modifier : metadata.getModifiers()) {
      final String normalizedModifier = modifier.trim().toLowerCase();
      if (!modifierOrder.contains(normalizedModifier)) {
        throw new GenerationHaltedException("Unknown modifier: '" + modifier + "' for project: " + metadata.getTitle());
      }
      final int index = modifierOrder.indexOf(normalizedModifier);
      if (index < highestPriority) {
        highestPriority = index;
      }
    }
    return highestPriority;
  }

  private int getStatusPriority(ProjectNodeMetadata metadata) {
    if (metadata == null || metadata.getStatus() == null) {
      return Integer.MAX_VALUE;
    }
    final String normalizedStatus = metadata.getStatus().trim().toLowerCase();
    if (!statusOrder.contains(normalizedStatus)) {
      throw new GenerationHaltedException("Unknown status: '" + metadata.getStatus() + "' for project: " +
        metadata.getTitle());
    }
    return statusOrder.indexOf(normalizedStatus);
  }

  private ProjectNodeMetadata getPrimaryMetadata(PageRepresentation<ProjectNodeMetadata> project) {
    if (project.translations() == null || project.translations().isEmpty()) {
      return null;
    }
    ParsedDocument<ProjectNodeMetadata> translation = project.translations().get(locale);
    if (translation == null) {
      translation = project.translations().values().iterator().next();
    }
    return translation != null ? translation.metadata() : null;
  }
}
