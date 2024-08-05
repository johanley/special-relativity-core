package sr.explore.noncolinear.velocitytransform;

import static sr.core.Axis.X;

import sr.core.Util;
import sr.core.VelocityTransformation;
import sr.core.vector.Velocity;
import sr.output.text.TextOutput;

/** 
 The velocity transformation formula never yields a speed equal to or greater than the speed limit.
*/
public final class NeverExceedsSpeedLimit extends TextOutput {
  
  public static void main(String[] args) {
    NeverExceedsSpeedLimit velocity = new NeverExceedsSpeedLimit();
    velocity.explore();
  }

  void explore() {
    lines.add("Velocity transformation formula.");
    neverExceedsTheSpeedLimit(Velocity.of(X, 0.99), Velocity.of(X, 0.99));
    outputToConsoleAnd("never-exceeds-speed-limit.txt");
  }

  private void neverExceedsTheSpeedLimit(Velocity boost, Velocity v) {
    lines.add(Util.NL + "Velocity addition never exceeds the speed limit."+ Util.NL);
    lines.add("Boost: " + boost + " Velocity v':" + v);
    Velocity sum = VelocityTransformation.unprimedVelocity(boost, v);
    lines.add("Resultant v: " + sum);
  }
}
