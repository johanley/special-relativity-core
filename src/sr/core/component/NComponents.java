package sr.core.component;

import static sr.core.Axis.CT;
import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;
import static sr.core.Util.round;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import sr.core.Axis;

/** 
 Three or four components corresponding the <em>ct</em>, <em>x</em>, <em>y</em>, and <em>z</em> axes.
 
 <P>For 3-vectors, any <em>ct</em>-component will be silently ignored, if it's present. 
*/
public final class NComponents {

  /** Space and time components (1+3). */
  public static NComponents of(double ct, double x, double y, double z) {
    NComponents result = new NComponents();
    result.comps.put(CT, ct);
    result.comps.put(X, x);
    result.comps.put(Y, y);
    result.comps.put(Z, z);
    return result;
  }
  
  /** Space components only (3). */
  public static NComponents of(double x, double y, double z) {
    NComponents result = new NComponents();
    result.comps.put(X, x);
    result.comps.put(Y, y);
    result.comps.put(Z, z);
    return result;
  }
  
  /** Read the component value on the given axis. */
  public double on(Axis axis) {
    return comps.get(axis);
  }
  
  /** The time component (not present for items having no time-component). */
  public double ct() {
    if (hasSpaceOnly()) {
      throw new IllegalStateException("No time component is present.");
    }
    return on(CT);  
  }
  /** The x-component. */
  public double x() { return on(X);  }
  /** The y-component. */
  public double y() { return on(Y);  }
  /** The z-component. */
  public double z() { return on(Z);  }
  
  /** 
   Return a new object with an overwrite of the value on the given axis.
   This method does not change the state of the current object.
   @param overwriteAxis must be already present in this object. 
  */
  public NComponents overwrite(Axis overwriteAxis, double value) {
    NComponents result = new NComponents();
    if(axes().contains(overwriteAxis)) {
      for(Axis existingAxis : this.axes()) {
        result.comps.put(existingAxis, this.comps.get(existingAxis));
      }
      result.comps.put(overwriteAxis, value);
    }
    else {
      throw new IllegalStateException("Trying to update component " + overwriteAxis + ", but that component is not present.");
    }
    return result;
  }

  /** Return the axes currently in use. */
  public Set<Axis> axes(){
    return Collections.unmodifiableSet(comps.keySet());
  };
  
  /** The number of components currently in use. */
  public int size() { 
    return comps.size();
  }
  
  public boolean hasSpaceOnly() {
    return size() == 3;
  }
  
  public boolean hasSpaceAndTime() {
    return size() == 4;
  }
  
  /** This implementation applies rounding. */
  @Override public String toString() {
    String sep = ",";
    String result = "[";
    for(Axis axis : comps.keySet()) {
      result = result + roundIt(on(axis)) + sep + " ";
    }
    //chop off the final separator+space characters 
    result = result.substring(0, result.length() - 2);
    return result + "]";
  }

  private Map<Axis, Double> comps = new LinkedHashMap<>();
  
  private double roundIt(Double val) {
    return round(val, 5);
  }
}
