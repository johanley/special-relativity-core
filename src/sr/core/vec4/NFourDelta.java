package sr.core.vec4;

import sr.core.component.NComponents;
import sr.core.component.NEvent;
import sr.core.component.ops.NSense;
import sr.core.ops.NLinearBoostOp;
import sr.core.ops.NLinearOps;
import sr.core.vec3.NAxisAngle;
import sr.core.vec3.NVelocity;

/** Difference between any two {@link NEvent}s, <em>&Delta;x<sup>i</sup></em>. */
public final class NFourDelta extends NFourVector implements NLinearOps<NFourDelta>, NLinearBoostOp<NFourDelta> {
  
  /** Factory method. The difference is in the sense of <em>b - a</em>, which "goes" from <em>a</em> to <em>b</em>. */
  public static NFourDelta of(NEvent a, NEvent b) {
    return new NFourDelta(a, b);
  }
  
  /** Factory method. The difference is in the sense of <em>b - origin</em>. */
  public static NFourDelta withRespectToOrigin(NEvent b) {
    return new NFourDelta(NEvent.origin(), b);
  }

  @Override public NFourDelta reverseClocks() {
    return new NFourDelta(a.reverseClocks(), b.reverseClocks());
  }
  
  @Override public NFourDelta reverseSpatialAxes() {
    return new NFourDelta(a.reverseSpatialAxes(), b.reverseSpatialAxes());
  }
  
  @Override public NFourDelta rotate(NAxisAngle axisAngle, NSense sense) {
    return new NFourDelta(a.rotate(axisAngle, sense), b.rotate(axisAngle, sense));
  }

  @Override public NFourDelta boost(NVelocity v, NSense sense) {
    return new NFourDelta(a.boost(v, sense), b.boost(v, sense));
  }

  private NEvent a;
  private NEvent b;
  
  /**
   Constructor.
   The difference is in the sense of <em>b - a</em>. 
  */
  private NFourDelta(NEvent a, NEvent b) {
    this.a = a;
    this.b = b;
    this.components = compsFromBminusA(a, b);
  }
 
  private static NComponents compsFromBminusA(NEvent a, NEvent b) {
    return NComponents.of(
      b.ct() - a.ct(), 
      b.x() - a.x(), 
      b.y() - a.y(), 
      b.z() - a.z()
    );
  }
}
