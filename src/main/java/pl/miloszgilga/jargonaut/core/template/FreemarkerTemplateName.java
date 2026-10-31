package pl.miloszgilga.jargonaut.core.template;

public enum FreemarkerTemplateName implements TemplateName {
  // pages
  HOME_PAGE("pages/home.ftl"),
  PROJECT_PAGE("pages/project.ftl"),
  PROJECT_NODE_PAGE("pages/project-node.ftl"),
  EDUCATION_PAGE("pages/education.ftl"),
  TAG_PAGE("pages/tag.ftl"),
  TAG_NODE_PAGE("pages/tag-node.ftl"),
  CATEGORY_PAGE("pages/category.ftl"),
  CATEGORY_NODE_PAGE("pages/category-node.ftl"),
  PRIVACY_POLICY_PAGE("pages/privacy-policy.ftl"),
  ERROR_PAGE("pages/error.ftl"),
  // other
  TOC_PLACEHOLDER("fragments/toc.ftl"),
  RSS_FEED("rss/feed.ftlx"),
  ;

  private final String templateName;

  FreemarkerTemplateName(String templateName) {
    this.templateName = templateName;
  }

  @Override
  public String getTemplateName() {
    return templateName;
  }
}
