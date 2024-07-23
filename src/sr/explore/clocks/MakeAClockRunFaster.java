package sr.explore.clocks;

import static sr.core.Axis.X;

import sr.core.Util;
import sr.core.history.History;
import sr.core.history.UniformVelocity;
import sr.core.vector.Position;
import sr.core.vector.Velocity;
import sr.output.text.TextOutput;

/**
 A boost doesn't always <em>decrease</em> the rate of a clock with respect to a frame.
 (It's easy to forget this.) 
 
 <P>This is simply because a boost can either <em>increase or decrease</em> the relative speed of the clock.
 
 <P>In terms of space-time geometry, a boost can cause an object's history to either bend <em>more</em> or <em>less</em> towards the ct-axis, 
 depending on the relative geometry between the initial motion and the direction of the boost.
*/
public final class MakeAClockRunFaster extends TextOutput {
  
  public static void main(String[] args) {
    MakeAClockRunFaster faster = new MakeAClockRunFaster();
    faster.explore();
  }
  
  void explore() {
    double speed1 = 0.9;
    double speed2 = 0.5;
    lines.add("A clock's rate relative to a frame can also DECREASE because of a boost.");
    lines.add("This happens simply because a boost can decrease the relative speed of the clock." + Util.NL);
    showClockRate(0.9);
    lines.add(Util.NL + "If a boost of " + (speed1 - speed2) + " is AWAY FROM the approaching clock, then the relative speed decreases:" + Util.NL);
    showClockRate(0.5);
    outputToConsoleAnd("make-a-clock-run-faster.txt");
  }
  
  private void showClockRate(double β){
    History clock = UniformVelocity.of(Position.of(X, -100.0), Velocity.of(X, β));
    double clockRate = clock.τ(1.0);
    lines.add("Clock approaching with β: " + round(β) + Util.NL + "Clock rate relative to the frame: " + round(clockRate));
  }
  
  private double round(double value) {
    return Util.round(value, 6);
  }
}
