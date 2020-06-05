package sr.core.history;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.transform.CoordTransform;
import sr.core.transform.Rotate;
import sr.core.transform.FourVector;

/** 
 Uniform circular motion at constant speed.
*/
public final class UniformCircularMotion implements History {

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
    if (!spatialAxis.isSpatial()) {
      throw new RuntimeException("You must use a spatial axis.");
    }
    this.spatialAxis = spatialAxis;
    this.r = r;
    this.β = β;
    this.τMin = τMin;
    this.τMax = τMax;
  }
  
  @Override public double τmin() { return τMin; }
  @Override public double τmax() { return τMax; }
  public double β() { return β; }
  public double r() { return r; }
  public Axis axis() { return spatialAxis; }


  /** 
   The Vector4 at proper-time τ of the object.
   For τ=τMin the the zero-vector is returned.
 
   @param τ proper-time for the object. 
  */
  @Override public FourVector event(double τ) {
    withinLimits(τ);
    FourVector result = null;
    double t = Physics.Γ(β) * Δτ(τ); //time dilation
    double ω = β/r;
    double θ = ω * t; //θ=0 at τ=0
    //here we can simply use a coord transform to map from one Vector4 to another, all in the same frame;
    CoordTransform rotate = new Rotate(spatialAxis, θ);
    FourVector start = null;
    if (Axis.Z == spatialAxis) {
      start = FourVector.from(0.0, r, 0.0, 0.0);
    }
    else if (Axis.Y == spatialAxis) {
      start = FourVector.from(0.0, 0.0, 0.0, r);
    }
    else if (Axis.X == spatialAxis) {
      start = FourVector.from(0.0, 0.0, r, 0.0);
    }
    result = rotate.toNewVector4(start);
    return result;
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
