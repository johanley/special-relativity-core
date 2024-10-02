package sr.explore.optics.doppler.cone;

import sr.core.Axis;
import sr.core.LorentzTransformation;
import sr.core.Util;
import sr.core.vector4.FourVector;
import sr.core.vector4.FourPhaseGradient;
import sr.core.vector4.transform.Boost;
import sr.core.vector4.transform.Rotation;
import sr.core.vector4.transform.Transform;
import sr.core.vector4.transform.TransformPipeline;
import sr.explore.Exploration;
import sr.output.text.TextOutput;

/**
 How a uniform set of {@link FourPhaseGradient}s are affected by a {@link LorentzTransformation}.
 
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
    FourPhaseGradient k_K = FourPhaseGradient.of(1.0, Axis.X);
    int num = 360;
    
    output_K.addComment("Wave-vectors in K.");
    output_Kp.addComment("Wave-vectors in Kp.");
    Transform elbowBoost = asCornerBoost(Axis.Z, 0.25, 0.60);
    for(int i = 0; i <= num; i=i+10) {
      FourPhaseGradient wv_rotated_K = rotated(k_K, i);
      FourPhaseGradient wv_rotated_Kp = elbowBoost.changeGrid(wv_rotated_K);
      output_K.add(wv_rotated_K);
      output_Kp.add(wv_rotated_Kp);
    }
    output_K.outputTo("output_elbow_K.txt", this);
    output_Kp.outputTo("output_elbow_Kp.txt", this);
  }
  
  private FourPhaseGradient rotated(FourPhaseGradient k, int numDegrees) {
    double rads = Util.degsToRads(numDegrees);
    Rotation rotation = Rotation.of(Axis.Z, rads);
    FourVector result = rotation.changeVector(k);
    return FourPhaseGradient.of(result.ct(), result.spatialComponents());
  }
  
  private TextOutput output_K = new TextOutput();
  private TextOutput output_Kp = new TextOutput();
  
  /** Two boosts of the same speed, with the second perpendicular to the first. */
  private Transform asCornerBoost(Axis pole, double β1, double β2) {
    Transform result = TransformPipeline.join(
      Boost.of(Axis.rightHandRuleFor(pole).get(0), β1),
      Boost.of(Axis.rightHandRuleFor(pole).get(1), β2)
    );
    return result;
  }
}
