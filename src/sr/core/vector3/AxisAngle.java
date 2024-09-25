package sr.core.vector3;

import sr.core.Axis;

/** 
 An axis-angle vector, used to define a rotation.
 
 <P>References:
 <ul> 
  <li><a href='https://en.wikipedia.org/wiki/Axis%E2%80%93angle_representation'>main</a>
  <li><a href='https://en.wikipedia.org/wiki/Rodrigues%27_rotation_formula'>Rodrigues' rotation formula</a>
 </ul>
 
 <P>For kinematic rotation (Wigner rotation), the axis-angle is proportional to the cross product of acceleration and velocity.
*/
public final class AxisAngle extends ThreeVectorImpl /*implements PseudoVector*/ {

  /** Factory method, taking the 3 components along the XYZ axes, in that order.  */
  public static AxisAngle of(double x, double y, double z) {
    return new AxisAngle(x, y, z);
  }

  /** Factory method. The vector has 1 non-zero component, along the given spatial coordinate axis. */
  public static AxisAngle of(Axis axis, double value) {
    return new AxisAngle(axis, value);
  }

  /*** Factory method, all components zero. */
  public static AxisAngle zero() {
    return AxisAngle.of(0,0,0);
  }

  private AxisAngle(double x, double y, double z) {
    super(x, y, z);
  }
  
  private AxisAngle(Axis axis, double value) {
    super(axis, value);
  }

}