package sr.core.vec3;

import sr.core.Axis;
import sr.core.component.Components;
import sr.core.component.ops.ReverseSpatialComponents;
import sr.core.component.ops.Rotate;
import sr.core.component.ops.Sense;
import sr.core.ops.LinearOps;

/** 
 An axis-angle vector, used to define a rotation.

 <P>References:
 <ul> 
  <li><a href='https://en.wikipedia.org/wiki/Axis%E2%80%93angle_representation'>main</a>
  <li><a href='https://en.wikipedia.org/wiki/Rodrigues%27_rotation_formula'>Rodrigues' rotation formula</a>
 </ul>

 <P>For kinematic rotation (Wigner rotation), the axis-angle is proportional to the cross product of acceleration and velocity.
*/
public final class AxisAngle extends ThreeVector implements LinearOps<AxisAngle> {
  
  /** Factory method, taking the 3 components (radians) along the XYZ axes, in that order.  */
  public static AxisAngle of(double x, double y, double z) {
    return new AxisAngle(x, y, z);
  }

  /** 
   Factory method. The vector has 1 non-zero component, along the given spatial coordinate axis.
   @param value radians. 
  */
  public static AxisAngle of(double value, Axis axis) {
    return new AxisAngle(value, axis);
  }

  /*** Factory method, all components zero. */
  public static AxisAngle zero() {
    return AxisAngle.of(0,0,0);
  }
  
  /** No effect. */
  @Override public AxisAngle reverseClocks() {
    return new AxisAngle(components);
  }
  
  /** Reverse all components. */
  @Override public AxisAngle reverseSpatialAxes() {
    Components comps = new ReverseSpatialComponents().applyTo(components);
    return new AxisAngle(comps);
  }
  
  @Override public AxisAngle rotate(AxisAngle axisAngle, Sense sense) {
    Components comps = Rotate.of(axisAngle, sense).applyTo(components);
    return new AxisAngle(comps);
  }

  private AxisAngle(double x, double y, double z) {
    super(x, y, z);
  }
  
  private AxisAngle(double angle, Axis axis) {
    super(angle, axis);
  }
  
  private AxisAngle(Components comps) {
    super(comps);
  }

}
