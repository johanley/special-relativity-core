package sr.core.vec3;

import sr.core.component.NComponents;
import sr.core.component.NPosition;
import sr.core.component.ops.NSense;
import sr.core.ops.NLinearOps;

/** Difference between any two {@link NPosition}s, <em>&Delta;x<sup>i</sup></em>. */
public final class NDelta extends NThreeVector implements NLinearOps<NDelta> {
  
  /** Factory method. The difference is in the sense of <em>b - a</em>, which "goes" from <em>a</em> to <em>b</em>. */
  public static NDelta of(NPosition a, NPosition b) {
    return new NDelta(a, b);
  }
  
  /** Factory method. The difference is in the sense of <em>b - origin</em>. */
  public static NDelta withRespectToOrigin(NPosition b) {
    return new NDelta(NPosition.origin(), b);
  }

  /** No effect. */
  @Override public NDelta reverseClocks() {
    return new NDelta(a.reverseClocks(), b.reverseClocks());
  }
  
  /** Reverse the sign of all components. */
  @Override public NDelta reverseSpatialAxes() {
    return new NDelta(a.reverseSpatialAxes(), b.reverseSpatialAxes());
  }
  
  @Override public NDelta rotate(NAxisAngle axisAngle, NSense sense) {
    return new NDelta(a.rotate(axisAngle, sense), b.rotate(axisAngle, sense));
  }
  
  private NDelta(NPosition a, NPosition b) {
    super(compsFromBminusA(a, b));
    this.a = a;
    this.b = b;
  }
  
  private NPosition a;
  private NPosition b;
  
  private static NComponents compsFromBminusA(NPosition a, NPosition b) {
    return NComponents.of(
      b.x() - a.x(), 
      b.y() - a.y(), 
      b.z() - a.z()
    );
  }
}