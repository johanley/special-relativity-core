package sr.core.ops;

import sr.core.component.ops.Sense;
import sr.core.vec3.AxisAngle;

/** Spatial rotation, parity (P), and clock-reversal (T). */
public interface LinearOps<T> {
  
  /**
   Spatial rotation about a given axis.
   
   @param axisAngle the direction and magnitude
   @param sense the direction of the transformation
   @return a new object
  */
  public T rotate(AxisAngle axisAngle, Sense sense);
  
  /**
   Negate the time component.
   @return a new object
  */
  public T reverseClocks();
  
  
  /**
   Negate all the spatial components.
   @return a new object
  */
  public T reverseSpatialAxes();

}
