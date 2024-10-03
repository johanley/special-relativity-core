package sr.core.vec4;

import sr.core.component.Components;
import sr.core.component.ops.Boost;
import sr.core.component.ops.Sense;
import sr.core.ops.LinearBoostOp;
import sr.core.ops.LinearOps;
import sr.core.vec3.NAxisAngle;
import sr.core.vec3.NPhaseGradient;
import sr.core.vec3.NVelocity;

/**
 Phase-gradient (wave vector) <em>k<sup>i</sup></em> for a plane monochromatic wave with phase-velocity as the speed limit <em>c</em>.
 <P>This corresponds to a light wave traveling in a vacuum.
 
 <p>The general relation is <em>ω = c*k</em>. 
 <b>In this project <em>c=1</em>, so ω and <em>k</em> are numerically the same.
 This class considers them as interchangeable.</b>
  
 <P>The space components are the direction unit-vector multiplied by <em>k<em>(or ω).
*/
public final class NFourPhaseGradient extends NFourVector implements LinearOps<NFourPhaseGradient>, LinearBoostOp<NFourPhaseGradient> {
  
  public static NFourPhaseGradient of(NPhaseGradient k) {
    return new NFourPhaseGradient(k);
  }

  /** The phase-gradient 3-vector. */
  public NPhaseGradient k() { return k; }
  
  /** No effect. */
  @Override public NFourPhaseGradient reverseClocks() {
    return NFourPhaseGradient.of(k.reverseClocks());
  }
  
  @Override public NFourPhaseGradient reverseSpatialAxes() {
    return NFourPhaseGradient.of(k.reverseSpatialAxes());
  }
  
  @Override public NFourPhaseGradient rotate(NAxisAngle axisAngle, Sense sense) {
    return NFourPhaseGradient.of(k.rotate(axisAngle, sense));
  }
  
  @Override public NFourPhaseGradient boost(NVelocity v, Sense sense) {
    Boost boost = Boost.of(v, sense);
    Components new_comps = boost.applyTo(components);
    //reverse-engineer the new comps to find k_new
    NPhaseGradient k_new = NPhaseGradient.of(new_comps.x(), new_comps.y(), new_comps.z());
    return NFourPhaseGradient.of(k_new);
  }
  
  private NPhaseGradient k;
  
  private NFourPhaseGradient(NPhaseGradient k) {
    this.k = k;
    this.components = Components.of(k.magnitude(), k.x(), k.y(), k.z());
  }
}
