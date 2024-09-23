package sr.core.vector4;

import static sr.core.Axis.CT;
import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;
import static sr.core.Util.mustHave;

import java.util.LinkedHashMap;
import java.util.Map;

import sr.core.Axis;
import sr.core.vector3.Position;

/** 
 An event in Minkowski space-time.
 
 <P><b>Objects of this class only behave as 4-vectors when they result from a difference between two events.</b>
 
 <P>The real 4-vectors are <em>differences</em> <em>&Delta;x<sup>i</sup></em> between events in space-time.
 The space-time events <em>x<sup>i</sup></em> themselves aren't true 4-vectors.
 Remember that the fundamental quadratic form is defined in terms of <em>differences</em> between event coordinates.
 
 <P>Take as an example the dot product with the phase-gradient (wave vector) <em>k<sup>i</sup></em>:
  <ul>
    <li><em>k<sup>i</sup>&Delta;x<sub>i</sub></em> is Poincaré invariant.
    <li><em>k<sup>i</sup>x<sub>i</sub></em> is <em>not</em> Poincaré invariant: it changes value when the origin of coordinates is displaced.
  </ul>
*/
public final class Event extends FourVector implements Builder<Event> {

  /** Factory method. */
  public static final Event of(Map<Axis, Double> components) {
    mustHave(components.size() == Axis.values().length, "Unexpected size for map: " + components.size());
    return new Event(components);
  }

  /**  Factory method. */
  public static final Event of(double ct, double x, double y, double z) {
    return new Event(ct, x, y, z);
  }
  
  /**  Factory method. */
  public static final Event of(Double ct, Position position) {
    return new Event(ct, position.on(X), position.on(Y), position.on(Z));
  }
  
  @Override public Event build(Map<Axis, Double> components) {
    return new Event(components);
  }
  
  /** Replace one component of this event, and build a new object in doing so. */
  public Event put(Axis axis, Double value) {
    Map<Axis, Double> updated = new LinkedHashMap<>();
    updated.putAll(this.components);
    updated.put(axis, value);
    return Event.of(updated);
  }
  
  /** This event plus 'that' event (for each component). Returns a new object. */
  public final Event plus(Event that) {
    Map<Axis, Double> components = new LinkedHashMap<>();
    for(Axis axis : Axis.values()) {
      components.put(axis, this.on(axis) + that.on(axis));
    }
    return Event.of(components);
  }
  
  /** This event minus 'that' event (for each component). Returns a new object. */
  public final Event minus(Event that) {
    Map<Axis, Double> components = new LinkedHashMap<>();
    for(Axis axis : Axis.values()) {
      components.put(axis, this.on(axis) - that.on(axis));
    }
    return Event.of(components);
  }

  /** Return the position of the event. */
  public Position position(){
    return Position.of(x(), y(), z());
  }
  
  // PRIVATE

  /** All construction passes through here. */
  private Event(Map<Axis, Double> parts) {
    this.components = new LinkedHashMap<>(parts);
  }
  
  private Event(Double ct, Double x, Double y, Double z) {
    this(buildComponents(ct, x, y, z));
  }

  private static Map<Axis, Double> buildComponents(Double ct, Double x, Double y, Double z){
    Map<Axis, Double> components = new LinkedHashMap<>();
    components.put(CT, ct);
    components.put(X, x);
    components.put(Y, y);
    components.put(Z, z);
    return components;
  }
}
