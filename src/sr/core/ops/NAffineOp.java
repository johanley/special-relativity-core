package sr.core.ops;

import sr.core.component.ops.NSense;
import sr.core.vec4.NFourDelta;

/** 
  Affine operations.
  These operations apply to events and positions, but not to vectors.
*/
public interface NAffineOp<T> {
  
  /**
   Displacement in space-time.
   <P>This operation applies to events and positions, but not to vectors.
   
   @param displacement is a displacement in space, time, or space-time. 
   @param sense the direction of the transformation
   @return a new object 
  */
  public T moveZeroPointBy(NFourDelta displacement, NSense sense);

}
