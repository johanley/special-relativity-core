package sr.explore.optics.kvector;

import static sr.core.Axis.*;
import static sr.core.Util.NL;
import static sr.core.Util.radsToDegs;

import sr.core.Axis;
import sr.core.LorentzTransformation3;
import sr.core.Util;
import sr.core.event.FourVector;
import sr.core.event.WaveVector;
import sr.core.vector.Direction;
import sr.core.vector.Velocity;
import sr.output.text.TextOutput;

public final class WaveVectorK extends TextOutput {
  
  public static void main(String[] args) {
    WaveVectorK aberration = new WaveVectorK();
    aberration.explore();
  }
  
  void explore() {
    sameLineChangesFrequencyOnly();
    notTheSameLineChangesFrequencyAndDirection();
    abberation();
    outputToConsoleAnd("wave-vector-k.txt");
  }
  
  private void sameLineChangesFrequencyOnly() {
    lines.add("A boost parallel to the wave-vector k changes the frequency only.");
    Axis axis = Axis.X;
    Velocity v = Velocity.of(axis, 0.6);
    sameLine(axis, v);
    
    lines.add(NL + "A boost anti-parallel to the wave-vector k changes the frequency only.");
    sameLine(axis, Velocity.of(v.times(-1)));
  }

  private void sameLine(Axis axis, Velocity v) {
    LorentzTransformation3 lt = LorentzTransformation3.of(v);
    WaveVector k_in = WaveVector.of(1.0, axis);
    WaveVector k_out = lt.primedVector(k_in);
    show(lt, k_in, k_out);
  }

  private void notTheSameLineChangesFrequencyAndDirection() {
    lines.add(NL + "A random boost not parallel to the wave-vector k changes both the frequency and the direction.");
    Velocity v = Velocity.of(Axis.X, 0.6);
    LorentzTransformation3 lt = LorentzTransformation3.of(v);
    WaveVector k_in = WaveVector.of(1.0, Direction.of(1, 2, 3));
    WaveVector k_out = lt.primedVector(k_in);
    show(lt, k_in, k_out);
  }
  
  private void abberation() {
    lines.add(NL+"Abberation.");
    lines.add("Input k in frame K is directed to the 4th quadrant, at 45 degrees down from the +X-axis.");
    Velocity v = Velocity.of(Axis.X, -0.60);
    LorentzTransformation3 lt = LorentzTransformation3.of(v);
    //directed into the 4th quadrant at 45 degrees
    WaveVector k_in = WaveVector.of(1.0, Direction.of(+1, -1, 0));
    WaveVector k_out = lt.primedVector(k_in);
    show(lt, k_in, k_out);
    showChangeInDirection(k_in, k_out);
  }
  
  private void show(LorentzTransformation3 lt, FourVector input, FourVector output) {
    lines.add("  Boost "  + lt);
    lines.add("  Input k in frame K "  + input + " mag " + round(input.square()));
    lines.add("  Output k in frame K'" + output + " mag " + round(output.square()));
  }
  
  private void showChangeInDirection(WaveVector k_in, FourVector k_out) {
    double degsIn = degreesWrtXaxis(k_in);
    double degsOut = degreesWrtXaxis(k_out);
    
    lines.add("  Input k angle in frame K with respect to the X-axis: " + degsIn);
    lines.add("  Output k angle in frame K' with respect to the X-axis: " + degsOut);
    lines.add("  Change in direction of k: " + round(degsOut - degsIn) + "°");
    lines.add("  Change in frequency by factor: " + round(k_out.on(CT)/k_in.on(CT)));
  }
  
  private double degreesWrtXaxis(FourVector k) {
    double rads = Math.atan2(k.on(Y), k.on(X)); //-pi..+pi
    return round(radsToDegs(rads));
  }
  
  private double round(double val) {
    return Util.round(val, 6);
  }
}
