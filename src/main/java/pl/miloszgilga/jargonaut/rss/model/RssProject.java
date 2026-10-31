package pl.miloszgilga.jargonaut.rss.model;

import java.util.List;

public record RssProject(String title, String description, String url, String pubDate, List<String> tags) {
}
