package sr.core.event;

import static sr.core.Axis.CT;
import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;
import static sr.core.Util.equalsWithEpsilon;
import static sr.core.Util.round;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import sr.core.Axis;
import sr.core.vector.ThreeVector;
import sr.core.vector.ThreeVectorImpl;

/** 
 The basic data and operations for a 4-vector.
 
 <P>Note on the design: this class doesn't do two important jobs:
  <ul> 
   <li>create new FourVector objects
   <li>mutate the state of existing FourVector objects
  </ul> 
*/
public class FourVector /* not possible: implements Builder<FourVector> */ {
  
  /*
   * Implementation note: the difficulty here is with trying to preserve object immutability.
   * It's trivial to change the state of an object; it takes more work to create new objects from scratch. 
   */
  
  /** A read-only view of the components of this four-vector. */
  public final Map<Axis, Double> components(){
    return Collections.unmodifiableMap(components);
  }

  /** A read-only view of the spatial components of this four-vector. */
  public final ThreeVector spatialComponents() {
    return ThreeVectorImpl.of(x(), y(), z());
  }

  /** Magnitude of the {@link #spatialComponents()}. Always non-negative. */
  public final double spatialMagnitude() {
    return spatialComponents().magnitude();
  }
  
  /** Return a single component on the given axis. */
  public final double on(Axis axis) {
    return components().get(axis);
  }

  /** Return the time-component. */
  public final double ct() { return on(CT); }
  /** Return the x-component. */
  public final double x() { return on(X); }
  /** Return the y-component. */
  public final double y() { return on(Y); }
  /** Return the z-component. */
  public final double z() { return on(Z); }
  
  /** 
   The dot product of this 4-vector with itself.
   The fundamental quadratic form. 
   Returns any sign, or 0. Signature (+---) for (ct,x,y,z). 
  */ 
  public final double square() {
    return this.dot(this);
  }
  
  /** 
   The scalar product of <em>this</em> object with <em>that</em> object (an invariant quantity).
   Returns positive and negative values. 
   Signature (+---) for (ct,x,y,z).
  */
  public final double dot(FourVector that) {
    return 
     + this.components.get(CT) * that.components.get(CT) 
     - this.components.get(X) * that.components.get(X) 
     - this.components.get(Y) * that.components.get(Y) 
     - this.components.get(Z) * that.components.get(Z) 
   ; 
  }
  
  /** This method allows for small rounding errors (which are often significant). */
  public final boolean equalsWithTinyDiff(FourVector that) {
    if (this == that) return true;
    for(Axis axis : Axis.values()) {
      if (!equalsWithEpsilon(on(axis), that.on(axis))){
        return false; 
      }
    }
    return true;
  }

  /** This implementation applies rounding. */
  @Override public String toString() {
    String sep = ",";
    String result = "[";
    for(Axis axis : Axis.values()) {
      result = result + roundIt(on(axis)) + sep;
    }
    //chop off the final separator character
    result = result.substring(0, result.length() - 1);
    return result + "]";
  }
  
  /** 
   The components of an {@link Event} or four-vector.
   Subclasses access this object to read-write the data. 
  */
  protected Map<Axis, Double> components = new LinkedHashMap<>(); 

  private double roundIt(Double val) {
    return round(val, 5);
  }
  
}
