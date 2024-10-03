package sr.core.hist.timelike;

/** 
 Points in time where histories are stitched together.
 The stitching is based on time, both coordinate-time and proper-time.
 For a given time, a specific {@link TimelikeHistory} is selected.
*/
final class BranchPoint {
  
  static BranchPoint of(double ct, double τ) {
    return new BranchPoint(ct, τ);
  }
  
  double ct() { return ct; }
  double τ() { return τ; }
  
  private BranchPoint(double ct, double τ) {
    this.ct = ct;
    this.τ = τ;
  }
  
  private double ct;
  private double τ;
}
