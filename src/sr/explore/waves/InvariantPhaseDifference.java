package sr.explore.waves;

import sr.core.Axis;
import sr.core.Util;
import sr.core.vector3.AxisAngle;
import sr.core.vector3.Direction;
import sr.core.vector3.Position;
import sr.core.vector4.Event;
import sr.core.vector4.FourPhaseGradient;
import sr.core.vector4.transform.Boost;
import sr.core.vector4.transform.Displacement;
import sr.core.vector4.transform.Rotation;
import sr.core.vector4.transform.Transform;
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
    
    FourPhaseGradient k_K = FourPhaseGradient.of(1.0, Direction.of(1.0, 1.0, 1.0));
    Event event_a_K = Event.of(10.0, Position.of(2, 3, 4));
    add("K: event:" + event_a_K);
    add("K: k:" + k_K);
    add("K: dot-product k.x: " + round(k_K.dot(event_a_K)));
    
    transform(event_a_K, k_K, Boost.of(Axis.X, 0.55));
    transform(event_a_K, k_K, Rotation.of(AxisAngle.of(1, 2, 3)));
    //transform(event_K, k_K, Reflection.allAxes()); FAILING!!!!
    transform(event_a_K, k_K, Displacement.along(Axis.X, 85)); //NOT THE SAME VALUE!! The value of this dot product is not invariant.
    
    add(Util.NL+"For displacement operations, you need to examine DIFFERENCES in k.x for two different events:");
    Event event_b_K = Event.of(11, 5, 22, 7);
    add("K: event:" + event_a_K + " a");
    add("K: event:" + event_b_K + " b");
    add("K: k:" + k_K);
    add("K: difference in the dot-product (k.b - k.a) = k.(b-a): " + round(k_K.dot(event_b_K) - k_K.dot(event_a_K)));
    Transform transform = Displacement.along(Axis.X, 85);
    add("Transform: " + transform);
    Event event_a_Kp = transform.changeGrid(event_a_K);
    Event event_b_Kp = transform.changeGrid(event_b_K);
    FourPhaseGradient k_Kp = transform.changeGrid(k_K);
    add("K': difference in the dot-product (k'.b' - k'.a') = k'.(b'-a'): " + round(k_Kp.dot(event_b_Kp) - k_Kp.dot(event_a_Kp)));
    add(Util.NL+"Remember that the true 4-vector is Δx, not x.");
  }
  
  private void transform(Event event, FourPhaseGradient k, Transform transform) {
    add(Util.NL+"Transform: " + transform);
    FourPhaseGradient k_Kp = transform.changeGrid(k);
    Event event_Kp = transform.changeGrid(event);
    double dotProd_Kp = k_Kp.dot(event_Kp);
    add("K': dot-product k'.x': " + round(dotProd_Kp));
  }
  
  private double round(double value) {
    return Util.round(value, 5);
  }

}
