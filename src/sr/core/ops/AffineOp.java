package sr.core.ops;

import sr.core.component.ops.Sense;
import sr.core.vec4.FourDelta;

/** 
  Affine operations.
  These operations apply to events and positions, but not to vectors.
*/
public interface AffineOp<T> {
  
  /**
   Displacement in space-time.
   <P>This operation applies to events and positions, but not to vectors.
   
   @param displacement is a displacement in space, time, or space-time. 
   @param sense the direction of the transformation
   @return a new object 
  */
  public T moveZeroPointBy(FourDelta displacement, Sense sense);

}
