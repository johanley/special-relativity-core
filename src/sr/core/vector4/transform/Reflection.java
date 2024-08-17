package sr.core.vector4.transform;

import static sr.core.Axis.CT;
import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;
import static sr.core.Parity.EVEN;
import static sr.core.Parity.ODD;

import java.util.LinkedHashMap;
import java.util.Map;

import sr.core.Axis;
import sr.core.Parity;
import sr.core.vector4.Event;

/**
 Change the sign of one or more components.
 This transform has the curious property of not having a distinction 
 between an operation and its inverse. 
*/
public final class Reflection implements Transform {
  
  /**
   Change the sign of more than one component.
   Changing the sign of exactly two components is equivalent to a rotation.  
   If a component is unaffected, then just pass {@link Parity#EVEN} for it. 
  */
  public static Reflection of(Parity ct, Parity x, Parity y, Parity z) {
    return new Reflection(ct, x, y, z);
  }

  /** Change the sign of a single component, along the given axis. */
  public static Reflection of(Axis axis) {
    Reflection result = new Reflection(EVEN, EVEN, EVEN, EVEN);
    result.components.put(axis, ODD);
    return result;
  }
  
  public static Reflection allAxes() {
    return new Reflection(ODD, ODD, ODD, ODD); 
  }
  
  @Override public Event changeFrame(Event event) {
    return doIt(event);
  }
  
  @Override public Event changeEvent(Event event) {
    return doIt(event);
  }
  
  @Override public String toString() {
    String result  = "reflect["; 
    for(Axis a : Axis.values()) {
      result = result + components.get(a) + ",";
    }
    return result.substring(0, result.length()-1) + "]";
  }
  
  // PRIVATE
  
  private Map<Axis, Parity> components = new LinkedHashMap<>();
  
  private Reflection(Parity ct, Parity x, Parity y, Parity z) {
    components.put(CT, ct);
    components.put(X, x);
    components.put(Y, y);
    components.put(Z, z);
  }

  private Event doIt(Event event/*, int sign is not needed or desired here */) {
    Map<Axis, Double> parts = new LinkedHashMap<>();
    for(Axis axis : Axis.values()) {
      parts.put(axis, components.get(axis).sign() * event.on(axis));
    }
    return event.build(parts);
  }
}