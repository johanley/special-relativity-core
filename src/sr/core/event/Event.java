package sr.core.event;

import static sr.core.Axis.CT;
import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;
import static sr.core.Util.equalsWithEpsilon;
import static sr.core.Util.round;
import static sr.core.Util.sq;

import java.util.LinkedHashMap;
import java.util.Map;

import sr.core.Axis;
import sr.core.vector.Position;

/** 
 An event in Minkowski space-time.
 
 Design note: currently, no attempt is made to model 4-vectors in general.
 This project models kinematics, not dynamics (4-momentum, 4-velocity, and so on.)
*/
public final class Event {

  /** 
   Factory method.
   Since this library uses units in which c=1 always, note that the 0th coord can be taken 
   either as t, or ct. I prefer the label ct.
  
   <P>If you are working in less than 3 spatial dimensions, then pass 0 for the unused spatial coordinates.
  */
  public static final Event of(double ct, double x, double y, double z) {
    return new Event(ct, x, y, z);
  }
  
  public static final Event of(Double ct, Position position) {
    return new Event(ct, position.on(X), position.on(Y), position.on(Z));
  }
  
  public static final Event origin() {
    return new Event(0.0, 0.0, 0.0, 0.0);
  }
  
  /** Replace one component of this event, and build a new object in doing so. */
  public Event put(Axis axis, Double val) {
    Map<Axis, Double> updated = new LinkedHashMap<>();
    updated.putAll(components);
    updated.put(axis, val);
    return Event.of(
      updated.get(CT), 
      updated.get(X), 
      updated.get(Y), 
      updated.get(Z) 
    );
  }
  
  /** Return one component of this event, corresponding to the given axis. */
  public double on(Axis axis) {
    return components.get(axis);
  }

  /** This event plus 'that' event (for each component). */
  public final Event plus(Event that) {
    return Event.of(
      ct() + that.ct(), 
      x()  + that.x(), 
      y()  + that.y(), 
      z()  + that.z() 
    );
  }
  
  /** This event minus 'that' event (for each component). */
  public final Event minus(Event that) {
    return Event.of(
      ct() - that.ct(), 
      x()  - that.x(), 
      y()  - that.y(), 
      z()  - that.z() 
    );
  }

  /** 
   The squared-magnitude of this event (an invariant quantity).
   The fundamental quadratic form.
   <P>Can be positive or negative! This projects uses the signature of (ct,x,y,z) (+,-,-,-).
  */
  public double square() {
    return dot(this);
  }

  /** Magnitude of this event's spatial part (its 3-vector). Always positive. */
  public double spatialMagnitude() {
    return Math.sqrt(
      sq(x()) + 
      sq(y()) + 
      sq(z())
    ); 
  }
  
  /** The time component. */
  public double ct() { return on(CT); }
  
  /** The spatial component along the X-axis. */
  public double x() { return on(X); }
  
  /** The spatial component along the Y-axis. */
  public double y() { return on(Y); }
  
  /** The spatial component along the Z-axis. */
  public double z() { return on(Z); }
  
  /** This method allows for rounding errors (which are often significant), and is usually recommended instead of {@link #equals(Object)}. */
  public final boolean equalsWithTinyDiff(Event that) {
    if (this == that) return true;
    for(Axis axis : components.keySet()) {
      if (!equalsWithEpsilon(on(axis), that.on(axis))){
        return false; 
      }
    }
    return true;
  }
  
  /** Return a new object with the same data as this object. */
  public Event copy() {
    Event result = origin();
    for(Axis axis : Axis.values()) {
      result.put(axis, this.on(axis));
    }
    return result;
  }

  /** Return the position of the event. */
  public Position position(){
    return Position.of(x(), y(), z());
  }

  /** This implementation applies rounding. */
  @Override public String toString() {
    String sep = ", ";
    return "[" + 
      roundIt(ct()) + sep + 
      roundIt(x())  + sep + 
      roundIt(y())  + sep + 
      roundIt(z())  +  
    "]";
  }
  
  public String toStringNoRounding() {
    String sep = ", ";
    return "[" + 
      ct() + sep + 
      x()  + sep + 
      y()  + sep + 
      z()  +
    "]";
  }

  // PRIVATE

  private Map<Axis, Double> components = new LinkedHashMap<>();

  private Event(Double ct, Double x, Double y, Double z) {
    this.components.put(CT, ct);
    this.components.put(X, x);
    this.components.put(Y, y);
    this.components.put(Z, z);
  }
  
  /** 
   The scalar product of this event with another event (an invariant quantity).
   Returns positive and negative values. 
  */
  private double dot(Event that) {
    return 
      + on(CT)* that.on(CT) 
      - on(X) * that.on(X) 
      - on(Y) * that.on(Y) 
      - on(Z) * that.on(Z)
    ; 
  }
  
  private double roundIt(Double val) {
    return round(val, 5);
  }
}
