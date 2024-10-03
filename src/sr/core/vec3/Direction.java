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
public final class Direction extends ThreeVector implements LinearOps<Direction> {
  
  /** 
   Factory method, taking the 3 components of the direction along the XYZ axes, in that order.
   The three components are allowed to be any vector, including the zero-vector.
   This method will use those three numbers to create a corresponding unit-vector, if needed.  
  */
  public static Direction of(double x, double y, double z) {
    ThreeVector result = ThreeVector.of(x, y, z);
    if (result.magnitude() > 0) {
      result = result.unitVector();
    }
    return new Direction(result.x(), result.y(), result.z());
  }

  /** Factory method. */
  public static Direction of(ThreeVector v) {
    return Direction.of(v.x(), v.y(), v.z());
  }

  /** Factory method. The vector has 1 non-zero component, along the given spatial coordinate axis. */
  public static Direction of(Axis axis) {
    return new Direction(axis);
  }

  /** No effect. */
  @Override public Direction reverseClocks() {
    return new Direction(components);
  }
  
  /** Reverse all components. */
  @Override public Direction reverseSpatialAxes() {
    Components comps = new ReverseSpatialComponents().applyTo(components);
    return new Direction(comps);
  }
  
  @Override public Direction rotate(AxisAngle axisAngle, Sense sense) {
    Components comps = Rotate.of(axisAngle, sense).applyTo(components);
    return new Direction(comps);
  }
  
  private Direction(double x, double y, double z) {
    super(x, y, z);
    check();
  }
  
  private Direction(Axis axis) {
    super(1, axis);
    check();
  }
  
  private Direction(Components comps) {
    super(comps);
    check();
  }
  
  private void check() {
    Util.equalsWithEpsilon(1.0, this.magnitude());
  }
}
