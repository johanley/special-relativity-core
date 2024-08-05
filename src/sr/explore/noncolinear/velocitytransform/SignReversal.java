package sr.explore.noncolinear.velocitytransform;

import static sr.core.Axis.*;

import sr.core.Util;
import sr.core.VelocityTransformation;
import sr.core.vector.Velocity;
import sr.output.text.TextOutput;

/** Colinear case, and when the sign of the velocity changes. */
public final class SignReversal extends TextOutput {
  
  public static void main(String[] args) {
    SignReversal velocity = new SignReversal();
    velocity.explore();
  }

  void explore() {
    lines.add("Velocity reverses sign only in the colinear case, and only with one variant of the formula." + Util.NL);
    show(Velocity.of(X, 0.5), Velocity.of(X, 0.7));
    outputToConsoleAnd("sign-reversal.txt");
  }

  private void show(Velocity boost, Velocity v) {
    lines.add("Boost from K to K': " + boost + " Velocity:" + v + " are in the same line." + Util.NL);
    lines.add("Compute primed v' given (boost + v). Treat the given Velocity as v.");
    Velocity sum1 = VelocityTransformation.primedVelocity(boost, v);
    lines.add("Order (boost,v) resultant-v':" + sum1 + " mag:" + mag(sum1));
    
    Velocity sum2 = VelocityTransformation.primedVelocity(v, boost);
    lines.add("Order (v,boost) resultant-v':" + emit(sum2));
    angleBetween(sum1, sum2);
    
    
    lines.add(Util.NL + "Compute unprimed v given (boost + v'). Treat the given Velocity as v'.");
    Velocity sum3 = VelocityTransformation.unprimedVelocity(boost, v);
    lines.add("Order (boost,v') resultant-v:" + emit(sum3));
    
    Velocity sum4 = VelocityTransformation.unprimedVelocity(v, boost);
    lines.add("Order (v',boost) resultant-v:" + emit(sum4));
    angleBetween(sum3, sum4);

  }

  private void angleBetween(Velocity sum1, Velocity sum2) {
    lines.add("Angle between the two results:" + round(Util.radsToDegs(sum2.angle(sum1))) +"°");
  }
  
  private String emit(Velocity sum) {
    return sum + " mag:" + mag(sum);
  }
  
  private double mag(Velocity v) {
    return round(v.magnitude());
  }
  
  private double round(double value) {
    return Util.round(value, 5);
  }
}
