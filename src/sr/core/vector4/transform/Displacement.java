package sr.core.vector4.transform;

import static sr.core.Axis.CT;
import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;

import java.util.LinkedHashMap;
import java.util.Map;

import sr.core.Axis;
import sr.core.vector4.Event;

/**
 Add/subtract a fixed amount to 1 or more components.
 
 <P>Note that this transform is the only one that uses numbers that have dimensions.
 <P>Also note that 4-vectors in general are differential and not affected by a displacement operation.
 (This reflects the distinction between affine operations and linear operations.)
*/
public final class Displacement implements Transform {
  
  /** 
   Factory method. 
   Pass the displacement used to alter each coordinate.
   Simply pass 0 if the coord is left unaffected.
  */
  public static Displacement of(double ct, double x, double y, double z) {
    return new Displacement(ct, x, y, z);
  }
  
  /** Displace parallel to a coordinate axis. */
  public static Displacement along(Axis axis, double value) {
    Displacement result = new Displacement(0, 0, 0, 0);
    result.components.put(axis, value);
    return result;
  }
  
  /** The origin of the frame of reference is displaced by the given amounts. */
  @Override public Event changeFrame(Event event) {
    return transform(event, -1);
  }
  
  /** The endpoint of the event is displaced by the given amounts. */
  @Override public Event changeEvent(Event event) {
    return transform(event, +1);
  }
  
  @Override public String toString() {
    String result  = "displacement["; 
    for(Axis a : Axis.values()) {
      result = result + components.get(a) + ",";
    }
    return result.substring(0, result.length()-1) + "]";
  }

  // PRIVATE
  
  private Map<Axis, Double> components = new LinkedHashMap<>();
  
  private Displacement(double ct, double x, double y, double z) {
    components.put(CT, ct);
    components.put(X, x);
    components.put(Y, y);
    components.put(Z, z);
  }
  
  private Event transform(Event event, int sign) {
    return Event.of(
      event.ct() + components.get(CT) * sign,  
      event.x() + components.get(X) * sign, 
      event.y() + components.get(Y) * sign, 
      event.z() + components.get(Z) * sign
    );
  }

}