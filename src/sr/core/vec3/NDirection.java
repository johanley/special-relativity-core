package sr.core.vec3;

import sr.core.Axis;
import sr.core.Util;
import sr.core.component.Components;
import sr.core.component.ops.ReverseSpatialComponents;
import sr.core.component.ops.Rotate;
import sr.core.component.ops.Sense;
import sr.core.ops.LinearOps;

/** 
 A direction in space as a unit vector.
 <P>This class was created for modeling the wave-vector <em>k<sup>i</sup></em> for light.
*/
public final class NDirection extends NThreeVector implements LinearOps<NDirection> {
  
  /** 
   Factory method, taking the 3 components of the direction along the XYZ axes, in that order.
   The three components are allowed to be any vector, including the zero-vector.
   This method will use those three numbers to create a corresponding unit-vector, if needed.  
  */
  public static NDirection of(double x, double y, double z) {
    NThreeVector result = NThreeVector.of(x, y, z);
    if (result.magnitude() > 0) {
      result = result.unitVector();
    }
    return new NDirection(result.x(), result.y(), result.z());
  }

  /** Factory method. */
  public static NDirection of(NThreeVector v) {
    return NDirection.of(v.x(), v.y(), v.z());
  }

  /** Factory method. The vector has 1 non-zero component, along the given spatial coordinate axis. */
  public static NDirection of(Axis axis) {
    return new NDirection(axis);
  }

  /** No effect. */
  @Override public NDirection reverseClocks() {
    return new NDirection(components);
  }
  
  /** Reverse all components. */
  @Override public NDirection reverseSpatialAxes() {
    Components comps = new ReverseSpatialComponents().applyTo(components);
    return new NDirection(comps);
  }
  
  @Override public NDirection rotate(NAxisAngle axisAngle, Sense sense) {
    Components comps = Rotate.of(axisAngle, sense).applyTo(components);
    return new NDirection(comps);
  }
  
  private NDirection(double x, double y, double z) {
    super(x, y, z);
    check();
  }
  
  private NDirection(Axis axis) {
    super(1, axis);
    check();
  }
  
  private NDirection(Components comps) {
    super(comps);
    check();
  }
  
  private void check() {
    Util.equalsWithEpsilon(1.0, this.magnitude());
  }
}
