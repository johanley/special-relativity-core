package sr.explore.clocks;

import static sr.core.Axis.X;

import sr.core.Util;
import sr.core.component.Position;
import sr.core.hist.timelike.TimelikeHistory;
import sr.core.hist.timelike.UniformVelocity;
import sr.core.vec3.Velocity;
import sr.explore.Exploration;
import sr.output.text.TextOutput;

/**
 A boost doesn't always <em>decrease</em> the rate of a clock with respect to a frame.
 (It's easy to forget this.) 
 
 <P>This is simply because a boost can either <em>increase or decrease</em> the relative speed of the clock.
 
 <P>In terms of space-time geometry, a boost can cause an object's history to either bend <em>more</em> or <em>less</em> towards the ct-axis, 
 depending on the relative geometry between the initial motion and the direction of the boost.
 
 <P>For the exact same reason, a similar remark can be made for flattening (Lorentz-Fitgerald contraction): a boost can both 
 flatten or unflatten the geometry of an object relative to a frame.
*/
public final class MakeAClockRunFaster extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    Exploration faster = new MakeAClockRunFaster();
    faster.explore();
  }
  
  @Override public void explore() {
    double speed1 = 0.9;
    double speed2 = 0.5;
    add("A clock's rate relative to a frame can also DECREASE because of a boost.");
    add("This happens simply because a boost can decrease the relative speed of the clock." + Util.NL);
    showClockRate(0.9);
    add(Util.NL + "If a boost of " + (speed1 - speed2) + " is AWAY FROM the approaching clock,");
    add("then the relative speed decreases, and the rate of the clock increases." + Util.NL);
    showClockRate(0.5);
    outputToConsoleAnd("make-a-clock-run-faster.txt");
  }
  
  private void showClockRate(double β){
    TimelikeHistory clock = UniformVelocity.of(Position.of(X, -100.0), Velocity.of(β, X));
    double clockRate = clock.τ(1.0);
    add("Clock approaching with β: " + round(β) + Util.NL + "Clock rate relative to the frame: " + round(clockRate));
  }
  
  private double round(double value) {
    return Util.round(value, 6);
  }
}
