package sr.core.history;

import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.FourVector;

/**
 Stationary object that doesn't change position in a given frame.
 The object's initial position is arbitrary.
 At proper time τmin the object has coordinate-time ct=0; 
*/
public final class StationaryParticle extends HistoryAbc {
  
  public StationaryParticle(double x0, double y0, double z0, double τmin, double τmax) {
    this.x0 = x0;
    this.y0 = y0;
    this.z0 = z0;
    this.τmin = τmin;
    this.τmax = τmax;
  }
  
  @Override protected FourVector eventFor(double τ) {
    // the proper time is the same as the coordinate-time, in this case, since the 
    // object isn't moving in the current frame
    return FourVector.from(Δτ(τ), x0, y0, z0, ApplyDisplaceOp.YES);
  }
  
  @Override protected FourVector fourVelocityFor(double τ) {
    return FourVector.from(1.0, 0.0, 0.0, 0.0, ApplyDisplaceOp.NO);
  }

  @Override public double β(double τ) { return 0.0; }
  @Override public double τmax() { return τmax; }
  @Override public double τmin() { return τmin; }
  
  @Override public String toString() {
    String sep = ",";
    return "["+ x0+sep + y0+sep + z0+sep+ τmin+sep + τmax+ "]";
  }
  
  private double x0; 
  private double y0; 
  private double z0; 
  private double τmin;
  private double τmax;
}
