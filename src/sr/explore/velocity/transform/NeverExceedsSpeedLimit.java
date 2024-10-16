package sr.explore.velocity.transform;

import static sr.core.Axis.X;

import sr.core.VelocityTransformation;
import sr.core.Util;
import sr.core.vec3.Velocity;
import sr.explore.Exploration;
import sr.output.text.TextOutput;

/** 
 The velocity transformation formula never yields a speed equal to or greater than the speed limit.
*/
public final class NeverExceedsSpeedLimit extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    Exploration velocity = new NeverExceedsSpeedLimit();
    velocity.explore();
  }

  @Override public void explore() {
    add("Velocity transformation formula.");
    neverExceedsTheSpeedLimit(Velocity.of(0.99, X), Velocity.of(0.99, X));
    outputToConsoleAnd("never-exceeds-speed-limit.txt");
  }

  private void neverExceedsTheSpeedLimit(Velocity boost, Velocity v) {
    add(Util.NL + "Velocity addition never exceeds the speed limit."+ Util.NL);
    add("Boost: " + boost + " Velocity v':" + v);
    Velocity sum = VelocityTransformation.unprimedVelocity(boost, v);
    add("Resultant v: " + sum);
  }
}
