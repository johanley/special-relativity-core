package sr.core.vector;

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
  
  public static Acceleration zero() {
    return Acceleration.of(0.0, 0.0, 0.0);
  }
  
  /** Some cases only make sense when the acceleration is non-zero. */
  public static Acceleration nonZero(double ax, double ay, double az) {
    Acceleration result = Acceleration.of(ax, ay, az);
    Util.mustHave(result.magnitude() > 0, "Vector should have a non-zero magnitude.");
    return result;
  }
  
  private Acceleration(double xComp, double yComp, double zComp) {
    super(xComp, yComp, zComp);
  }
  
  private Acceleration(Axis axis, double value) {
    super(axis, value);
  }
}
