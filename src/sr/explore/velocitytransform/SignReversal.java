package sr.explore.velocitytransform;

import static sr.core.Axis.X;

import sr.core.VelocityTransformation;
import sr.core.Util;
import sr.core.vec3.NVelocity;
import sr.explore.Exploration;
import sr.output.text.TextOutput;

/** Boost and velocity in the same directions. */
public final class SignReversal extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    SignReversal velocity = new SignReversal();
    velocity.explore();
  }

  @Override public void explore() {
    add("Velocity reverses sign only in the collinear case, and only with one variant of the formula." + Util.NL);
    show(NVelocity.of(0.5, X), NVelocity.of(0.7, X));
    outputToConsoleAnd("sign-reversal.txt");
  }

  private void show(NVelocity boost, NVelocity v) {
    add("Boost from K to K': " + boost + " Velocity:" + v + " are in the same line." + Util.NL);
    add("Compute primed v' given (boost + v). Treat the given Velocity as v.");
    NVelocity sum1 = VelocityTransformation.primedVelocity(boost, v);
    add("Order (boost,v) resultant-v':" + sum1 + " mag:" + mag(sum1));
    
    NVelocity sum2 = VelocityTransformation.primedVelocity(v, boost);
    add("Order (v,boost) resultant-v':" + emit(sum2));
    angleBetween(sum1, sum2);
    
    
    add(Util.NL + "Compute unprimed v given (boost + v'). Treat the given Velocity as v'.");
    NVelocity sum3 = VelocityTransformation.unprimedVelocity(boost, v);
    add("Order (boost,v') resultant-v:" + emit(sum3));
    
    NVelocity sum4 = VelocityTransformation.unprimedVelocity(v, boost);
    add("Order (v',boost) resultant-v:" + emit(sum4));
    angleBetween(sum3, sum4);

  }

  private void angleBetween(NVelocity sum1, NVelocity sum2) {
    add("Angle between the two results:" + round(Util.radsToDegs(sum2.angle(sum1))) +"°");
  }
  
  private String emit(NVelocity sum) {
    return sum + " mag:" + mag(sum);
  }
  
  private double mag(NVelocity v) {
    return round(v.magnitude());
  }
  
  private double round(double value) {
    return Util.round(value, 5);
  }
}
