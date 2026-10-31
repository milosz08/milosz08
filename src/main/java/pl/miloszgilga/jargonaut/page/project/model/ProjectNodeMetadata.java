package pl.miloszgilga.jargonaut.page.project.model;

import pl.miloszgilga.jargonaut.core.DataUtils;
import pl.miloszgilga.jargonaut.core.parser.GenericMdDocument;
import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;

import java.util.List;

public class ProjectNodeMetadata extends GenericMdDocument<ProjectNodeMetadata> {
  @YamlRequired
  private String title;

  @YamlRequired
  private String kind;

  @YamlRequired
  private String summary;

  @YamlRequired
  private List<String> tech;

  private List<String> techDetails;

  @YamlRequired
  private String status;

  private List<String> modifiers;

  @YamlRequired
  private Boolean pinned;

  @YamlRequired
  private Integer weight;

  private List<Link> repoUrls;

  private List<Link> demoUrls;

  private List<Link> downloadUrls;

  private List<Link> docsUrls;

  @Override
  protected void mergeAdditionalFields(ProjectNodeMetadata merged, ProjectNodeMetadata override) {
    merged.title = DataUtils.mergeValues(title, override.title);
    merged.kind = DataUtils.mergeValues(kind, override.kind);
    merged.summary = DataUtils.mergeValues(summary, override.summary);
    merged.tech = DataUtils.mergeValues(tech, override.tech);
    merged.techDetails = DataUtils.mergeValues(techDetails, override.techDetails);
    merged.status = DataUtils.mergeValues(status, override.status);
    merged.modifiers = DataUtils.mergeValues(modifiers, override.modifiers);
    merged.pinned = DataUtils.mergeValues(pinned, override.pinned);
    merged.weight = DataUtils.mergeValues(weight, override.weight);
    merged.repoUrls = DataUtils.mergeList(repoUrls, override.repoUrls);
    merged.demoUrls = DataUtils.mergeList(demoUrls, override.demoUrls);
    merged.downloadUrls = DataUtils.mergeList(downloadUrls, override.downloadUrls);
    merged.docsUrls = DataUtils.mergeList(docsUrls, override.docsUrls);
  }

  @Override
  protected ProjectNodeMetadata createInstance() {
    return new ProjectNodeMetadata();
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getKind() {
    return kind;
  }

  public void setKind(String kind) {
    this.kind = kind;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public List<String> getTech() {
    return tech;
  }

  public void setTech(List<String> tech) {
    this.tech = tech;
  }

  public List<String> getTechDetails() {
    return techDetails;
  }

  public void setTechDetails(List<String> techDetails) {
    this.techDetails = techDetails;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public List<String> getModifiers() {
    return modifiers;
  }

  public void setModifiers(List<String> modifiers) {
    this.modifiers = modifiers;
  }

  public Boolean getPinned() {
    return pinned;
  }

  public void setPinned(Boolean pinned) {
    this.pinned = pinned;
  }

  public Integer getWeight() {
    return weight;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
  }

  public List<Link> getRepoUrls() {
    return repoUrls;
  }

  public void setRepoUrls(List<Link> repoUrls) {
    this.repoUrls = repoUrls;
  }

  public List<Link> getDemoUrls() {
    return demoUrls;
  }

  public void setDemoUrls(List<Link> demoUrls) {
    this.demoUrls = demoUrls;
  }

  public List<Link> getDownloadUrls() {
    return downloadUrls;
  }

  public void setDownloadUrls(List<Link> downloadUrls) {
    this.downloadUrls = downloadUrls;
  }

  public List<Link> getDocsUrls() {
    return docsUrls;
  }

  public void setDocsUrls(List<Link> docsUrls) {
    this.docsUrls = docsUrls;
  }
}
