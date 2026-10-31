package pl.miloszgilga.jargonaut.config.model;

import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;

import java.util.ArrayList;
import java.util.List;

public class Project {
  @YamlRequired
  private String rootCategoryId;

  @YamlRequired
  private List<String> modifierOrder = new ArrayList<>();

  @YamlRequired
  private List<String> statusOrder = new ArrayList<>();

  @YamlRequired
  private Integer recentlyUpdatedDeltaDays;

  public Project() {
  }

  public String getRootCategoryId() {
    return rootCategoryId;
  }

  public void setRootCategoryId(String rootCategoryId) {
    this.rootCategoryId = rootCategoryId;
  }

  public List<String> getModifierOrder() {
    return modifierOrder;
  }

  public void setModifierOrder(List<String> modifierOrder) {
    this.modifierOrder = modifierOrder;
  }

  public List<String> getStatusOrder() {
    return statusOrder;
  }

  public void setStatusOrder(List<String> statusOrder) {
    this.statusOrder = statusOrder;
  }

  public Integer getRecentlyUpdatedDeltaDays() {
    return recentlyUpdatedDeltaDays;
  }

  public void setRecentlyUpdatedDeltaDays(Integer recentlyUpdatedDeltaDays) {
    this.recentlyUpdatedDeltaDays = recentlyUpdatedDeltaDays;
  }
}
