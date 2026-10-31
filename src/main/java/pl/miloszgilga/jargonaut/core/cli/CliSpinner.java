package pl.miloszgilga.jargonaut.core.cli;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CliSpinner implements Runnable {
  private static final String[] FRAMES = {"⠋", "⠙", "⠹", "⠸", "⠼", "⠴", "⠦", "⠧", "⠇", "⠏"};

  private final ScheduledExecutorService scheduler;
  private final AtomicInteger frameIndex = new AtomicInteger(0);

  private ScheduledFuture<?> scheduledTask;
  private volatile String message;

  public CliSpinner(String message) {
    this.message = message;
    this.scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
      Thread t = new Thread(r, "spinner-thread");
      t.setDaemon(true);
      return t;
    });
  }

  public void updateMessage(String newMessage) {
    this.message = newMessage;
  }

  public void start() {
    scheduledTask = scheduler.scheduleAtFixedRate(this, 0, 80, TimeUnit.MILLISECONDS);
  }

  public void stopSuccess() {
    stop();
    synchronized (System.out) {
      System.out.print("\r\033[K" + Ansi.GREEN + "✔" + Ansi.RESET + " " + message + "\n");
      System.out.flush();
    }
  }

  public void stopError() {
    stop();
    synchronized (System.out) {
      System.out.print("\r\033[K" + Ansi.RED + "✖" + Ansi.RESET + " " + message + "\n");
      System.out.flush();
    }
  }

  private void stop() {
    if (scheduledTask != null) {
      scheduledTask.cancel(true);
    }
    if (scheduler != null) {
      scheduler.shutdown();
    }
  }

  @Override
  public void run() {
    final int idx = frameIndex.getAndIncrement();
    synchronized (System.out) {
      System.out.print("\r\033[K" + Ansi.CYAN + FRAMES[idx % FRAMES.length] + Ansi.RESET + " " + message);
      System.out.flush();
    }
  }
}
