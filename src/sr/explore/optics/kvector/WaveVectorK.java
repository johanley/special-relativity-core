package sr.explore.optics.kvector;

import static sr.core.Axis.CT;
import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Util.NL;
import static sr.core.Util.radsToDegs;

import sr.core.Axis;
import sr.core.Util;
import sr.core.component.ops.Sense;
import sr.core.vec3.Direction;
import sr.core.vec3.PhaseGradient;
import sr.core.vec3.Velocity;
import sr.core.vec4.FourPhaseGradient;
import sr.core.vec4.FourVector;
import sr.explore.Exploration;
import sr.output.text.TextOutput;

public final class WaveVectorK extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    Exploration aberration = new WaveVectorK();
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
    Velocity v = Velocity.of(0.6, axis);
    sameLine(axis, v);
    
    add(NL + "A boost anti-parallel to the wave-vector k changes the frequency only.");
    sameLine(axis, Velocity.of(v.times(-1)));
  }

  private void sameLine(Axis axis, Velocity v) {
    FourPhaseGradient k_in = FourPhaseGradient.of(PhaseGradient.of(1.0, axis));
    FourPhaseGradient k_out = k_in.boost(Velocity.of(v), Sense.ChangeGrid);
    show(v, k_in, k_out);
  }

  private void notTheSameLineChangesFrequencyAndDirection() {
    add(NL + "A random boost not parallel to the wave-vector k changes both the frequency and the direction.");
    Velocity v = Velocity.of(0.6, Axis.X);
    FourPhaseGradient k_in = FourPhaseGradient.of(PhaseGradient.of(1.0, Direction.of(1, 2, 3)));
    FourPhaseGradient k_out = k_in.boost(v, Sense.ChangeGrid);
    show(v, k_in, k_out);
  }
  
  private void abberation() {
    add(NL+"Abberation.");
    add("Input k in frame K is directed to the 4th quadrant, at 45 degrees down from the +X-axis.");
    Velocity v = Velocity.of(-0.60, Axis.X);
    //directed into the 4th quadrant at 45 degrees
    FourPhaseGradient k_in = FourPhaseGradient.of(PhaseGradient.of(1.0, Direction.of(+1, -1, 0)));
    FourPhaseGradient k_out = k_in.boost(v, Sense.ChangeGrid);
    show(v, k_in, k_out);
    showChangeInDirection(k_in, k_out);
  }
  
  private void show(Velocity boost_v, FourVector input, FourVector output) {
    add("  Boost "  + boost_v);
    add("  Input k in frame K "  + input + " mag " + round(input.square()));
    add("  Output k in frame K'" + output + " mag " + round(output.square()));
  }
  
  private void showChangeInDirection(FourPhaseGradient k_in, FourVector k_out) {
    double degsIn = degreesWrtXaxis(k_in);
    double degsOut = degreesWrtXaxis(k_out);
    
    add("  Input k angle in frame K with respect to the X-axis: " + degsIn);
    add("  Output k angle in frame K' with respect to the X-axis: " + degsOut);
    add("  Change in direction of k: " + round(degsOut - degsIn) + "°");
    add("  Change in frequency by factor: " + round(k_out.on(CT)/k_in.on(CT)));
  }
  
  private double degreesWrtXaxis(FourVector k) {
    double rads = Math.atan2(k.on(Y), k.on(X)); //-pi..+pi
    return round(radsToDegs(rads));
  }
  
  private double round(double val) {
    return Util.round(val, 6);
  }
}
