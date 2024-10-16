package sr.explore.velocity.transform;

import static sr.core.Axis.X;

import sr.core.VelocityTransformation;
import sr.core.Util;
import sr.core.vec3.Velocity;
import sr.explore.Exploration;
import sr.output.text.TextOutput;

/** Boost and velocity in the same directions. */
public final class SignReversal extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    Exploration velocity = new SignReversal();
    velocity.explore();
  }

  @Override public void explore() {
    add("Velocity reverses sign only in the collinear case, and only with one variant of the formula." + Util.NL);
    show(Velocity.of(0.5, X), Velocity.of(0.7, X));
    outputToConsoleAnd("sign-reversal.txt");
  }

  private void show(Velocity boost, Velocity v) {
    add("Boost from K to K': " + boost + " Velocity:" + v + " are in the same line." + Util.NL);
    add("Compute primed v' given (boost + v). Treat the given Velocity as v.");
    Velocity sum1 = VelocityTransformation.primedVelocity(boost, v);
    add("Order (boost,v) resultant-v':" + sum1 + " mag:" + mag(sum1));
    
    Velocity sum2 = VelocityTransformation.primedVelocity(v, boost);
    add("Order (v,boost) resultant-v':" + emit(sum2));
    angleBetween(sum1, sum2);
    
    
    add(Util.NL + "Compute unprimed v given (boost + v'). Treat the given Velocity as v'.");
    Velocity sum3 = VelocityTransformation.unprimedVelocity(boost, v);
    add("Order (boost,v') resultant-v:" + emit(sum3));
    
    Velocity sum4 = VelocityTransformation.unprimedVelocity(v, boost);
    add("Order (v',boost) resultant-v:" + emit(sum4));
    angleBetween(sum3, sum4);

  }

  private void angleBetween(Velocity sum1, Velocity sum2) {
    add("Angle between the two results:" + round(Util.radsToDegs(sum2.angle(sum1))) +"°");
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
