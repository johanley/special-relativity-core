package sr.core.vector;

import sr.core.Axis;

/** 
 The position of an object in space.
*/
public final class Position extends ThreeVectorImpl {

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

  private Position(double x, double y, double z) {
    super(x, y, z);
  }
  
  private Position(Axis axis, double value) {
    super(axis, value);
  }

}