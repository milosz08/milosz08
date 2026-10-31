package pl.miloszgilga.jargonaut.core.extractor.stats.github;

import java.util.List;
import java.util.Map;

public record GithubRepoStatsGraphQlRes(Map<String, Data> data) {
  public record Data(Languages languages, DefaultBranchRef defaultBranchRef) {
  }

  public record Languages(List<LanguageEdge> edges) {
  }

  public record LanguageEdge(long size, LanguageNode node) {
  }

  public record LanguageNode(String name, String color) {
  }

  public record DefaultBranchRef(Target target) {
  }

  public record Target(CommitHistory latestCommit, CommitHistory yearlyCommits) {
  }

  public record CommitHistory(List<CommitNode> nodes, PageInfo pageInfo) {
  }

  public record CommitNode(String committedDate) {
  }

  public record PageInfo(boolean hasNextPage, String endCursor) {
  }
}
