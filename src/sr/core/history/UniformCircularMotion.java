package sr.core.history;

import static sr.core.Util.mustBeSpatial;

import java.util.List;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.CoordTransform;
import sr.core.transform.FourVector;
import sr.core.transform.Rotate;

/** 
 Uniform circular motion at constant speed.
 The origin-event is NOT included in this history.
 
 <P>In this class, τ represents the proper time of the object.
*/
public final class UniformCircularMotion extends HistoryAbc {

  /**
    Constructor.
    
    @param spatialAxis the 'pole' about which the motion takes place. 
    The motion is in a plane perpendicular to this axis. 
    The positive sense of rotation is that defined by {@link Axis#rightHandRuleFor(Axis)}.
    @param r the radius of the circle
    @param β the constant speed of the object, tangent to the circle; a negative value will reverse the sense of rotation.
    @param τMin starting τ 
    @param τMax ending τ
  */
  public UniformCircularMotion(Axis spatialAxis, double r, double β,  double τMin, double τMax) {
    mustBeSpatial(spatialAxis);
    this.spatialAxis = spatialAxis;
    this.r = r;
    this.β = β;
    this.τMin = τMin;
    this.τMax = τMax;
  }
  
  @Override public double τmin() { return τMin; }
  @Override public double τmax() { return τMax; }
  public double r() { return r; }
  public Axis axis() { return spatialAxis; }

  /** 
   The FourVector at proper-time τ of the object.
   For τ=τmin the the zero-vector is NOT returned: the event is located at a distance r from the origin, along 
   one of the spatial axes.
 
   @param τ proper-time for the object. 
  */
  @Override protected FourVector eventFor(double τ) {
    double ct = ct(τ); 
    //here we can simply use a coord transform to map from one FourVector to another, all in the same frame
    CoordTransform rotate = Rotate.about(spatialAxis, θ(ct));
    FourVector start = FourVector.from(ct, 0.0, 0.0, 0.0, ApplyDisplaceOp.YES);
    Axis startAxis = Axis.rightHandRuleFor(spatialAxis).get(0);
    start = start.put(startAxis, r);
    FourVector result = rotate.toNewFourVector(start);
    return result;
  }

  /** 
   The magnitude of the 4-velocity is constant, but its direction changes.
   @param τ proper-time for the object. 
  */
  @Override protected FourVector fourVelocityFor(double τ) {
    Physics.fourVelocity(β, spatialAxis);
    FourVector result = FourVector.ZERO_LINEAR;
    double Γ = Physics.Γ(β);
    result = result.put(Axis.CT, Γ);
    
    //there are two non-zero space components, that depend only on time
    double magnitude =  Γ * β;
    double ct = ct(τ); 
    double θ = θ(ct);
    double component1 = -magnitude * Math.sin(θ);
    double component2 = magnitude * Math.cos(θ);
    //the trick is to attach the above components to the correct 2 spatial axes
    List<Axis> axes = Axis.rightHandRuleFor(spatialAxis);
    result = result.put(axes.get(0), component1);
    result = result.put(axes.get(1), component2);
    return result;
  }  
  
  /** The speed passed to the constructor (independent of τ, in this case). */
  @Override public double β(double τ) {
    return β;
  }

  @Override public String toString() {
    String sep = ",";
    return "[" +spatialAxis+sep+ +r+sep+ β+sep+ τMin+sep + τMax+  "]";
  }
  
  //PRIVATE
  
  private Axis spatialAxis;
  private double r;
  private double β;
  private double τMin;
  private double τMax;
  
  private double ct(double τ) {
    return Physics.Γ(β) * Δτ(τ); //time dilation
  }
  
  /** Angle in rads, measured from the 'starting' spatial axis.*/
  private double θ(double ct) {
    double ω = β/r; //per length
    double result = ω * ct; //θ=0 at τ=0; c=1 here; rads
    return result;
  }
}