package sr.core.component.ops;

import sr.core.Axis;
import sr.core.component.NComponents;
import sr.core.vec4.NFourDelta;

/** 
 Displacement of the origin of coordinates, in both time and space.
 This operation has no effect on vectors; it only affects positions and events.
*/
public final class NMoveZeroPointBy implements NComponentOp {
  
  /**  Factory method. */
  public static NMoveZeroPointBy of(NFourDelta displacement, NSense sense) {
    return new NMoveZeroPointBy(displacement, sense);
  }

  /**
   Displacement of the origin of coordinates, in both time and space.
   If a 3-vector is the source to be acted upon by this operation, then any displacement in time is silently ignored.
  */
  @Override public NComponents applyTo(NComponents source) {
    NComponents result = source;
    for(Axis axis : source.axes()) {
      double currentValue = source.on(axis);
      double moveBy = sense.sign() * displacement.on(axis);
      result = result.overwrite(axis, currentValue + moveBy);
    }
    return result;
  }
  
  private NFourDelta displacement;
  private NSense sense;
  
  private NMoveZeroPointBy(NFourDelta displacement, NSense sense) {
    this.displacement = displacement;
    this.sense = sense;
  }
}
