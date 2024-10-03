package sr.core.component.ops;

import sr.core.Axis;
import sr.core.component.Components;
import sr.core.vec4.NFourDelta;

/** 
 Displacement of the origin of coordinates, in both time and space.
 This operation has no effect on vectors; it only affects positions and events.
*/
public final class MoveZeroPointBy implements ComponentOp {
  
  /**  Factory method. */
  public static MoveZeroPointBy of(NFourDelta displacement, Sense sense) {
    return new MoveZeroPointBy(displacement, sense);
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
  
  private NFourDelta displacement;
  private Sense sense;
  
  private MoveZeroPointBy(NFourDelta displacement, Sense sense) {
    this.displacement = displacement;
    this.sense = sense;
  }
}
