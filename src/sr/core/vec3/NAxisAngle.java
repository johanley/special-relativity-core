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
public final class NAxisAngle extends NThreeVector implements LinearOps<NAxisAngle> {
  
  /** Factory method, taking the 3 components along the XYZ axes, in that order.  */
  public static NAxisAngle of(double x, double y, double z) {
    return new NAxisAngle(x, y, z);
  }

  /** Factory method. The vector has 1 non-zero component, along the given spatial coordinate axis. */
  public static NAxisAngle of(double value, Axis axis) {
    return new NAxisAngle(value, axis);
  }

  /*** Factory method, all components zero. */
  public static NAxisAngle zero() {
    return NAxisAngle.of(0,0,0);
  }
  
  /** No effect. */
  @Override public NAxisAngle reverseClocks() {
    return new NAxisAngle(components);
  }
  
  /** Reverse all components. */
  @Override public NAxisAngle reverseSpatialAxes() {
    Components comps = new ReverseSpatialComponents().applyTo(components);
    return new NAxisAngle(comps);
  }
  
  @Override public NAxisAngle rotate(NAxisAngle axisAngle, Sense sense) {
    Components comps = Rotate.of(axisAngle, sense).applyTo(components);
    return new NAxisAngle(comps);
  }

  private NAxisAngle(double x, double y, double z) {
    super(x, y, z);
  }
  
  private NAxisAngle(double angle, Axis axis) {
    super(angle, axis);
  }
  
  private NAxisAngle(Components comps) {
    super(comps);
  }

}
