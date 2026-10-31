package pl.miloszgilga.jargonaut.core.cli;

import pl.miloszgilga.jargonaut.core.event.BuildEvent;
import pl.miloszgilga.jargonaut.core.event.EventBusListener;
import pl.miloszgilga.jargonaut.core.event.FileProcessedData;

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;

import java.util.ArrayList;
import java.util.List;

public class ConsoleUi implements EventBusListener {
  private static final int RENDER_LENGTH = 80;

  private final List<FileProcessedData> generatedFiles = new ArrayList<>();
  private final boolean verbose;

  private ProgressBar progressBar;
  private CliSpinner activeSpinner;
  private long startTime;
  private long phaseStartTime;
  private long spinnerStartTime;

  public ConsoleUi(boolean verbose) {
    this.verbose = verbose;
  }

  private void handleStageStarted(BuildEvent event) {
    if (activeSpinner != null) {
      activeSpinner.stopSuccess();
      activeSpinner = null;
    }
    if (startTime == 0) {
      startTime = System.currentTimeMillis();
    }
    System.out.println("\n" + Ansi.MAGENTA + Ansi.BOLD + "==> " + event.phaseName() + Ansi.RESET);
  }

  private void handleSpinnerStarted(BuildEvent event) {
    spinnerStartTime = System.currentTimeMillis();
    activeSpinner = new CliSpinner(event.phaseName());
    activeSpinner.start();
  }

  private void handleSpinnerFinished(BuildEvent event) {
    if (activeSpinner == null) {
      return;
    }
    activeSpinner.updateMessage(event.phaseName());
    activeSpinner.stopSuccess();
    activeSpinner = null;
    printDuration(spinnerStartTime);
  }

  private void handlePhaseStarted(BuildEvent event) {
    phaseStartTime = System.currentTimeMillis();
    progressBar = new ProgressBarBuilder()
      .setTaskName(event.phaseName())
      .setInitialMax(event.totalItems())
      .setConsumer(new SysOutProgressBarConsumer(RENDER_LENGTH))
      .setStyle(ProgressBarStyle.ASCII)
      .build();
  }

  private void handleItemProcessed(BuildEvent event) {
    if (progressBar != null) {
      progressBar.step();
    }
    if (event.detailData() instanceof FileProcessedData data) {
      generatedFiles.add(data);
    }
  }

  private void handleSpinnerDetails(BuildEvent event) {
    if (activeSpinner != null) {
      String message = (String) event.detailData();
      activeSpinner.updateMessage(message);
    }
  }

  private void handlePhaseFinished() {
    if (progressBar != null) {
      progressBar.stepTo(progressBar.getMax());
      progressBar.close();
      progressBar = null;
    }
    printDuration(phaseStartTime);
  }

  private void handleBuildCompleted() {
    final long duration = System.currentTimeMillis() - startTime;
    System.out.println("\n" + Ansi.GREEN + Ansi.BOLD + "DONE! Total time: " + duration + "ms" + Ansi.RESET);
    final long totalBytes = FileVisualizationRenderer.renderSummary(generatedFiles);
    FileVisualizationRenderer.renderTable(generatedFiles, totalBytes, RENDER_LENGTH);
    if (verbose && !generatedFiles.isEmpty()) {
      System.out.println("Output files structure:\n");
      FileVisualizationRenderer.renderTree(generatedFiles);
    }
  }

  private void handleBuildFailed() {
    if (activeSpinner == null) {
      return;
    }
    activeSpinner.stopError();
    activeSpinner = null;
  }

  private void printDuration(long startTimestamp) {
    final long duration = System.currentTimeMillis() - startTimestamp;
    System.out.println(Ansi.DIM + "  └─ Ended in " + duration + "ms" + Ansi.RESET);
  }

  @Override
  public void onEvent(BuildEvent event) {
    synchronized (System.out) {
      switch (event.type()) {
        case STAGE_STARTED -> handleStageStarted(event);
        case SPINNER_STARTED -> handleSpinnerStarted(event);
        case SPINNER_FINISHED -> handleSpinnerFinished(event);
        case PHASE_STARTED -> handlePhaseStarted(event);
        case ITEM_PROCESSED -> handleItemProcessed(event);
        case SPINNER_DETAIL -> handleSpinnerDetails(event);
        case PHASE_FINISHED -> handlePhaseFinished();
        case BUILD_COMPLETED -> handleBuildCompleted();
        case BUILD_FAILED -> handleBuildFailed();
      }
    }
  }
}
