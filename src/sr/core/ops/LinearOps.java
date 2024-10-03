package sr.core.ops;

import sr.core.component.ops.Sense;
import sr.core.vec3.NAxisAngle;

/** Spatial rotation, parity (P), and clock-reversal (T). */
public interface LinearOps<T> {
  
  /**
   Spatial rotation about a given axis.
   
   @param axisAngle
   @param sense the direction of the transformation
   @return a new object
  */
  public T rotate(NAxisAngle axisAngle, Sense sense);
  
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
