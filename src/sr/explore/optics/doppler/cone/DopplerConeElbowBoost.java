package sr.explore.optics.doppler.cone;

import static sr.core.component.ops.NSense.ChangeComponents;
import static sr.core.component.ops.NSense.ChangeGrid;

import sr.core.Axis;
import sr.core.Util;
import sr.core.component.ops.NBoost;
import sr.core.vec3.NAxisAngle;
import sr.core.vec3.NPhaseGradient;
import sr.core.vec3.NVelocity;
import sr.core.vec4.NFourPhaseGradient;
import sr.explore.Exploration;
import sr.output.text.TextOutput;

/**
 How a uniform set of {@link NFourPhaseGradient}s are affected by a {@link NBoost}.
 
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
    NFourPhaseGradient k_K = NFourPhaseGradient.of(NPhaseGradient.of(1.0, Axis.X));
    int num = 360;
    
    output_K.addComment("Wave-vectors in K.");
    output_Kp.addComment("Wave-vectors in Kp.");
    for(int i = 0; i <= num; i=i+10) {
      NFourPhaseGradient wv_rotated_K = rotated(k_K, i);
      NFourPhaseGradient wv_rotated_Kp = cornerBoostOf(wv_rotated_K, Axis.Z, 0.25, 0.60);
      output_K.add(wv_rotated_K);
      output_Kp.add(wv_rotated_Kp);
    }
    output_K.outputTo("output_elbow_K.txt", this);
    output_Kp.outputTo("output_elbow_Kp.txt", this);
  }
  
  private NFourPhaseGradient rotated(NFourPhaseGradient k, int numDegrees) {
    double rads = Util.degsToRads(numDegrees);
    return k.rotate(NAxisAngle.of(rads, Axis.Z), ChangeComponents);
  }
  
  private TextOutput output_K = new TextOutput();
  private TextOutput output_Kp = new TextOutput();
  
  /** Two boosts of the same speed, with the second perpendicular to the first. */
  private NFourPhaseGradient cornerBoostOf(NFourPhaseGradient k, Axis pole, double β1, double β2) {
    NFourPhaseGradient result = k.boost(NVelocity.of(β1, Axis.rightHandRuleFor(pole).get(0)), ChangeGrid);
    return                 result.boost(NVelocity.of(β2, Axis.rightHandRuleFor(pole).get(1)), ChangeGrid);
  }
}
