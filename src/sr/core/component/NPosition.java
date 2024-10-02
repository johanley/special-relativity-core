package sr.core.component;

import static sr.core.Util.mustHave;

import sr.core.Axis;
import sr.core.Util;
import sr.core.component.ops.NMoveZeroPointBy;
import sr.core.component.ops.NReverseSpatialComponents;
import sr.core.component.ops.NRotate;
import sr.core.component.ops.NSense;
import sr.core.ops.NAffineOp;
import sr.core.ops.NLinearOps;
import sr.core.vec3.NAxisAngle;
import sr.core.vec4.NFourDelta;

/** 
 The position of an object in space.
 <P>Note that a position is not a 3-vector.
*/
public final class NPosition implements NAffineOp<NPosition>, NLinearOps<NPosition> {

  /** Factory method. */
  public static final NPosition of(NComponents components) {
    return new NPosition(components);
  }
  
  /** Factory method, taking the 3 components of the position along the XYZ axes, in that order.  */
  public static NPosition of(double x, double y, double z) {
    return new NPosition(x, y, z);
  }

  /** Factory method. The vector has 1 non-zero component, along the given spatial coordinate axis. */
  public static NPosition of(Axis axis, double value) {
    return new NPosition(axis, value);
  }
  
  /** The origin of the coordinate system. */
  public static NPosition origin() {
    return new NPosition(0.0, 0.0, 0.0);
  }

  public double on(Axis axis) { return components.on(axis); }
  public double x() { return components.x(); }
  public double y() { return components.y(); }
  public double z() { return components.z(); }
  
  @Override public NPosition moveZeroPointBy(NFourDelta displacement, NSense sense) {
    NComponents comps = NMoveZeroPointBy.of(displacement, sense).applyTo(components);
    return NPosition.of(comps);
  }
  
  /** No effect. */
  @Override public NPosition reverseClocks() {
    return NPosition.of(components);
  }
  
  @Override public NPosition reverseSpatialAxes() {
    NComponents comps = new NReverseSpatialComponents().applyTo(components);
    return NPosition.of(comps);
  }
  
  @Override public NPosition rotate(NAxisAngle axisAngle, NSense sense) {
    NComponents comps = NRotate.of(axisAngle, sense).applyTo(components);
    return NPosition.of(comps);
  }
  
  /** This implementation applies rounding. */
  @Override public String toString() {
    String sep = ",";
    return "[" + 
      roundIt(x()) + sep + 
      roundIt(y()) + sep + 
      roundIt(z()) + 
    "]" ;
  }

  private NComponents components;
  
  private NPosition(double x, double y, double z) {
    this.components = NComponents.of(x, y, z);
  }
  
  private NPosition(NComponents components) {
    mustHave(components.hasSpaceOnly(), "Expecting 3 components for position: " + components.size());
    this.components = components;
  }
  
  private NPosition(Axis axis, double value) {
    Util.mustBeSpatial(axis);
    this.components = NComponents.of(0,0,0).overwrite(axis, value);
  }
  
  private double roundIt(double value) {
    return Util.round(value, 5);
  }
}
