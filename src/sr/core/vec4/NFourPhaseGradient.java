package sr.core.vec4;

import sr.core.component.NComponents;
import sr.core.component.ops.NBoost;
import sr.core.component.ops.NSense;
import sr.core.ops.NLinearBoostOp;
import sr.core.ops.NLinearOps;
import sr.core.vec3.NAxisAngle;
import sr.core.vec3.NPhaseGradient;
import sr.core.vec3.NVelocity;

/**
 Phase-gradient (wave vector) <em>k<sup>i</sup></em> for a plane monochromatic wave with phase-velocity as the speed limit <em>c</em>.
 <P>This corresponds to a light wave travelling in a vacuum.
 
 <p>The general relation is <em>ω = c*k</em>. 
 <b>In this project <em>c=1</em>, so ω and <em>k</em> are numerically the same.
 This class considers them as interchangeable.</b>
  
 <P>The space components are the direction unit-vector multiplied by <em>k<em>(or ω).
*/
public final class NFourPhaseGradient extends NFourVector implements NLinearOps<NFourPhaseGradient>, NLinearBoostOp<NFourPhaseGradient>{
  
  public static NFourPhaseGradient of(NPhaseGradient k) {
    return new NFourPhaseGradient(k);
  }
  
  public NPhaseGradient k() { return k; }
  
  /** No effect on the phase-gradient. */
  @Override public NFourPhaseGradient reverseClocks() {
    NPhaseGradient k_new = k.reverseClocks();
    return NFourPhaseGradient.of(k_new);
  }
  
  @Override public NFourPhaseGradient reverseSpatialAxes() {
    NPhaseGradient k_new = k.reverseSpatialAxes();
    return NFourPhaseGradient.of(k_new);
  }
  
  @Override public NFourPhaseGradient rotate(NAxisAngle axisAngle, NSense sense) {
    NPhaseGradient k_new = k.rotate(axisAngle, sense);
    return NFourPhaseGradient.of(k_new);
  }
  
  @Override public NFourPhaseGradient boost(NVelocity v, NSense sense) {
    NBoost boost = NBoost.of(v, sense);
    NComponents new_comps = boost.applyTo(components);
    //reverse-engineer the new comps to find k_new
    NPhaseGradient k_new = NPhaseGradient.of(new_comps.x(), new_comps.y(), new_comps.z());
    return NFourPhaseGradient.of(k_new);
  }
  
  private NPhaseGradient k;
  
  private NFourPhaseGradient(NPhaseGradient k) {
    this.k = k;
    this.components = NComponents.of(k.magnitude(), k.x(), k.y(), k.z());
  }
}
