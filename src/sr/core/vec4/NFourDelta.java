package sr.core.vec4;

import sr.core.component.Components;
import sr.core.component.Event;
import sr.core.component.ops.Sense;
import sr.core.ops.LinearBoostOp;
import sr.core.ops.LinearOps;
import sr.core.vec3.NAxisAngle;
import sr.core.vec3.NVelocity;

/** Difference between any two {@link Event}s, <em>&Delta;x<sup>i</sup></em>. */
public final class NFourDelta extends NFourVector implements LinearOps<NFourDelta>, LinearBoostOp<NFourDelta> {
  
  /** Factory method. The difference is in the sense of <em>b - a</em>, which "goes" from <em>a</em> to <em>b</em>. */
  public static NFourDelta of(Event a, Event b) {
    return new NFourDelta(a, b);
  }
  
  /** Factory method. The difference is in the sense of <em>b - origin</em>. */
  public static NFourDelta withRespectToOrigin(Event b) {
    return new NFourDelta(Event.origin(), b);
  }

  @Override public NFourDelta reverseClocks() {
    return new NFourDelta(a.reverseClocks(), b.reverseClocks());
  }
  
  @Override public NFourDelta reverseSpatialAxes() {
    return new NFourDelta(a.reverseSpatialAxes(), b.reverseSpatialAxes());
  }
  
  @Override public NFourDelta rotate(NAxisAngle axisAngle, Sense sense) {
    return new NFourDelta(a.rotate(axisAngle, sense), b.rotate(axisAngle, sense));
  }

  @Override public NFourDelta boost(NVelocity v, Sense sense) {
    return new NFourDelta(a.boost(v, sense), b.boost(v, sense));
  }

  private Event a;
  private Event b;
  
  /**
   Constructor.
   The difference is in the sense of <em>b - a</em>. 
  */
  private NFourDelta(Event a, Event b) {
    this.a = a;
    this.b = b;
    this.components = compsFromBminusA(a, b);
  }
 
  private static Components compsFromBminusA(Event a, Event b) {
    return Components.of(
      b.ct() - a.ct(), 
      b.x() - a.x(), 
      b.y() - a.y(), 
      b.z() - a.z()
    );
  }
}
