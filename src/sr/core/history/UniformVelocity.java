package sr.core.history;

import sr.core.Axis;
import sr.core.transform.Boost;
import sr.core.transform.CoordTransform;
import sr.core.transform.FourVector;

/** 
 The object moves with uniform velocity along one of the spatial axes.

 <P>At τ=τMin the history crosses the origin-event.
*/
public final class UniformVelocity implements History {

  /**
   Constructor.
   
   @param spatialAxis parallel to the uniform velocity of the object
   @param β the speed of the object along the axis; positive is parallel to the axis, negative is antiparallel
  */
  public UniformVelocity(Axis spatialAxis, double β, double τMin, double τMax) {
    if (!spatialAxis.isSpatial()) {
      throw new RuntimeException("You must use a spatial axis.");
    }
    this.spatialAxis = spatialAxis;
    this.β = β;
    this.τMin = τMin;
    this.τMax = τMax;
  }
  
  @Override public double τmin() { return τMin; }
  @Override public double τmax() { return τMax; }
  public double β() { return β; }
  public Axis axis() { return spatialAxis; }

  
  /** 
   The event at proper-time τ of the object.
   For τ=τMin the the origin-event is returned.
  */
  @Override public FourVector event(double τ) {
    withinLimits(τ);
    FourVector start = FourVector.from(Δτ(τ), 0.0, 0.0, 0.0);
    CoordTransform boost = new Boost(spatialAxis, β);
    FourVector result = boost.toNewVector4(start);
    return result;
  }
  
  @Override public String toString() {
    String sep = ",";
    return "[" +spatialAxis+sep +β+sep + τMin+sep + τMax + "]";
  }
  
  private Axis spatialAxis;
  private double β;
  private double τMin;
  private double τMax;

}
