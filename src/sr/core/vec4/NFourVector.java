package sr.core.vec4;

import static sr.core.Axis.CT;
import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;
import static sr.core.Util.round;

import sr.core.Axis;
import sr.core.component.NComponents;
import sr.core.vec3.NThreeVector;

/** 
The basic data and operations for a 4-vector.

<P>Note on the design: this class doesn't do two important jobs:
 <ul> 
  <li>create new FourVector objects (since different vector have different construction needs)
  <li>mutate the state of existing FourVector objects
 </ul> 
*/
public class NFourVector {

  public double on(Axis axis) { return components.on(axis); }
  public double ct() { return on(CT); }
  public double x() { return on(X); }
  public double y() { return on(Y); }
  public double z() { return on(Z); }
  
  /** 
   The scalar product of <em>this</em> object with <em>that</em> object (a Poincaré invariant quantity).
   Returns positive and negative values. 
   Signature (+---) for (ct,x,y,z).
  */
  public final double dot(NFourVector that) {
    return 
     + this.components.ct() * that.components.ct() 
     - this.components.x() * that.components.x() 
     - this.components.y() * that.components.y() 
     - this.components.z() * that.components.z() 
   ; 
  }
  
  /** 
   The dot product of this 4-vector with itself.
   The fundamental quadratic form. 
   Returns any sign, or 0. Signature (+---) for (ct,x,y,z). 
  */ 
  public final double square() {
    return this.dot(this);
  }

  /** Magnitude of the spatial components of this 4-vector. Always non-negative. */
  public final double spatialMagnitude() {
    NThreeVector v = NThreeVector.of(x(), y(), z());
    return v.magnitude();
  }

  /** The spatial components as a basic 3-vector. */
  public final NThreeVector spatialComponents() {
    return NThreeVector.of(x(), y(), z());
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
  
  protected NComponents components;

  private double roundIt(Double val) {
    return round(val, 5);
  }
}
