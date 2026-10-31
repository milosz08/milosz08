package pl.miloszgilga.jargonaut.rss;

import pl.miloszgilga.jargonaut.config.model.Config;
import pl.miloszgilga.jargonaut.core.processor.TaskProcessor;

public interface RssProcessor extends TaskProcessor {
  String getRssFeedUrl(Config config);
}
