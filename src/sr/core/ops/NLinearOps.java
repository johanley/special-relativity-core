package sr.core.ops;

import sr.core.component.ops.NSense;
import sr.core.vec3.NAxisAngle;

public interface NLinearOps<T> {
  
  public T rotate(NAxisAngle axisAngle, NSense sense);
  public T reverseClocks();
  public T reverseSpatialAxes();

}
