package sr.explore.flyby;

/** Compute stats or summary data for an entire data-set. */
public interface OutputSummary {

  /** Process each data point. */
  void accept(DetectionEvent detection);

  /** Do something with the data-set at the end, after all the data have been consumed. */
  void render();
  
}
