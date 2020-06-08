package sr.core.history;

import static sr.core.Util.mustBeSpatial;
import static sr.core.Util.sq;
import static sr.core.Util.sqroot;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.FourVector;

/**
 Uniform linear acceleration along one of the spatial axes.
 
 Constant proper-acceleration.
 If riding in a rocket having this motion, an occupant feels a constant g-force.
 
 <P>At τ=τMin, the history crosses the origin-event, and the velocity is the zero-vector. 
*/
public final class UniformLinearAcceleration extends HistoryAbc {
  
  /** 
   Constructor.
   
   @param spatialAxis along which the object is accelerating in a straight line. The velocity 
   has no component along the other spatial axes.
   @param α the constant proper-acceleration of the object. Equals the g-force 
   felt if moving in a rocket having this proper-acceleration. 
  */
  public UniformLinearAcceleration(Axis spatialAxis, double α, double τMin, double τMax) {
    mustBeSpatial(spatialAxis);
    this.spatialAxis = spatialAxis;
    this.α = α;
    this.τMin = τMin;
    this.τMax = τMax;
  }
  
  @Override public double τmin() { return τMin; }
  @Override public double τmax() { return τMax; }
  public double α() { return α; }
  public Axis axis() { return spatialAxis; }

  /** 
   The event at proper-time τ of the object.
   For τ=τMin the the origin-event is returned.
  
   @param τ proper-time for the object.
  */
  @Override protected FourVector eventFor(double τ) {
    FourVector result = FourVector.ZERO_AFFINE; //default
    double c = 1.0; //to make it easy to compare with formulas in books
    //SHOULD THIS BE A COORD TRANSFORM, LIKE THE OTHERS?
    //not sure; for the moment, let's not try that
    double a = (c * c)/α;
    double b = (α * Δτ(τ))/c;
    double ct = a * Math.sinh(b);
    double distanceAlongAxis = a * Math.cosh(b) - a; 
    if (Axis.X == spatialAxis) {
      result = FourVector.from(ct, distanceAlongAxis, 0.0, 0.0, ApplyDisplaceOp.YES);
    }
    else if (Axis.Y == spatialAxis) {
      result = FourVector.from(ct, 0.0, distanceAlongAxis, 0.0, ApplyDisplaceOp.YES);
    }
    else if (Axis.Z == spatialAxis) {
      result = FourVector.from(ct, 0.0, 0.0, distanceAlongAxis, ApplyDisplaceOp.YES);
    }
    return result;
  }
  
  /** Varies with τ, and is always parallel to the given spatial axis. */
  @Override protected FourVector fourVelocityFor(double τ) {
    return Physics.fourVelocity(β(τ), spatialAxis);
  }
  
  /** Varies with τ.*/
  @Override public double β(double τ) {
    double t = event(τ).ct();
    double c = 1.0; //to make it easier to compare with formulas from books
    double αt = α * t;
    double result = αt / sqroot(1 + sq(αt/c));
    return result;
  }
  
  @Override public String toString() {
    String sep = ",";
    return "[" +spatialAxis+sep+ +α+sep + τMin+sep + τMax+  "]";
  }
  
  //PRIVATE
  
  private Axis spatialAxis;
  private double α;
  private double τMin;
  private double τMax;
}
