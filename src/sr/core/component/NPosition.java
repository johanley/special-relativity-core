package sr.core.component;

import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;
import static sr.core.Util.mustHave;

import sr.core.Axis;
import sr.core.Util;
import sr.core.component.ops.NMoveZeroPointBy;
import sr.core.component.ops.NSense;
import sr.core.ops.NAffineOp;
import sr.core.ops.NLinearOps;
import sr.core.vec3.NAxisAngle;
import sr.core.vec4.NFourDelta;

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
    NMoveZeroPointBy op = NMoveZeroPointBy.of(displacement, sense);
    NComponents comps = op.applyTo(components);
    return NPosition.of(comps);
  }
  
  @Override public NPosition reverseClocks() {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override public NPosition reverseSpatialAxes() {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override public NPosition rotate(NAxisAngle axisAngle, NSense sense) {
    // TODO Auto-generated method stub
    return null;
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
}
