package sr.core.ops;

import sr.core.component.ops.NSense;
import sr.core.vec3.NVelocity;

public interface NLinearBoostOp<T> {
  
  public T boost(NVelocity v, NSense sense);
  

}
