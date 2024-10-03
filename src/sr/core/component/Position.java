package sr.core.component;

import static sr.core.Util.mustHave;

import sr.core.Axis;
import sr.core.Util;
import sr.core.component.ops.MoveZeroPointBy;
import sr.core.component.ops.ReverseSpatialComponents;
import sr.core.component.ops.Rotate;
import sr.core.component.ops.Sense;
import sr.core.ops.AffineOp;
import sr.core.ops.LinearOps;
import sr.core.vec3.NAxisAngle;
import sr.core.vec4.NFourDelta;

/** 
 The position of an object in space.
 <P>Note that a position is not a 3-vector.
*/
public final class Position implements AffineOp<Position>, LinearOps<Position> {

  /** Factory method. */
  public static final Position of(Components components) {
    return new Position(components);
  }
  
  /** Factory method, taking the 3 components of the position along the XYZ axes, in that order.  */
  public static Position of(double x, double y, double z) {
    return new Position(x, y, z);
  }

  /** Factory method. The vector has 1 non-zero component, along the given spatial coordinate axis. */
  public static Position of(Axis axis, double value) {
    return new Position(axis, value);
  }
  
  /** The origin of the coordinate system. */
  public static Position origin() {
    return new Position(0.0, 0.0, 0.0);
  }

  public double on(Axis axis) { return components.on(axis); }
  public double x() { return components.x(); }
  public double y() { return components.y(); }
  public double z() { return components.z(); }
  
  @Override public Position moveZeroPointBy(NFourDelta displacement, Sense sense) {
    Components comps = MoveZeroPointBy.of(displacement, sense).applyTo(components);
    return Position.of(comps);
  }
  
  /** No effect. */
  @Override public Position reverseClocks() {
    return Position.of(components);
  }
  
  @Override public Position reverseSpatialAxes() {
    Components comps = new ReverseSpatialComponents().applyTo(components);
    return Position.of(comps);
  }
  
  @Override public Position rotate(NAxisAngle axisAngle, Sense sense) {
    Components comps = Rotate.of(axisAngle, sense).applyTo(components);
    return Position.of(comps);
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

  private Components components;
  
  private Position(double x, double y, double z) {
    this.components = Components.of(x, y, z);
  }
  
  private Position(Components components) {
    mustHave(components.hasSpaceOnly(), "Expecting 3 components for position: " + components.size());
    this.components = components;
  }
  
  private Position(Axis axis, double value) {
    Util.mustBeSpatial(axis);
    this.components = Components.of(0,0,0).overwrite(axis, value);
  }
  
  private double roundIt(double value) {
    return Util.round(value, 5);
  }
}
