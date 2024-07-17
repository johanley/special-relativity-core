package sr.core;

import java.util.LinkedHashMap;
import java.util.Map;

import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.FourVector;

/** The position of an object. */
public final class Position {

  /**
   Factory method.
   @param x coord along the X-axis. 
   @param y coord along the Y-axis. 
   @param z coord along the Z-axis. 
  */
  public static Position from(double x, double y, double z) {
    return new Position(x, y, z);
  }

  public static Position origin() {
    return new Position(0.0, 0.0, 0.0);
  }

  /** The component of the position along the X-axis. */
  public double x() {
    return coords.get(Axis.X);
  }
  /** The component of the position along the Y-axis. */
  public double y() {
    return coords.get(Axis.Y);
  }
  /** The component of the position along the Z-axis. */
  public double z() {
    return coords.get(Axis.Z);
  }
  
  /** Replace one component of the position with the given value. Returns a new object. */
  public Position put(Axis axis, Double value) {
    Util.mustBeSpatial(axis);
    //copy this object's data to start with
    Map<Axis, Double> c = new LinkedHashMap<>();
    for(Axis a: Axis.values()) {
      c.put(a, coords.get(a));
    }
    c.put(axis, value); //then override one value
    return new Position(c.get(Axis.X), c.get(Axis.Y), c.get(Axis.Z));
  }
  
  /** Return an event having this position and the given <em>ct</em> coordinate. */
  public FourVector eventForTime(double ct) {
    return FourVector.from(ct, x(), y(), z(), ApplyDisplaceOp.YES);
  }

  private Map</*spatial*/Axis, Double> coords = new LinkedHashMap<>();

  private Position(double x, double y, double z) {
    this.coords.put(Axis.X, x);
    this.coords.put(Axis.Y, y);
    this.coords.put(Axis.Z, z);
  }
}