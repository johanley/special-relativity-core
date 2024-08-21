package sr.core.history.timelike;

/** 
 Points in time where histories are stitched together.
 The stitching is based on time, both coordinate-time and proper-time.
 For a given time, a specific {@link TimelikeHistory} is selected.
*/
final class TimelikeBranchPoint {
  
  static TimelikeBranchPoint of(double ct, double τ) {
    return new TimelikeBranchPoint(ct, τ);
  }
  
  double ct() { return ct; }
  double τ() { return τ; }
  
  private TimelikeBranchPoint(double ct, double τ) {
    this.ct = ct;
    this.τ = τ;
  }
  
  private double ct;
  private double τ;
}
