package sr.core.vector4.transform;

import java.util.LinkedHashMap;
import java.util.Map;

import sr.core.Axis;
import sr.core.vector3.AxisAngle;
import sr.core.vector3.ThreeVector;
import sr.core.vector3.transform.SpatialRotation;
import sr.core.vector4.Builder;
import sr.core.vector4.FourVector;

/** 
 Rotate around any axis, using an axis-angle pseudo-vector.
 See {@link SpatialRotation}.
*/
public final class Rotation implements Transform {
  
  /** Construct with a factory method. */
  public static Rotation of(AxisAngle axisAngle) {
    return new Rotation(axisAngle);
  }
  
  public static Rotation of(Axis axis, double angle) {
    return new Rotation(axis, angle);
  }
  
  @Override public <T extends FourVector & Builder<T>> T changeFrame(T fourVector) {
    return transform(fourVector, -1);
  }
  
  @Override public <T extends FourVector & Builder<T>> T changeVector(T fourVector) {
    return transform(fourVector, +1);
  }
  
  @Override public String toString() {
    return spatialRotation.toString();
  }
  
  // PRIVATE

  private SpatialRotation spatialRotation;

  private Rotation(AxisAngle axisAngle) {
    this.spatialRotation = SpatialRotation.of(axisAngle);
  }
  
  private Rotation(Axis axis, double angle) {
    this.spatialRotation = SpatialRotation.of(axis, angle);
  }
  
  private <T extends FourVector & Builder<T>> T transform(T fourVector, int sign) {
    ThreeVector new_position = null;
    if (sign > 0) {
      new_position = spatialRotation.changeVector(fourVector.spatialComponents());
    }
    else {
      new_position = spatialRotation.changeFrame(fourVector.spatialComponents());
    }
    
    Map<Axis, Double> parts = new LinkedHashMap<>();
    parts.put(Axis.CT, fourVector.ct());
    for(Axis axis : Axis.spatialAxes()) {
      parts.put(axis, new_position.on(axis));
    }
    return fourVector.build(parts);
  }
}
