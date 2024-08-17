package sr.core.vector3;

import sr.core.Axis;
import sr.core.Util;

/** 
 The acceleration of an object having mass.
*/
public final class Acceleration extends ThreeVectorImpl {

  /** Factory method, taking the 3 components of the acceleration along the XYZ axes, in that order.  */
  public static Acceleration of(double ax, double ay, double az) {
    return new Acceleration(ax, ay, az);
  }
  
  /** Factory method for the case in which the acceleration is parallel to a coordinate axis. */
  public static Acceleration of(Axis axis, double ai) {
    return new Acceleration(axis, ai);
  }

  /** Factory method for the case in which the data is in some ThreeVector (due to a calculation). */
  public static Acceleration of(ThreeVector v) {
    return new Acceleration(v.x(), v.y(), v.z());
  }

  public static Acceleration zero() {
    return Acceleration.of(0.0, 0.0, 0.0);
  }
  
  /** Some cases only make sense when the acceleration is non-zero. */
  public static Acceleration nonZero(double ax, double ay, double az) {
    Acceleration result = Acceleration.of(ax, ay, az);
    Util.mustHave(result.magnitude() > 0, "Acceleration should have a non-zero magnitude.");
    return result;
  }
  
  private Acceleration(double x, double y, double z) {
    super(x, y, z);
  }
  
  private Acceleration(Axis axis, double value) {
    super(axis, value);
  }
}
