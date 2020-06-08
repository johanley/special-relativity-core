package sr.core.history;

import static sr.core.Util.mustBeSpatial;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.CoordTransform;
import sr.core.transform.FourVector;
import sr.core.transform.Rotate;

/** 
 Uniform circular motion at constant speed.
 The origin-event is NOT included in this history.
*/
public final class UniformCircularMotion extends HistoryAbc {

  /**
    Constructor.
    
    @param spatialAxis the 'pole' about which the motion takes place. The motion is in a plane
    perpendicular to this axis. The positive sense of rotation is the same as that defined by the {@link Rotate} class, 
    with a right-hand rule.
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
   The Vector4 at proper-time τ of the object.
   For τ=τMin the the zero-vector is NOT returned: the event is located at a distance r from the origin, along 
   one of the spatial axes.
 
   @param τ proper-time for the object. 
  */
  @Override protected FourVector eventFor(double τ) {
    FourVector result = null;
    double ct = Physics.Γ(β) * Δτ(τ); //time dilation
    double ω = β/r;
    double θ = ω * ct; //θ=0 at τ=0; c=1 here
    //here we can simply use a coord transform to map from one FourVector to another, all in the same frame
    CoordTransform rotate = Rotate.about(spatialAxis, θ);
    FourVector start = FourVector.from(ct, 0.0, 0.0, 0.0, ApplyDisplaceOp.YES);
    start = start.put(spatialAxis, r);
    result = rotate.toNewFourVector(start);
    return result;
  }

  /*
   * The 4-velocity is not constant.
   * Its magnitude is constant, but its direction changes. 
   */
  
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
}
