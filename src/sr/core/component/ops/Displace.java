package sr.core.component.ops;

import sr.core.Axis;
import sr.core.component.Components;
import sr.core.vec4.FourDelta;

/** 
 Move the zero-point of the origin of coordinates, in both time and space.
 This operation has no effect on vectors; it only affects positions and events.
*/
public final class Displace implements ComponentOp {
  
  /**  Factory method. */
  public static Displace of(FourDelta displacement, Sense sense) {
    return new Displace(displacement, sense);
  }

  /**
   Displacement of the origin of coordinates, in both time and space.
   If a 3-vector is the source to be acted upon by this operation, then any displacement in time is silently ignored.
  */
  @Override public Components applyTo(Components source) {
    Components result = source;
    for(Axis axis : source.axes()) {
      double currentValue = source.on(axis);
      double moveBy = sense.sign() * displacement.on(axis);
      result = result.overwrite(axis, currentValue + moveBy);
    }
    return result;
  }
  
  private FourDelta displacement;
  private Sense sense;
  
  private Displace(FourDelta displacement, Sense sense) {
    this.displacement = displacement;
    this.sense = sense;
  }
}
