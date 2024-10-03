package sr.core.vec4;

import sr.core.component.Components;
import sr.core.component.Event;
import sr.core.component.ops.Sense;
import sr.core.ops.LinearBoostOp;
import sr.core.ops.LinearOps;
import sr.core.vec3.AxisAngle;
import sr.core.vec3.Velocity;

/** Difference between any two {@link Event}s, <em>&Delta;x<sup>i</sup></em>. */
public final class FourDelta extends FourVector implements LinearOps<FourDelta>, LinearBoostOp<FourDelta> {
  
  /** Factory method. The difference is in the sense of <em>b - a</em>, which "goes" from <em>a</em> to <em>b</em>. */
  public static FourDelta of(Event a, Event b) {
    return new FourDelta(a, b);
  }
  
  /** Factory method. The difference is in the sense of <em>b - origin</em>. */
  public static FourDelta withRespectToOrigin(Event b) {
    return new FourDelta(Event.origin(), b);
  }

  @Override public FourDelta reverseClocks() {
    return new FourDelta(a.reverseClocks(), b.reverseClocks());
  }
  
  @Override public FourDelta reverseSpatialAxes() {
    return new FourDelta(a.reverseSpatialAxes(), b.reverseSpatialAxes());
  }
  
  @Override public FourDelta rotate(AxisAngle axisAngle, Sense sense) {
    return new FourDelta(a.rotate(axisAngle, sense), b.rotate(axisAngle, sense));
  }

  @Override public FourDelta boost(Velocity v, Sense sense) {
    return new FourDelta(a.boost(v, sense), b.boost(v, sense));
  }

  private Event a;
  private Event b;
  
  /**
   Constructor.
   The difference is in the sense of <em>b - a</em>. 
  */
  private FourDelta(Event a, Event b) {
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
