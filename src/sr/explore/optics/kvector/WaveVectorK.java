package sr.explore.optics.kvector;

import static sr.core.Axis.CT;
import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Util.NL;
import static sr.core.Util.radsToDegs;

import sr.core.Axis;
import sr.core.Util;
import sr.core.component.ops.NSense;
import sr.core.vec3.NDirection;
import sr.core.vec3.NPhaseGradient;
import sr.core.vec3.NVelocity;
import sr.core.vec4.NFourPhaseGradient;
import sr.core.vec4.NFourVector;
import sr.explore.Exploration;
import sr.output.text.TextOutput;

public final class WaveVectorK extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    WaveVectorK aberration = new WaveVectorK();
    aberration.explore();
  }
  
  @Override public void explore() {
    sameLineChangesFrequencyOnly();
    notTheSameLineChangesFrequencyAndDirection();
    abberation();
    outputToConsoleAnd("wave-vector-k.txt");
  }
  
  private void sameLineChangesFrequencyOnly() {
    add("A boost parallel to the wave-vector k changes the frequency only.");
    Axis axis = Axis.X;
    NVelocity v = NVelocity.of(0.6, axis);
    sameLine(axis, v);
    
    add(NL + "A boost anti-parallel to the wave-vector k changes the frequency only.");
    sameLine(axis, NVelocity.of(v.times(-1)));
  }

  private void sameLine(Axis axis, NVelocity v) {
    NFourPhaseGradient k_in = NFourPhaseGradient.of(NPhaseGradient.of(1.0, axis));
    NFourPhaseGradient k_out = k_in.boost(NVelocity.of(v), NSense.ChangeGrid);
    show(v, k_in, k_out);
  }

  private void notTheSameLineChangesFrequencyAndDirection() {
    add(NL + "A random boost not parallel to the wave-vector k changes both the frequency and the direction.");
    NVelocity v = NVelocity.of(0.6, Axis.X);
    NFourPhaseGradient k_in = NFourPhaseGradient.of(NPhaseGradient.of(1.0, NDirection.of(1, 2, 3)));
    NFourPhaseGradient k_out = k_in.boost(v, NSense.ChangeGrid);
    show(v, k_in, k_out);
  }
  
  private void abberation() {
    add(NL+"Abberation.");
    add("Input k in frame K is directed to the 4th quadrant, at 45 degrees down from the +X-axis.");
    NVelocity v = NVelocity.of(-0.60, Axis.X);
    //directed into the 4th quadrant at 45 degrees
    NFourPhaseGradient k_in = NFourPhaseGradient.of(NPhaseGradient.of(1.0, NDirection.of(+1, -1, 0)));
    NFourPhaseGradient k_out = k_in.boost(v, NSense.ChangeGrid);
    show(v, k_in, k_out);
    showChangeInDirection(k_in, k_out);
  }
  
  private void show(NVelocity boost_v, NFourVector input, NFourVector output) {
    add("  Boost "  + boost_v);
    add("  Input k in frame K "  + input + " mag " + round(input.square()));
    add("  Output k in frame K'" + output + " mag " + round(output.square()));
  }
  
  private void showChangeInDirection(NFourPhaseGradient k_in, NFourVector k_out) {
    double degsIn = degreesWrtXaxis(k_in);
    double degsOut = degreesWrtXaxis(k_out);
    
    add("  Input k angle in frame K with respect to the X-axis: " + degsIn);
    add("  Output k angle in frame K' with respect to the X-axis: " + degsOut);
    add("  Change in direction of k: " + round(degsOut - degsIn) + "°");
    add("  Change in frequency by factor: " + round(k_out.on(CT)/k_in.on(CT)));
  }
  
  private double degreesWrtXaxis(NFourVector k) {
    double rads = Math.atan2(k.on(Y), k.on(X)); //-pi..+pi
    return round(radsToDegs(rads));
  }
  
  private double round(double val) {
    return Util.round(val, 6);
  }
}
