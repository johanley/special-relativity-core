package sr.core.vector.transform;

import sr.core.Axis;
import sr.core.vector.AxisAngle;
import sr.core.vector.ThreeVector;

/** 
 Rotate around any axis, using an axis-angle pseudo-vector.
*/
public final class SpatialRotation implements SpatialTransform {

  /** 
   Factory method.
   @param axisAngle defines both the axis of rotation and the magnitude of the rotation. 
    Right-hand rules defined by {@link Axis#rightHandRuleFor(Axis)} give the sense of rotation for positive angle θ  
   (for negative θ, the sense of rotation is simply reversed).
  */
  public static SpatialRotation of(AxisAngle axisAngle) {
    return new SpatialRotation(axisAngle);
  }

  /** For case in which the axis of rotation is a coordinate axis. */
  public static SpatialRotation of(Axis axis, double angle) {
    return new SpatialRotation(axis, angle);
  }

  /** The frame of reference is rotated about the axis-angle. */
  @Override public ThreeVector changeFrame(ThreeVector v) {
    return transform(v, -1);
  }
  
  /** In a given frame of reference, the given vector is rotated about the axis-angle. */
  @Override public ThreeVector changeVector(ThreeVector vPrime) {
    return transform(vPrime, +1);
  }
  
  public AxisAngle axisAngle() { return axisAngle; }
  
  @Override public String toString() {
    return "rotation" + axisAngle;
  }
  
  // PRIVATE

  private AxisAngle axisAngle;

  private SpatialRotation(AxisAngle axisAngle) {
    this.axisAngle = axisAngle;
  }
  
  private SpatialRotation(Axis axis, double angle) {
    this(AxisAngle.of(axis, angle));
  }

  /** The reversal is simply reversing the sign of the angle θ (positive versus negative sense of rotation). */
  private ThreeVector transform(ThreeVector v, int sign) {
    //https://en.wikipedia.org/wiki/Rodrigues%27_rotation_formula
    ThreeVector result = v.copy();
    double θ = sign * θ();
    if (Math.abs(θ) > 0) {
      double cosθ = Math.cos(θ);
      double sinθ = Math.sin(θ);
      ThreeVector e = unitVector();
      
      ThreeVector a = v.times(cosθ); 
      ThreeVector b = e.cross(v).times(sinθ); 
      ThreeVector c = e.times((1 - cosθ) * e.dot(v));
      result = a.plus(b).plus(c);
    }
    return result;
  }
  
  private double θ() {
    return axisAngle.magnitude();
  }
  
  private ThreeVector unitVector() {
    //be careful of division by 0
    return axisAngle.times(1.0/θ());
  }
}
