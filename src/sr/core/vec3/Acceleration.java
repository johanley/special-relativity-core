package sr.core.vec3;

import sr.core.Axis;
import sr.core.Util;
import sr.core.component.Components;
import sr.core.component.ops.ReverseSpatialComponents;
import sr.core.component.ops.Rotate;
import sr.core.component.ops.Sense;
import sr.core.ops.LinearOps;

/** 
 The acceleration of an object having mass.
*/
public final class Acceleration extends ThreeVector implements LinearOps<Acceleration> { 
  
  /** Factory method, taking the 3 components of the acceleration along the XYZ axes, in that order.  */
  public static Acceleration of(double ax, double ay, double az) {
    return new Acceleration(ax, ay, az);
  }

  /** Factory method for the case in which the acceleration is parallel to a coordinate axis. */
  public static Acceleration of(Axis axis, double a) {
    return new Acceleration(a, axis);
  }

  /** Factory method for the case in which the data is in some ThreeVector (due to a calculation). */
  public static Acceleration of(ThreeVector v) {
    return new Acceleration(v.x(), v.y(), v.z());
  }

  public static Acceleration zero() {
    return Acceleration.of(0.0, 0.0, 0.0);
  }

  /** Some cases only make sense when the acceleration is non-zero. */
  public static Acceleration nonZero(double x, double y, double z) {
    Acceleration result = Acceleration.of(x, y, z);
    Util.mustHave(result.magnitude() > 0, "Acceleration should have a non-zero magnitude.");
    return result;
  }

  /** No effect. */
  @Override public Acceleration reverseClocks() {
    return new Acceleration(components);
  }
  
  /** Reverse the sign of all spatial components.*/
  @Override public Acceleration reverseSpatialAxes() {
    Components comps = new ReverseSpatialComponents().applyTo(components);
    return new Acceleration(comps);
  }
  
  @Override public Acceleration rotate(AxisAngle axisAngle, Sense sense) {
    Components comps = Rotate.of(axisAngle, sense).applyTo(components);
    return new Acceleration(comps);
  }
  
  private Acceleration(double x, double y, double z) {
    super(x, y, z);
  }
  
  private Acceleration(double value, Axis axis) {
    super(value, axis);
  }
  
  private Acceleration(Components comps) {
    super(comps);
  }
}
