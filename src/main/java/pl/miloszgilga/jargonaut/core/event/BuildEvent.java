package pl.miloszgilga.jargonaut.core.event;

import java.util.Locale;

public record BuildEvent(BuildEventType type, String phaseName, int totalItems, Object detailData) {
  public static BuildEvent startStage(String stageName) {
    return new BuildEvent(BuildEventType.STAGE_STARTED, stageName.toUpperCase(Locale.ROOT), 0, null);
  }

  public static BuildEvent startTask(String taskName) {
    return new BuildEvent(BuildEventType.SPINNER_STARTED, taskName, 0, null);
  }

  public static BuildEvent endTask(String taskName) {
    return new BuildEvent(BuildEventType.SPINNER_FINISHED, taskName, 0, null);
  }

  public static BuildEvent startPhase(String phaseName, int total) {
    return new BuildEvent(BuildEventType.PHASE_STARTED, phaseName, total, null);
  }

  public static BuildEvent tick(String phaseName, Object detail) {
    return new BuildEvent(BuildEventType.ITEM_PROCESSED, phaseName, 0, detail);
  }

  public static BuildEvent progress() {
    return new BuildEvent(BuildEventType.ITEM_PROCESSED, "", 0, null);
  }

  public static BuildEvent spinnerDetail(String message) {
    return new BuildEvent(BuildEventType.SPINNER_DETAIL, null, 0, message);
  }

  public static BuildEvent endPhase(String phaseName) {
    return new BuildEvent(BuildEventType.PHASE_FINISHED, phaseName, 0, null);
  }

  public static BuildEvent completeBuild() {
    return new BuildEvent(BuildEventType.BUILD_COMPLETED, "Ended", 0, null);
  }

  public static BuildEvent failedBuild(Throwable cause) {
    return new BuildEvent(BuildEventType.BUILD_FAILED, cause.getMessage(), 0, null);
  }
}
