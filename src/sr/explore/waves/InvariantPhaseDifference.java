package sr.explore.waves;

import sr.core.Axis;
import sr.core.Util;
import sr.core.component.NEvent;
import sr.core.component.NPosition;
import static sr.core.component.ops.NSense.*;
import sr.core.vec3.NAxisAngle;
import sr.core.vec3.NDirection;
import sr.core.vec3.NPhaseGradient;
import sr.core.vec3.NVelocity;
import sr.core.vec4.NFourDelta;
import sr.core.vec4.NFourPhaseGradient;
import sr.explore.Exploration;
import sr.output.text.TextOutput;

/**
 For waves, differences in phase are Poincaré-invariant.
 
 <P>The value of the phase itself is not Poincaré-invariant. 
 This is a common misconception.
*/
public final class InvariantPhaseDifference extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    InvariantPhaseDifference invariantPhase = new InvariantPhaseDifference();
    invariantPhase.explore();
  }
  
  @Override public void explore() {
    add("Examine the dot product k.x, the phase of a plane wave of a single frequency.");
    add(" - k is the phase-gradient four-vector.");
    add(" - x is an event." + Util.NL);
    add(Util.NL+"Use the phase-gradient k for light waves in a vacuum."+Util.NL);
    wavesInVacuum();
    outputToConsoleAnd("invariant-phase-difference.txt");
  }
  
  private void wavesInVacuum() {
    NFourPhaseGradient k_K = NFourPhaseGradient.of(NPhaseGradient.of(1.0, NDirection.of(1.0, 1.0, 1.0)));
    NEvent event_a_K = NEvent.of(10.0, NPosition.of(2, 3, 4));
    add("K: event:" + event_a_K);
    add("K: k:" + k_K);
    add("K: dot-product k.x: " + round(pseudoDot(k_K, event_a_K)));
    
    transformBoost(event_a_K, k_K);
    transformRotate(event_a_K, k_K);
    transformDisplace(event_a_K, k_K); 
    
    add(Util.NL+"For displacement operations, you need to examine DIFFERENCES in k.x for two different events:");
    NEvent event_b_K = NEvent.of(11, 5, 22, 7);
    add("K: event:" + event_a_K + " a");
    add("K: event:" + event_b_K + " b");
    add("K: k:" + k_K);
    add("K: difference in the dot-product (k.b - k.a) = k.(b-a): " + round(pseudoDot(k_K, event_b_K) - pseudoDot(k_K, event_a_K)));
    
    NFourDelta delta = NFourDelta.withRespectToOrigin(NEvent.of(0.0, 85, 0, 0));
    add("Transform: displacement" + delta);
    NEvent event_a_Kp = event_a_K.moveZeroPointBy(delta, ChangeGrid);
    NEvent event_b_Kp = event_b_K.moveZeroPointBy(delta, ChangeGrid);
    //k is unaffected by displacement operations!
    NFourPhaseGradient k_Kp = k_K;
    add("K': difference in the dot-product (k'.b' - k'.a') = k'.(b'-a'): " + round(pseudoDot(k_Kp, event_b_Kp) - pseudoDot(k_Kp, event_a_Kp)));
    add(Util.NL+"Remember that the true 4-vector is Δx, not x.");
  }

  private void transformBoost(NEvent event, NFourPhaseGradient k) {
    NVelocity boost_v = NVelocity.of(0.55, Axis.X);
    add(Util.NL+"Transform: boost " + boost_v);
    NFourPhaseGradient k_Kp = k.boost(boost_v, ChangeGrid);
    NEvent event_Kp = event.boost(boost_v, ChangeGrid);
    double dotProd_Kp = pseudoDot(k_Kp, event_Kp);
    add("K': dot-product k'.x': " + round(dotProd_Kp));
  }
  
  private void transformRotate(NEvent event, NFourPhaseGradient k) {
    NAxisAngle rot = NAxisAngle.of(1,2,3);
    add(Util.NL+"Transform: rotation" + rot);
    NFourPhaseGradient k_Kp = k.rotate(rot, ChangeGrid);
    NEvent event_Kp = event.rotate(rot, ChangeGrid);
    double dotProd_Kp = pseudoDot(k_Kp, event_Kp);
    add("K': dot-product k'.x': " + round(dotProd_Kp));
  }
  
  private void transformDisplace(NEvent event, NFourPhaseGradient k) {
    NFourDelta delta = NFourDelta.withRespectToOrigin(NEvent.of(0,85,0,0));
    add(Util.NL+"Transform: displacement" + delta);
    NEvent event_Kp = event.moveZeroPointBy(delta, ChangeGrid);
    //the k vector is insensitive to the displacement operation!
    double dotProd_Kp = pseudoDot(k, event_Kp);
    add("K': dot-product k'.x': " + round(dotProd_Kp));
  }
  
  private double round(double value) {
    return Util.round(value, 5);
  }
  
  /** NEvent has no dot-product, since it's not a four-vector. */
  private double pseudoDot(NFourPhaseGradient k, NEvent event) {
    return 
      + k.ct() * event.ct() 
      - k.x() * event.x()
      - k.y() * event.y()
      - k.z() * event.z()
    ;
  }
}
