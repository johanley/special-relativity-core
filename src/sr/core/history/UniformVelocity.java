package sr.core.history;

import static sr.core.Util.mustBeSpatial;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.Boost;
import sr.core.transform.CoordTransform;
import sr.core.transform.FourVector;

/** 
 The object moves with uniform velocity along one of the spatial axes.

 <P>In this class, τ represents the proper time of the object.
 
 <P>At τ=τmin the history crosses the origin-event.
*/
public final class UniformVelocity extends HistoryAbc {

  /**
   Constructor.
   
   @param spatialAxis parallel to the uniform velocity of the object
   @param β the speed of the object along the axis; positive is parallel to the axis, negative is antiparallel
  */
  public UniformVelocity(Axis spatialAxis, double β, double τmin, double τmax) {
    mustBeSpatial(spatialAxis);
    this.spatialAxis = spatialAxis;
    this.β = β;
    this.τmin = τmin;
    this.τmax = τmax;
  }
  
  @Override public double τmin() { return τmin; }
  @Override public double τmax() { return τmax; }
  public Axis axis() { return spatialAxis; }

  /** 
   The event at proper-time τ of the object.
   For τ=τmin the the origin-event is returned.
   @param τ proper time for the object 
  */
  @Override protected FourVector eventFor(double τ) {
    //note: for a frame in which the object is stationary, the proper time is the same as the coordinate-time
    FourVector start = FourVector.from(Δτ(τ), 0.0, 0.0, 0.0, ApplyDisplaceOp.YES);
    CoordTransform boost = new Boost(spatialAxis, β);
    FourVector result = boost.toNewFourVector(start);
    return result;
  }

  /** 
   Constant 4-vector (independent of τ, in this case) determined by the speed and direction
   passed to the constructor.
   @param τ proper time for the object 
  */
  @Override protected FourVector fourVelocityFor(double τ) {
    return Physics.fourVelocity(β, spatialAxis);
  }

  /** 
   The speed passed to the constructor (independent of τ, in this case).
   @param τ proper time for the object 
  */
  @Override public double β(double τ) {
    return β;
  }
  
  @Override public String toString() {
    String sep = ",";
    return "[" +spatialAxis+sep +β+sep + τmin+sep + τmax + "]";
  }

  //PRIVATE 
  private Axis spatialAxis;
  private double β;
  private double τmin;
  private double τmax;

}
