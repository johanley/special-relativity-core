package sr.core.component;

import static sr.core.Axis.CT;
import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;
import static sr.core.Util.mustHave;
import static sr.core.Util.round;

import sr.core.Axis;
import sr.core.component.ops.Boost;
import sr.core.component.ops.MoveZeroPointBy;
import sr.core.component.ops.ReverseSpatialComponents;
import sr.core.component.ops.ReverseTimeComponent;
import sr.core.component.ops.Rotate;
import sr.core.component.ops.Sense;
import sr.core.ops.AffineOp;
import sr.core.ops.LinearBoostOp;
import sr.core.ops.LinearOps;
import sr.core.vec3.AxisAngle;
import sr.core.vec3.Velocity;
import sr.core.vec4.FourDelta;

/** 
 An event in Minkowski space-time.
 <P>Note that an event is not a 4-vector.
*/
public final class Event implements AffineOp<Event>, LinearOps<Event>, LinearBoostOp<Event> {
  
  /** Factory method. */
  public static final Event of(Components components) {
    mustHave(components.hasSpaceAndTime(), "Expecting 4 components for event: " + components.size());
    return new Event(components);
  }
  
  /** Factory method. */
  public static final Event of(double ct, double x, double y, double z) {
    return new Event(ct, x, y, z);
  }

  /** Factory method. */
  public static final Event of(Double ct, Position position) {
    return new Event(ct, position.on(X), position.on(Y), position.on(Z));
  }
  
  /** The event at the origin of the coordinate system. */
  public static final Event origin() {
    return Event.of(0.0, Position.origin());
  }
  
  public double on(Axis axis) { return components.on(axis); }
  public double ct() { return on(CT); }
  public double x() { return on(X); }
  public double y() { return on(Y); }
  public double z() { return on(Z); }

  /** Return the position of the event. */
  public Position position(){
    return Position.of(on(X), on(Y), on(Z));
  }
  
  @Override public Event reverseClocks() {
    Components comps = new ReverseTimeComponent().applyTo(components);
    return Event.of(comps);
  }
  
  @Override public Event reverseSpatialAxes() {
    Components comps = new ReverseSpatialComponents().applyTo(components);
    return Event.of(comps);
  }
  
  @Override public Event rotate(AxisAngle axisAngle, Sense sense) {
    Components comps3 = Rotate.of(axisAngle, sense).applyTo(components);
    return Event.of(this.components.ct(), comps3.x(), comps3.y(), comps3.z());
  }
  
  @Override public Event boost(Velocity v, Sense sense) {
    Components comps = Boost.of(v, sense).applyTo(components);
    return Event.of(comps);
  }
  
  @Override public Event moveZeroPointBy(FourDelta displacement, Sense sense) {
    Components comps = MoveZeroPointBy.of(displacement, sense).applyTo(components);
    return Event.of(comps);
  }
  
  /** This implementation applies rounding. */
  @Override public String toString() {
    String sep = ",";
    String result = "[";
    for(Axis axis : Axis.values()) {
      result = result + roundIt(on(axis)) + sep + " ";
    }
    //chop off the final separator+space characters 
    result = result.substring(0, result.length() - 2);
    return result + "]";
  }
  
  // PRIVATE

  private Components components;

  private Event(Components components) {
    this.components = components;
  }

  private Event(Double ct, Double x, Double y, Double z) {
    components = Components.of(ct, x, y, z);
  }
  
  private double roundIt(Double val) {
    return round(val, 5);
  }
}
