package sr.core.ops;

import sr.core.component.ops.Sense;
import sr.core.vec3.Velocity;

/** A Lorentz boost in any direction. */
public interface LinearBoostOp<T> {
  
  /**
   A Lorentz boost in any direction. No rotation is applied.
   
   @param boost_v the boost velocity
   @param sense the direction of the transformation
   @return a new object
  */
  public T boost(Velocity boost_v, Sense sense);
  

}
