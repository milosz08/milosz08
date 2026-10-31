package pl.miloszgilga.jargonaut.core.cli;

import me.tongfei.progressbar.ProgressBarConsumer;

public class SysOutProgressBarConsumer implements ProgressBarConsumer {
  private final int renderLength;

  public SysOutProgressBarConsumer(int renderLength) {
    this.renderLength = renderLength;
  }

  @Override
  public int getMaxRenderedLength() {
    return renderLength;
  }

  @Override
  public void accept(String rendered) {
    System.out.print("\r" + rendered);
    System.out.flush();
  }

  @Override
  public void close() {
    System.out.println();
    System.out.flush();
  }
}
