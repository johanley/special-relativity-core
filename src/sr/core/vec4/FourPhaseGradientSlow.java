package sr.core.vec4;

import sr.core.VelocityTransformation;
import sr.core.component.Components;
import sr.core.component.ops.Boost;
import sr.core.component.ops.Sense;
import sr.core.ops.LinearBoostOp;
import sr.core.ops.LinearOps;
import sr.core.vec3.AxisAngle;
import sr.core.vec3.PhaseGradient;
import sr.core.vec3.Velocity;

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
public final class FourPhaseGradientSlow extends FourVector implements LinearOps<FourPhaseGradientSlow>, LinearBoostOp<FourPhaseGradientSlow> {

  /**
   Factory method.
   @param phase_velocity less than 1
  */
  public static FourPhaseGradientSlow of(PhaseGradient k, Velocity phase_velocity) {
    return new FourPhaseGradientSlow(k, phase_velocity);
  }
  
  @Override public FourPhaseGradientSlow reverseClocks() {
    Velocity new_phase_velocity = phase_velocity.reverseClocks();
    return FourPhaseGradientSlow.of(k, new_phase_velocity);
  }
  
  @Override public FourPhaseGradientSlow reverseSpatialAxes() {
    Velocity new_phase_velocity = phase_velocity.reverseSpatialAxes();
    PhaseGradient new_k = k.reverseSpatialAxes();
    return FourPhaseGradientSlow.of(new_k, new_phase_velocity);
  }
  
  @Override public FourPhaseGradientSlow rotate(AxisAngle axisAngle, Sense sense) {
    Velocity new_phase_velocity = phase_velocity.rotate(axisAngle, sense);
    PhaseGradient new_k = k.rotate(axisAngle, sense);
    return FourPhaseGradientSlow.of(new_k, new_phase_velocity);
  }
  
  @Override public FourPhaseGradientSlow boost(Velocity boost_v, Sense sense) {
    Velocity new_phase_velocity = VelocityTransformation.primedVelocity(boost_v, phase_velocity);
    Boost boost = Boost.of(boost_v, sense);
    Components new_comps = boost.applyTo(components);
    PhaseGradient new_k = PhaseGradient.of(new_comps.x(), new_comps.y(), new_comps.z());
    return FourPhaseGradientSlow.of(new_k, new_phase_velocity);
  }
  
  private PhaseGradient k;
  
  /** The phase velocity (u). */
  private Velocity phase_velocity;
  
  private FourPhaseGradientSlow(PhaseGradient k, Velocity phase_velocity) {
    this.k = k;
    this.phase_velocity = phase_velocity;
    this.components = Components.of(k.dot(phase_velocity), k.x(), k.y(), k.z());
  }
}
