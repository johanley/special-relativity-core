package sr.explore.optics.kvector;

import static sr.core.Axis.*;
import static sr.core.Util.NL;
import static sr.core.Util.radsToDegs;

import sr.core.Axis;
import sr.core.LorentzTransformation;
import sr.core.Util;
import sr.core.vector3.Direction;
import sr.core.vector3.Velocity;
import sr.core.vector4.FourVector;
import sr.core.vector4.WaveVector;
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
    add("A boost parallel to the wave-vector k changes the frequency only.");
    Axis axis = Axis.X;
    Velocity v = Velocity.of(axis, 0.6);
    sameLine(axis, v);
    
    add(NL + "A boost anti-parallel to the wave-vector k changes the frequency only.");
    sameLine(axis, Velocity.of(v.times(-1)));
  }

  private void sameLine(Axis axis, Velocity v) {
    LorentzTransformation lt = LorentzTransformation.of(v);
    WaveVector k_in = WaveVector.of(1.0, axis);
    WaveVector k_out = lt.primedVector(k_in);
    show(lt, k_in, k_out);
  }

  private void notTheSameLineChangesFrequencyAndDirection() {
    add(NL + "A random boost not parallel to the wave-vector k changes both the frequency and the direction.");
    Velocity v = Velocity.of(Axis.X, 0.6);
    LorentzTransformation lt = LorentzTransformation.of(v);
    WaveVector k_in = WaveVector.of(1.0, Direction.of(1, 2, 3));
    WaveVector k_out = lt.primedVector(k_in);
    show(lt, k_in, k_out);
  }
  
  private void abberation() {
    add(NL+"Abberation.");
    add("Input k in frame K is directed to the 4th quadrant, at 45 degrees down from the +X-axis.");
    Velocity v = Velocity.of(Axis.X, -0.60);
    LorentzTransformation lt = LorentzTransformation.of(v);
    //directed into the 4th quadrant at 45 degrees
    WaveVector k_in = WaveVector.of(1.0, Direction.of(+1, -1, 0));
    WaveVector k_out = lt.primedVector(k_in);
    show(lt, k_in, k_out);
    showChangeInDirection(k_in, k_out);
  }
  
  private void show(LorentzTransformation lt, FourVector input, FourVector output) {
    add("  Boost "  + lt);
    add("  Input k in frame K "  + input + " mag " + round(input.square()));
    add("  Output k in frame K'" + output + " mag " + round(output.square()));
  }
  
  private void showChangeInDirection(WaveVector k_in, FourVector k_out) {
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
