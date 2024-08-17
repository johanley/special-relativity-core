package sr.core.vector3.transform;

import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;

import java.util.LinkedHashMap;
import java.util.Map;

import sr.core.Axis;
import sr.core.vector3.ThreeVector;
import sr.core.vector3.ThreeVectorImpl;

/**
 Add/subtract a fixed amount to 1 or more components.
 
 <P>Note that this transform is the only one that uses numbers that have dimensions.
 <P>Also note that vectors in general are differential and are not affected by a displacement operation.
 (This reflects the distinction between affine operations and linear operations.)
*/
public final class SpatialDisplacement implements SpatialTransform {
  
  /** 
   Factory method.
   Pass the displacement used to alter each coordinate.
   Simply pass 0 if the spatial coordinate is left unchanged.
  */
  public static SpatialDisplacement of(double x, double y, double z) {
    return new SpatialDisplacement(x, y, z);
  }

  /** Displace parallel to a coordinate axis. */
  public static SpatialDisplacement along(Axis axis, double value) {
    SpatialDisplacement result = new SpatialDisplacement(0, 0, 0);
    result.components.put(axis, value);
    return result;
  }
  
  /** The origin of the frame of reference is displaced by the given amounts. */
  @Override public ThreeVector changeFrame(ThreeVector v) {
    return transform(v, -1);
  }
  
  /** The endpoint of the vector is displaced by the given amounts. */
  @Override public ThreeVector changeVector(ThreeVector v) {
    return transform(v, +1);
  }
  
  @Override public String toString() {
    String result  = "spatial-displacement["; 
    for(Axis a : Axis.spatialAxes()) {
      result = result + components.get(a) + ",";
    }
    return result.substring(0, result.length()-1) + "]";
  }

  // PRIVATE 

  private Map<Axis, Double> components = new LinkedHashMap<>();
 
  private SpatialDisplacement(double x, double y, double z) {
    components.put(X, x);
    components.put(Y, y);
    components.put(Z, z);
  }
  
  private ThreeVector transform(ThreeVector v, int sign) {
    return ThreeVectorImpl.of(
      v.x() + components.get(X) * sign, 
      v.y() + components.get(Y) * sign, 
      v.z() + components.get(Z) * sign
    );
  }
}