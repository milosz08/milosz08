package pl.miloszgilga.jargonaut.core.cli;

import pl.miloszgilga.jargonaut.core.event.FileProcessedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileVisualizationRenderer {
  private static final int MIN_FILES_THRESHOLD = 1;

  private FileVisualizationRenderer() {
  }

  public static long renderSummary(List<FileProcessedData> files) {
    final long totalBytes = files.stream().mapToLong(FileProcessedData::sizeBytes).sum();
    final String totalSizeStr = formatSize(totalBytes);
    System.out.println(Ansi.DIM + "Total size: " + Ansi.RESET + Ansi.BOLD + totalSizeStr + Ansi.RESET + "\n");
    return totalBytes;
  }

  public static void renderTable(List<FileProcessedData> files, long totalBytes, int targetTableWidth) {
    final Map<String, FileStats> extStatsMap = new HashMap<>();
    for (final FileProcessedData file : files) {
      extStatsMap.computeIfAbsent(getExtension(file.targetPath()), k -> new FileStats()).add(file.sizeBytes());
    }
    final Map<String, FileStats> mainCategories = new HashMap<>();
    final FileStats otherStats = new FileStats();
    for (final Map.Entry<String, FileStats> entry : extStatsMap.entrySet()) {
      FileStats stats = entry.getValue();
      if (stats.getCount() >= MIN_FILES_THRESHOLD && !entry.getKey().isEmpty()) {
        mainCategories.put(entry.getKey(), stats);
      } else {
        otherStats.addAdd(stats);
      }
    }
    final List<Map.Entry<String, FileStats>> sortedCategories = new ArrayList<>(mainCategories.entrySet());
    sortedCategories.sort((a, b) -> Integer.compare(b.getValue().getCount(), a.getValue().getCount()));
    final String[] headers = {"FILE TYPE", "COUNT", "SIZE"};
    final List<String[]> rows = new ArrayList<>();
    for (final Map.Entry<String, FileStats> entry : sortedCategories) {
      rows.add(new String[]{
        entry.getKey(),
        String.valueOf(entry.getValue().getCount()),
        formatSize(entry.getValue().getBytes())});
    }
    if (otherStats.getCount() > 0) {
      rows.add(new String[]{
        "Other",
        String.valueOf(otherStats.getCount()),
        formatSize(otherStats.getBytes())});
    }
    final String[] totals = {"TOTAL", String.valueOf(files.size()), formatSize(totalBytes)};
    final int[] colWidths = new int[headers.length];
    for (int i = 0; i < headers.length; i++) {
      colWidths[i] = headers[i].length();
      for (String[] row : rows) {
        colWidths[i] = Math.max(colWidths[i], row[i].length());
      }
      colWidths[i] = Math.max(colWidths[i], totals[i].length());
      colWidths[i] = Math.max(colWidths[i], i == 0 ? 15 : 10);
    }
    if (targetTableWidth > 0) {
      int currentTotalWidth = 1 + (colWidths.length - 1) + 1;
      for (int w : colWidths) {
        currentTotalWidth += (w + 2);
      }
      if (currentTotalWidth < targetTableWidth) {
        int extraSpace = targetTableWidth - currentTotalWidth;
        colWidths[0] += extraSpace;
      }
    }
    System.out.println(buildSeparator("╭", "┬", "╮", colWidths));
    System.out.print(buildRow(headers, colWidths, true));
    System.out.println(buildSeparator("├", "┼", "┤", colWidths));
    for (final String[] row : rows) {
      System.out.print(buildRow(row, colWidths, false));
    }
    System.out.println(buildSeparator("├", "┼", "┤", colWidths));
    System.out.print(buildRow(totals, colWidths, true));
    System.out.println(buildSeparator("╰", "┴", "╯", colWidths) + "\n");
  }

  public static void renderTree(List<FileProcessedData> files) {
    final FileTreeNode root = new FileTreeNode("dist");
    for (final FileProcessedData file : files) {
      final String[] parts = file.targetPath().split("/");
      FileTreeNode current = root;
      for (final String part : parts) {
        current = current.getChildren().computeIfAbsent(part, FileTreeNode::new);
      }
      current.setData(file);
    }
    System.out.println(Ansi.CYAN + Ansi.BOLD + root.getName() + "/" + Ansi.RESET);
    drawNode(root, "");
  }

  private static String getExtension(String path) {
    int lastSlash = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
    int lastDot = path.lastIndexOf('.');

    if (lastDot > lastSlash && lastDot < path.length() - 1) {
      return path.substring(lastDot + 1).toUpperCase();
    }
    return "";
  }

  private static String buildSeparator(String left, String mid, String right, int[] widths) {
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(Ansi.DIM).append(left);
    for (int i = 0; i < widths.length; i++) {
      stringBuilder.repeat("─", widths[i] + 2);
      if (i < widths.length - 1) stringBuilder.append(mid);
    }
    stringBuilder.append(right).append(Ansi.RESET);
    return stringBuilder.toString();
  }

  private static String buildRow(String[] columns, int[] widths, boolean isBold) {
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(Ansi.DIM).append("│ ").append(Ansi.RESET);
    for (int i = 0; i < columns.length; i++) {
      final String align = (i == 0) ? "-" : "";
      final String boldPrefix = isBold ? Ansi.BOLD : "";
      final String fmt = boldPrefix + "%" + align + widths[i] + "s" + Ansi.RESET;
      stringBuilder.append(String.format(fmt, columns[i]));
      stringBuilder.append(Ansi.DIM).append(" │");
      if (i < columns.length - 1) {
        stringBuilder.append(" ");
      }
      stringBuilder.append(Ansi.RESET);
    }
    stringBuilder.append("\n");
    return stringBuilder.toString();
  }

  private static void drawNode(FileTreeNode node, String prefix) {
    final List<FileTreeNode> children = new ArrayList<>(node.getChildren().values());
    for (int i = 0; i < children.size(); i++) {
      final FileTreeNode child = children.get(i);
      final boolean isLast = (i == children.size() - 1);
      System.out.print(prefix + (isLast ? "└── " : "├── "));
      if (child.getData() == null) {
        System.out.println(Ansi.BOLD + child.getName() + "/" + Ansi.RESET);
        drawNode(child, prefix + (isLast ? "    " : "│   "));
      } else {
        final String sizeStr = formatSize(child.getData().sizeBytes());
        if (child.getData().fromCache()) {
          System.out.println(Ansi.GREEN + child.getName() + " " + sizeStr + Ansi.RESET + Ansi.DIM + " [cache]" +
            Ansi.RESET);
        } else {
          System.out.println(child.getName() + " " + Ansi.DIM + sizeStr + Ansi.RESET);
        }
      }
    }
  }

  private static String formatSize(long bytes) {
    if (bytes < 1024) {
      return bytes + " B";
    }
    return String.format("%.2f kB", bytes / 1024.0);
  }
}
