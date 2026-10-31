package pl.miloszgilga.jargonaut;

import pl.miloszgilga.jargonaut.core.bootstrap.JArgonautBootstrapper;
import pl.miloszgilga.jargonaut.core.bootstrap.JArgonautEntrypoint;
import pl.miloszgilga.jargonaut.core.processor.ProcessorContext;
import pl.miloszgilga.jargonaut.core.processor.TaskProcessor;
import pl.miloszgilga.jargonaut.page.category.processor.CategoryNodeProcessor;
import pl.miloszgilga.jargonaut.page.category.processor.CategoryProcessor;
import pl.miloszgilga.jargonaut.page.education.processor.EducationProcessor;
import pl.miloszgilga.jargonaut.page.error.processor.ErrorProcessor;
import pl.miloszgilga.jargonaut.page.home.processor.HomeProcessor;
import pl.miloszgilga.jargonaut.page.privacypolicy.processor.PrivacyPolicyProcessor;
import pl.miloszgilga.jargonaut.page.project.processor.ProjectProcessor;
import pl.miloszgilga.jargonaut.page.project.processor.ProjectRepoProcessor;
import pl.miloszgilga.jargonaut.page.project.processor.ProjectTreeProcessor;
import pl.miloszgilga.jargonaut.page.tag.processor.TagNodeProcessor;
import pl.miloszgilga.jargonaut.page.tag.processor.TagProcessor;
import pl.miloszgilga.jargonaut.rss.ProjectsRssProcessor;
import pl.miloszgilga.jargonaut.rss.RssProcessor;

import java.util.List;

public class SsgMain implements JArgonautEntrypoint {
  public static void main(String[] args) {
    JArgonautBootstrapper.run(args, new SsgMain());
  }

  @Override
  public void attachPageParsers(JArgonautBootstrapper b, ProcessorContext pc, List<TaskProcessor> pageParsers) {
    pageParsers.add(new ProjectTreeProcessor(pc));
    pageParsers.add(new ProjectRepoProcessor(b.getRepoStatsExtractors()));
    pageParsers.add(new ProjectProcessor(pc));
    pageParsers.add(new TagProcessor(pc));
    pageParsers.add(new TagNodeProcessor(pc));
    pageParsers.add(new CategoryProcessor(pc));
    pageParsers.add(new CategoryNodeProcessor(pc));
    pageParsers.add(new EducationProcessor(pc));
    pageParsers.add(new HomeProcessor(pc, b.getUserContributionsExtractors()));
    pageParsers.add(new PrivacyPolicyProcessor(pc));
    pageParsers.add(new ErrorProcessor(pc));
  }

  @Override
  public RssProcessor rssProcessor(JArgonautBootstrapper b) {
    return new ProjectsRssProcessor(b.getBuildDateTime(), b.getTemplateEngine(), b.getXmlMinifier());
  }
}
