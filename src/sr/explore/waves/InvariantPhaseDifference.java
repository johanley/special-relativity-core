package sr.explore.waves;

import sr.core.Axis;
import sr.core.Util;
import sr.core.component.Event;
import sr.core.component.Position;
import static sr.core.component.ops.Sense.*;
import sr.core.vec3.AxisAngle;
import sr.core.vec3.Direction;
import sr.core.vec3.PhaseGradient;
import sr.core.vec3.Velocity;
import sr.core.vec4.FourDelta;
import sr.core.vec4.FourPhaseGradient;
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
    FourPhaseGradient k_K = FourPhaseGradient.of(PhaseGradient.of(1.0, Direction.of(1.0, 1.0, 1.0)));
    Event event_a_K = Event.of(10.0, Position.of(2, 3, 4));
    add("K: event:" + event_a_K);
    add("K: k:" + k_K);
    add("K: dot-product k.x: " + round(pseudoDot(k_K, event_a_K)));
    
    transformBoost(event_a_K, k_K);
    transformRotate(event_a_K, k_K);
    transformDisplace(event_a_K, k_K); 
    
    add(Util.NL+"For displacement operations, you need to examine DIFFERENCES in k.x for two different events:");
    Event event_b_K = Event.of(11, 5, 22, 7);
    add("K: event:" + event_a_K + " a");
    add("K: event:" + event_b_K + " b");
    add("K: k:" + k_K);
    add("K: difference in the dot-product (k.b - k.a) = k.(b-a): " + round(pseudoDot(k_K, event_b_K) - pseudoDot(k_K, event_a_K)));
    
    FourDelta delta = FourDelta.withRespectToOrigin(Event.of(0.0, 85, 0, 0));
    add("Transform: displacement" + delta);
    Event event_a_Kp = event_a_K.moveZeroPointBy(delta, ChangeGrid);
    Event event_b_Kp = event_b_K.moveZeroPointBy(delta, ChangeGrid);
    //k is unaffected by displacement operations!
    FourPhaseGradient k_Kp = k_K;
    add("K': difference in the dot-product (k'.b' - k'.a') = k'.(b'-a'): " + round(pseudoDot(k_Kp, event_b_Kp) - pseudoDot(k_Kp, event_a_Kp)));
    add(Util.NL+"Remember that the true 4-vector is Δx, not x.");
  }

  private void transformBoost(Event event, FourPhaseGradient k) {
    Velocity boost_v = Velocity.of(0.55, Axis.X);
    add(Util.NL+"Transform: boost " + boost_v);
    FourPhaseGradient k_Kp = k.boost(boost_v, ChangeGrid);
    Event event_Kp = event.boost(boost_v, ChangeGrid);
    double dotProd_Kp = pseudoDot(k_Kp, event_Kp);
    add("K': dot-product k'.x': " + round(dotProd_Kp));
  }
  
  private void transformRotate(Event event, FourPhaseGradient k) {
    AxisAngle rot = AxisAngle.of(1,2,3);
    add(Util.NL+"Transform: rotation" + rot);
    FourPhaseGradient k_Kp = k.rotate(rot, ChangeGrid);
    Event event_Kp = event.rotate(rot, ChangeGrid);
    double dotProd_Kp = pseudoDot(k_Kp, event_Kp);
    add("K': dot-product k'.x': " + round(dotProd_Kp));
  }
  
  private void transformDisplace(Event event, FourPhaseGradient k) {
    FourDelta delta = FourDelta.withRespectToOrigin(Event.of(0,85,0,0));
    add(Util.NL+"Transform: displacement" + delta);
    Event event_Kp = event.moveZeroPointBy(delta, ChangeGrid);
    //the k vector is insensitive to the displacement operation!
    double dotProd_Kp = pseudoDot(k, event_Kp);
    add("K': dot-product k'.x': " + round(dotProd_Kp));
  }
  
  private double round(double value) {
    return Util.round(value, 5);
  }
  
  /** NEvent has no dot-product, since it's not a four-vector. */
  private double pseudoDot(FourPhaseGradient k, Event event) {
    return 
      + k.ct() * event.ct() 
      - k.x() * event.x()
      - k.y() * event.y()
      - k.z() * event.z()
    ;
  }
}
