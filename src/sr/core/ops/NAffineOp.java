package sr.core.ops;

import sr.core.component.ops.NSense;
import sr.core.vec4.NFourDelta;

/** Displacement in space-time. */
public interface NAffineOp<T> {
  
  /**
   @param sense +1 applies the displacement to the components, -1 to the frame. 
  */
  public T moveZeroPointBy(NFourDelta displacement, NSense sense);

}
