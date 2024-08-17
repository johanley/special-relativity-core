package sr.core.vector4.transform;

import sr.core.Axis;
import sr.core.vector3.AxisAngle;
import sr.core.vector3.ThreeVector;
import sr.core.vector3.transform.SpatialRotation;
import sr.core.vector4.Event;

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
  
  @Override public Event changeFrame(Event event) {
    return transform(event, -1);
  }
  
  @Override public Event changeEvent(Event event) {
    return transform(event, +1);
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
  
  private Event transform(Event event, int sign) {
    ThreeVector v = null;
    if (sign > 0) {
      v = spatialRotation.changeVector(event.position());
    }
    else {
      v = spatialRotation.changeFrame(event.position());
    }
    return Event.of(event.ct(), v.x(), v.y(), v.z());
  }
  
}
