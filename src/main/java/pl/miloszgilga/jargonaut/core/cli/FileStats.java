package pl.miloszgilga.jargonaut.core.cli;

public class FileStats {
  private int count;
  private long bytes;

  public void add(long size) {
    count++;
    bytes += size;
  }

  public void addAdd(FileStats stats) {
    count += stats.count;
    bytes += stats.bytes;
  }

  public int getCount() {
    return count;
  }

  public long getBytes() {
    return bytes;
  }
}
