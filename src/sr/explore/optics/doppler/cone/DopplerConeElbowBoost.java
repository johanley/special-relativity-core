package sr.explore.optics.doppler.cone;

import static sr.core.component.ops.Sense.ChangeComponents;
import static sr.core.component.ops.Sense.ChangeGrid;

import sr.core.Axis;
import sr.core.Util;
import sr.core.component.ops.Boost;
import sr.core.vec3.AxisAngle;
import sr.core.vec3.PhaseGradient;
import sr.core.vec3.Velocity;
import sr.core.vec4.FourPhaseGradient;
import sr.explore.Exploration;
import sr.output.text.TextOutput;

/**
 How a uniform set of {@link FourPhaseGradient}s are affected by a {@link Boost}.
 
 <P>Start with a set of wave-vectors having the same frequency, but different directions.
 In space-time, this set generates a cone shape.
 Under a Lorentz boost, the cone becomes distorted, somewhat like water sloshing in a bucket that's been pushed.
*/
public final class DopplerConeElbowBoost implements Exploration {
  
  public static void main(String[] args) {
    DopplerConeElbowBoost dopplerCone = new DopplerConeElbowBoost();
    dopplerCone.explore();
  }
  
  @Override public void explore() {
    //base wave-vector in frame K
    FourPhaseGradient k_K = FourPhaseGradient.of(PhaseGradient.of(1.0, Axis.X));
    int num = 360;
    
    output_K.addComment("Wave-vectors in K.");
    output_Kp.addComment("Wave-vectors in Kp.");
    for(int i = 0; i <= num; i=i+10) {
      FourPhaseGradient wv_rotated_K = rotated(k_K, i);
      FourPhaseGradient wv_rotated_Kp = cornerBoostOf(wv_rotated_K, Axis.Z, 0.25, 0.60);
      output_K.add(wv_rotated_K);
      output_Kp.add(wv_rotated_Kp);
    }
    output_K.outputTo("output_elbow_K.txt", this);
    output_Kp.outputTo("output_elbow_Kp.txt", this);
  }
  
  private FourPhaseGradient rotated(FourPhaseGradient k, int numDegrees) {
    double rads = Util.degsToRads(numDegrees);
    return k.rotate(AxisAngle.of(rads, Axis.Z), ChangeComponents);
  }
  
  private TextOutput output_K = new TextOutput();
  private TextOutput output_Kp = new TextOutput();
  
  /** Two boosts of the same speed, with the second perpendicular to the first. */
  private FourPhaseGradient cornerBoostOf(FourPhaseGradient k, Axis pole, double β1, double β2) {
    FourPhaseGradient result = k.boost(Velocity.of(β1, Axis.rightHandRuleFor(pole).get(0)), ChangeGrid);
    return                 result.boost(Velocity.of(β2, Axis.rightHandRuleFor(pole).get(1)), ChangeGrid);
  }
}
