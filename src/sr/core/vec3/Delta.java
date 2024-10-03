package sr.core.vec3;

import sr.core.component.Components;
import sr.core.component.Position;
import sr.core.component.ops.Sense;
import sr.core.ops.LinearOps;

/** Difference between any two {@link Position}s, <em>&Delta;x<sup>i</sup></em>. */
public final class Delta extends ThreeVector implements LinearOps<Delta> {
  
  /** Factory method. The difference is in the sense of <em>b - a</em>, which "goes" from <em>a</em> to <em>b</em>. */
  public static Delta of(Position a, Position b) {
    return new Delta(a, b);
  }
  
  /** Factory method. The difference is in the sense of <em>b - origin</em>. */
  public static Delta withRespectToOrigin(Position b) {
    return new Delta(Position.origin(), b);
  }

  /** No effect. */
  @Override public Delta reverseClocks() {
    return new Delta(a.reverseClocks(), b.reverseClocks());
  }
  
  /** Reverse the sign of all components. */
  @Override public Delta reverseSpatialAxes() {
    return new Delta(a.reverseSpatialAxes(), b.reverseSpatialAxes());
  }
  
  @Override public Delta rotate(AxisAngle axisAngle, Sense sense) {
    return new Delta(a.rotate(axisAngle, sense), b.rotate(axisAngle, sense));
  }
  
  private Delta(Position a, Position b) {
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