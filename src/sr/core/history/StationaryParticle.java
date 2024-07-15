package sr.core.history;

import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.FourVector;

/**
 Stationary object that doesn't change position in a given frame.
 This is the simplest possible case.
 
 <P>The (XYZ) position doesn't change with changing values of the parameter τ.
 In this case, the τ parameter for the history is identified with both the <em>ct</em>-coordinate and with the proper-time.
 The proper time is taken as <em>0</em> for <em>ct=0</em>.
*/
public final class StationaryParticle extends HistoryAbc {
  
  /**
    Constructor.
     
    @param x0 the X-position
    @param y0 the Y-position
    @param z0 the Z-position
    @param τmin minimum value for the history's parameter
    @param τmax maximum value for the history's parameter
  */
  public StationaryParticle(double x0, double y0, double z0, double τmin, double τmax) {
    this.x0 = x0;
    this.y0 = y0;
    this.z0 = z0;
    this.τmin = τmin;
    this.τmax = τmax;
  }
  
  @Override protected FourVector eventFor(double τ) {
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
    return "stationary history [(x,y,z)=("+x0+sep+y0+sep+z0+sep+"), τmin=" + τmin+sep + "τmax=" +τmax+ "]";
  }
  
  private double x0; 
  private double y0; 
  private double z0; 
  private double τmin;
  private double τmax;
}
