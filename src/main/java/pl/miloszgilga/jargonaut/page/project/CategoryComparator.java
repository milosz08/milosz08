package pl.miloszgilga.jargonaut.page.project;

import pl.miloszgilga.jargonaut.page.project.model.Category;

import java.util.Comparator;

public class CategoryComparator implements Comparator<Category> {
  @Override
  public int compare(Category g1, Category g2) {
    final int w1 = g1.getWeight() != null ? g1.getWeight() : 0;
    final int w2 = g2.getWeight() != null ? g2.getWeight() : 0;
    final int comp = Integer.compare(w1, w2);
    if (comp != 0) {
      return comp;
    }
    return g1.getId().compareToIgnoreCase(g2.getId());
  }
}
