package sr.core.vec4;

import sr.core.NVelocityTransformation;
import sr.core.component.NComponents;
import sr.core.component.ops.NBoost;
import sr.core.component.ops.NSense;
import sr.core.ops.NLinearBoostOp;
import sr.core.ops.NLinearOps;
import sr.core.vec3.NAxisAngle;
import sr.core.vec3.NPhaseGradient;
import sr.core.vec3.NVelocity;

/**
 Four phase-gradient <em>k<sup>i</sup></em> for a plane monochromatic wave of any speed.
 Here, <em>gradient</em> includes a time component (a time-derivative).

 <P>Core data:
 <ul>
  <li>velocity of the wave <b>u</b> (the phase velocity)
  <li>the spatial phase gradient <b>k</b> as a vector (the change in phase per unit distance). 
  The spatial phase-gradient is always perpendicular to the wavefront.
 </ul>

 <P>Important to note: <b>The direction of <em>k</em> is not necessarily the same as the direction of <em>u</em>.</b>
 For a plane wave moving in a stationary medium, the two vectors are indeed parallel.
 But when you boost to another frame moving relative to the stationary medium, then they are usually not parallel.

 <P>The angular frequency ω of the wave equals <em>k.u</em>.
 
 <P>This implementation is more general than the typical model you find in textbooks. 
 Here, the speed of the wave is allowed to be less than the speed limit.
 Thus, this class can be used as a simple model of light in a medium, or of sound in a medium.
 
 <P>Reference: <a href='https://arxiv.org/pdf/0801.3149v2'>article</a> of Aleksandar Gjurchinovski (2008) for the formula for the 
  phase-gradient in the more general case.
*/
public final class NFourPhaseGradientSlow extends NFourVector implements NLinearOps<NFourPhaseGradientSlow>, NLinearBoostOp<NFourPhaseGradientSlow> {

  /**
   Factory method.
   @param phase_velocity less than 1
  */
  public static NFourPhaseGradientSlow of(NPhaseGradient k, NVelocity phase_velocity) {
    return new NFourPhaseGradientSlow(k, phase_velocity);
  }
  
  @Override public NFourPhaseGradientSlow reverseClocks() {
    NVelocity new_phase_velocity = phase_velocity.reverseClocks();
    return NFourPhaseGradientSlow.of(k, new_phase_velocity);
  }
  
  @Override public NFourPhaseGradientSlow reverseSpatialAxes() {
    NVelocity new_phase_velocity = phase_velocity.reverseSpatialAxes();
    NPhaseGradient new_k = k.reverseSpatialAxes();
    return NFourPhaseGradientSlow.of(new_k, new_phase_velocity);
  }
  
  @Override public NFourPhaseGradientSlow rotate(NAxisAngle axisAngle, NSense sense) {
    NVelocity new_phase_velocity = phase_velocity.rotate(axisAngle, sense);
    NPhaseGradient new_k = k.rotate(axisAngle, sense);
    return NFourPhaseGradientSlow.of(new_k, new_phase_velocity);
  }
  
  @Override public NFourPhaseGradientSlow boost(NVelocity boost_v, NSense sense) {
    NVelocity new_phase_velocity = NVelocityTransformation.primedVelocity(boost_v, phase_velocity);
    NBoost boost = NBoost.of(boost_v, sense);
    NComponents new_comps = boost.applyTo(components);
    NPhaseGradient new_k = NPhaseGradient.of(new_comps.x(), new_comps.y(), new_comps.z());
    return NFourPhaseGradientSlow.of(new_k, new_phase_velocity);
  }
  
  private NPhaseGradient k;
  
  /** The phase velocity (u). */
  private NVelocity phase_velocity;
  
  private NFourPhaseGradientSlow(NPhaseGradient k, NVelocity phase_velocity) {
    this.k = k;
    this.phase_velocity = phase_velocity;
    this.components = NComponents.of(k.dot(phase_velocity), k.x(), k.y(), k.z());
  }
}
