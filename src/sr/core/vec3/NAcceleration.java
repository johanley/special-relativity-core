package sr.core.vec3;

import sr.core.Axis;
import sr.core.Util;
import sr.core.component.NComponents;
import sr.core.component.ops.NReverseSpatialComponents;
import sr.core.component.ops.NRotate;
import sr.core.component.ops.NSense;
import sr.core.ops.NLinearOps;
import sr.core.vector3.ThreeVector;

public final class NAcceleration extends NThreeVector implements NLinearOps<NAcceleration> { 
  
  /** Factory method, taking the 3 components of the acceleration along the XYZ axes, in that order.  */
  public static NAcceleration of(double ax, double ay, double az) {
    return new NAcceleration(ax, ay, az);
  }

  /** Factory method for the case in which the acceleration is parallel to a coordinate axis. */
  public static NAcceleration of(Axis axis, double a) {
    return new NAcceleration(a, axis);
  }

  /** Factory method for the case in which the data is in some ThreeVector (due to a calculation). */
  public static NAcceleration of(ThreeVector v) {
    return new NAcceleration(v.x(), v.y(), v.z());
  }

  public static NAcceleration zero() {
    return NAcceleration.of(0.0, 0.0, 0.0);
  }

  /** Some cases only make sense when the acceleration is non-zero. */
  public static NAcceleration nonZero(double x, double y, double z) {
    NAcceleration result = NAcceleration.of(x, y, z);
    Util.mustHave(result.magnitude() > 0, "Acceleration should have a non-zero magnitude.");
    return result;
  }

  /** No effect. */
  @Override public NAcceleration reverseClocks() {
    return new NAcceleration(components);
  }
  
  /** Reverse the sign of all spatial components.*/
  @Override public NAcceleration reverseSpatialAxes() {
    NComponents comps = new NReverseSpatialComponents().applyTo(components);
    return new NAcceleration(comps);
  }
  
  @Override public NAcceleration rotate(NAxisAngle axisAngle, NSense sense) {
    NComponents comps = NRotate.of(axisAngle, sense).applyTo(components);
    return new NAcceleration(comps);
  }
  
  private NAcceleration(double x, double y, double z) {
    super(x, y, z);
  }
  
  private NAcceleration(double value, Axis axis) {
    super(value, axis);
  }
  
  private NAcceleration(NComponents comps) {
    super(comps);
  }
}
