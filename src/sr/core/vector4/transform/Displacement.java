package sr.core.vector4.transform;

import static sr.core.Axis.CT;
import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;
import static sr.core.Util.round;

import java.util.LinkedHashMap;
import java.util.Map;

import sr.core.Axis;
import sr.core.vector4.Builder;
import sr.core.vector4.Event;
import sr.core.vector4.FourVector;

/**
 Add/subtract a fixed amount to 1 or more components.

 <P><b>This operation is a no-operation for all four vectors except {@link Event}s.</b>
 
 <P>4-vectors in general are differential and not affected by a displacement operation.
 This reflects the distinction between affine operations and linear operations.

 <P>Note as well that all transforms use dimensionless numbers except for this one.
*/
public final class Displacement implements Transform {
  
  /** 
   Factory method. 
   Pass the displacement used to alter each coordinate.
   Simply pass 0 if the coordinate is left unaffected.
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
  
  /** The origin of the frame is displaced by the given amounts. */
  @Override public <T extends FourVector & Builder<T>> T changeGrid(T fourVector) {
    return transform(fourVector, -1);
  }
  
  /** The endpoint of the event is displaced by the given amounts. */
  @Override public <T extends FourVector & Builder<T>> T changeVector(T fourVector) {
    return transform(fourVector, +1);
  }

  /** This implementation applies rounding. */
  @Override public String toString() {
    String sep = ",";
    String result = "displacement[";
    for(Axis axis : Axis.values()) {
      result = result + roundIt(components.get(axis)) + sep;
    }
    //chop off the final separator
    result = result.substring(0, result.length() - 1);
    return result + "]";
  }

  // PRIVATE
  
  private Map<Axis, Double> components = new LinkedHashMap<>();
  
  private Displacement(double ct, double x, double y, double z) {
    components.put(CT, ct);
    components.put(X, x);
    components.put(Y, y);
    components.put(Z, z);
  }
  
  private <T extends FourVector & Builder<T>> T transform(T fourVector, int sign) {
    T result = fourVector;
    //only applies to Events!
    if (fourVector instanceof Event) {
      Map<Axis, Double> parts = new LinkedHashMap<>();
      for(Axis axis : Axis.values()) {
        parts.put(axis, fourVector.on(axis) + components.get(axis) * sign);
      }
      result = fourVector.build(parts);
    }
    return result;
  }
  
  private double roundIt(Double val) {
    return round(val, 5);
  }
}