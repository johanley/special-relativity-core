package sr.core.vec4;

import sr.core.component.Components;
import sr.core.component.ops.Boost;
import sr.core.component.ops.Sense;
import sr.core.ops.LinearBoostOp;
import sr.core.ops.LinearOps;
import sr.core.vec3.AxisAngle;
import sr.core.vec3.PhaseGradient;
import sr.core.vec3.Velocity;

/**
 Phase-gradient (wave vector) <em>k<sup>i</sup></em> for a plane monochromatic wave with phase-velocity as the speed limit <em>c</em>.
 <P>This corresponds to a light wave traveling in a vacuum.
 
 <p>The general relation is <em>ω = c*k</em>. 
 <b>In this project <em>c=1</em>, so ω and <em>k</em> are numerically the same.
 This class considers them as interchangeable.</b>
  
 <P>The space components are the direction unit-vector multiplied by <em>k</em>(or ω).
*/
public final class FourPhaseGradient extends FourVector implements LinearOps<FourPhaseGradient>, LinearBoostOp<FourPhaseGradient> {
  
  public static FourPhaseGradient of(PhaseGradient k) {
    return new FourPhaseGradient(k);
  }

  /** The phase-gradient 3-vector. */
  public PhaseGradient k() { return k; }
  
  /** No effect. */
  @Override public FourPhaseGradient reverseClocks() {
    return FourPhaseGradient.of(k.reverseClocks());
  }
  
  @Override public FourPhaseGradient reverseSpatialAxes() {
    return FourPhaseGradient.of(k.reverseSpatialAxes());
  }
  
  @Override public FourPhaseGradient rotate(AxisAngle axisAngle, Sense sense) {
    return FourPhaseGradient.of(k.rotate(axisAngle, sense));
  }
  
  @Override public FourPhaseGradient boost(Velocity v, Sense sense) {
    Boost boost = Boost.of(v, sense);
    Components new_comps = boost.applyTo(components);
    //reverse-engineer the new comps to find k_new
    PhaseGradient k_new = PhaseGradient.of(new_comps.x(), new_comps.y(), new_comps.z());
    return FourPhaseGradient.of(k_new);
  }
  
  private PhaseGradient k;
  
  private FourPhaseGradient(PhaseGradient k) {
    this.k = k;
    this.components = Components.of(k.magnitude(), k.x(), k.y(), k.z());
  }
}
