package sr.core.ops;

import sr.core.component.ops.NSense;
import sr.core.vec3.NVelocity;

/** A Lorentz boost in any direction. */
public interface NLinearBoostOp<T> {
  
  /**
   A Lorentz boost in any direction. No rotation is applied.
   
   @param boost_v the boost velocity
   @param sense the direction of the transformation
   @return a new object
  */
  public T boost(NVelocity boost_v, NSense sense);
  

}
