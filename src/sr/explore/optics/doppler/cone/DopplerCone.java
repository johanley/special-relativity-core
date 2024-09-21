package sr.explore.optics.doppler.cone;

import sr.core.Axis;
import sr.core.LorentzTransformation;
import sr.core.Util;
import sr.core.vector3.Velocity;
import sr.core.vector4.FourVector;
import sr.core.vector4.FourPhaseGradient;
import sr.core.vector4.transform.Rotation;
import sr.explore.Exploration;
import sr.output.text.TextOutput;

/**
 How a uniform set of {@link FourPhaseGradient}s are affected by a {@link LorentzTransformation}.
 
 <P>Start with a set of wave-vectors having the same frequency, but different directions.
 In space-time, this set generates a cone shape.
 Under a Lorentz boost, the cone becomes distorted, somewhat like water sloshing in a bucket that's been pushed.
*/
public final class DopplerCone implements Exploration {
  
  public static void main(String[] args) {
    DopplerCone dopplerCone = new DopplerCone();
    //dopplerCone.generateOutputsForAnimation();
    dopplerCone.explore();
  }
  
  @Override public void explore() {
    //base wave-vector in frame K
    FourPhaseGradient k_K = FourPhaseGradient.of(1.0, Axis.X);
    int num = 360;
    double β = 0.5;
    
    output_K.addComment("Wave-vectors in K.");
    output_Kp.addComment("Wave-vectors in Kp.");
    LorentzTransformation lt = LorentzTransformation.of(Velocity.of(Axis.X, β));
    for(int i = 0; i <= num; i=i+10) {
      FourPhaseGradient k_rotated_K = rotated(k_K, i);
      FourPhaseGradient k_rotated_Kp = lt.primedVector(k_rotated_K);
      output_K.add(k_rotated_K);
      output_Kp.add(k_rotated_Kp);
    }
    output_K.outputTo("output_K.txt", this);
    output_Kp.outputTo("output_Kp.txt", this);
  }
  
  void generateOutputsForAnimation() {
    FourPhaseGradient k_K = FourPhaseGradient.of(1.0, Axis.X);
    int num = 360;
    for (int speed = 1; speed <= 50; ++speed) {
      double β = 0.01 * speed;
      TextOutput output = new TextOutput();
      LorentzTransformation lt = LorentzTransformation.of(Velocity.of(Axis.X, β));
      for(int i = 0; i <= num; i=i+10) {
        FourPhaseGradient k_rotated_K = rotated(k_K, i);
        FourPhaseGradient k_rotated_Kp = lt.primedVector(k_rotated_K);
        output.add(k_rotated_Kp);
      }
      output.outputTo("output_"+speed+".txt", this);
    }
  }
  
  private FourPhaseGradient rotated(FourPhaseGradient k, int numDegrees) {
    double rads = Util.degsToRads(numDegrees);
    Rotation rotation = Rotation.of(Axis.Z, rads);
    FourVector result = rotation.changeVector(k);
    return FourPhaseGradient.of(result.ct(), result.spatialComponents());
  }
  
  private TextOutput output_K = new TextOutput();
  private TextOutput output_Kp = new TextOutput();

}
