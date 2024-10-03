package sr.core.vec3;

import sr.core.component.Components;
import sr.core.component.Position;
import sr.core.component.ops.Sense;
import sr.core.ops.LinearOps;

/** Difference between any two {@link Position}s, <em>&Delta;x<sup>i</sup></em>. */
public final class NDelta extends NThreeVector implements LinearOps<NDelta> {
  
  /** Factory method. The difference is in the sense of <em>b - a</em>, which "goes" from <em>a</em> to <em>b</em>. */
  public static NDelta of(Position a, Position b) {
    return new NDelta(a, b);
  }
  
  /** Factory method. The difference is in the sense of <em>b - origin</em>. */
  public static NDelta withRespectToOrigin(Position b) {
    return new NDelta(Position.origin(), b);
  }

  /** No effect. */
  @Override public NDelta reverseClocks() {
    return new NDelta(a.reverseClocks(), b.reverseClocks());
  }
  
  /** Reverse the sign of all components. */
  @Override public NDelta reverseSpatialAxes() {
    return new NDelta(a.reverseSpatialAxes(), b.reverseSpatialAxes());
  }
  
  @Override public NDelta rotate(NAxisAngle axisAngle, Sense sense) {
    return new NDelta(a.rotate(axisAngle, sense), b.rotate(axisAngle, sense));
  }
  
  private NDelta(Position a, Position b) {
    super(compsFromBminusA(a, b));
    this.a = a;
    this.b = b;
  }
  
  private Position a;
  private Position b;
  
  private static Components compsFromBminusA(Position a, Position b) {
    return Components.of(
      b.x() - a.x(), 
      b.y() - a.y(), 
      b.z() - a.z()
    );
  }
}