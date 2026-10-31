package pl.miloszgilga.jargonaut.core.cli;

import pl.miloszgilga.jargonaut.core.event.FileProcessedData;

import java.util.Map;
import java.util.TreeMap;

public class FileTreeNode {
  private final String name;
  private final Map<String, FileTreeNode> children = new TreeMap<>();

  private FileProcessedData data;

  FileTreeNode(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public Map<String, FileTreeNode> getChildren() {
    return children;
  }

  public FileProcessedData getData() {
    return data;
  }

  public void setData(FileProcessedData data) {
    this.data = data;
  }
}
