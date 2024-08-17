package sr.core.vector3.transform;

import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;
import static sr.core.Parity.*;

import java.util.LinkedHashMap;
import java.util.Map;

import sr.core.Axis;
import sr.core.Parity;
import sr.core.Util;
import sr.core.vector3.ThreeVector;

/**
 Change the sign of one or more components.
 This transform has the curious property of not having a distinction 
 between an operation and its inverse.
  
 <p>WARNING: pseudo-vectors don't react to reflection operations.
 The {@link AxisAngle} is an example of a pseudo-vector. 
 Most pseudo-vectors are related to the cross-product of two polar vectors. 
*/
public final class SpatialReflection implements SpatialTransform {

  /** 
   Change the sign of more than one component.
   Changing the sign of exactly two components is equivalent to a rotation.  
   If a component is unaffected, then just pass {@link Parity#EVEN} for it. 
  */
  public static SpatialReflection of(Parity x, Parity y, Parity z) {
    return new SpatialReflection(x, y, z); 
  }
  
  /** Change the sign of a single component, along the given axis. */
  public static SpatialReflection of(Axis axis) {
    Util.mustBeSpatial(axis);
    SpatialReflection result = new SpatialReflection(EVEN, EVEN, EVEN);
    result.components.put(axis, ODD);
    return result;
  }
  
  public static SpatialReflection allAxes() {
    return new SpatialReflection(ODD, ODD, ODD); 
  }
  
  @Override public ThreeVector changeFrame(ThreeVector v) {
    return transform(v);
  }
  
  @Override public ThreeVector changeVector(ThreeVector v) {
    return transform(v);
  }
  
  @Override public String toString() {
    String result  = "spatial-reflect["; 
    for(Axis a : Axis.spatialAxes()) {
      result = result + components.get(a) + ",";
    }
    return result.substring(0, result.length()-1) + "]";
  }
  
  private Map<Axis, Parity> components = new LinkedHashMap<>();
  
  private SpatialReflection(Parity x, Parity y, Parity z) {
    components.put(X, x);
    components.put(Y, y);
    components.put(Z, z);
  }
  
  private ThreeVector transform(ThreeVector v/*, int sign is not needed or desired here! */) {
    ThreeVector result = v.copy();
    for(Axis axis : Axis.spatialAxes()) {
      result = result.put(axis, components.get(axis).sign() * v.on(axis));
    }
    return result;
  }
}
