package sr.explore.velocitytransform;

import static sr.core.Axis.X;
import static sr.core.Axis.Y;

import sr.core.VelocityTransformation;
import sr.core.Util;
import sr.core.vec3.NVelocity;
import sr.explore.Exploration;
import sr.output.text.TextOutput;

/** 
 The cases in which the velocity transformation formula commutes and doesn't commute.
*/
public final class Commutation extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    Commutation velocity = new Commutation();
    velocity.explore();
  }

  @Override public void explore() {
    add("Velocity transformation formula, and whether or not it commutes.");
    add("There are two variants of the formula, both of which must be checked: the formula for v', and the formula for v.");
    
    parallelVelocities(NVelocity.of(0.5, X), NVelocity.of(0.7, X));
    nonParallelVelocities();
    
    add(Util.NL + "Right-hand rule: rotate the first resultant-v toward the second, by the above angle.");
    add("The pole of the rotation is parallel to the cross-product (first x second).");
    add("The angle is the same size as the kinematic (Thomas-Wigner) rotation.");
    
    outputToConsoleAnd("commutation.txt");
  }

  private void nonParallelVelocities() {
    add(Util.NL + "When boost-velocity and object-velocity are at a right angle, the velocity formula for v does NOT commute.");
    showWithUnprimed(NVelocity.of(0.5, X), NVelocity.of(0.7, Y));

    add(Util.NL + "When boost-velocity and object-velocity are at any old angle, the velocity formula for v does NOT commute.");
    showWithUnprimed(NVelocity.of(0.5, X), NVelocity.of(0.1, 0.2, 0.8));
    
    add(Util.NL + "When boost-velocity and object-velocity are at a right angle, the velocity formula for v' does NOT commute.");
    showWithPrimed(NVelocity.of(0.5, X), NVelocity.of(0.7, Y));

    add(Util.NL + "When boost-velocity and object-velocity are at any old angle, the velocity formula for v' does NOT commute.");
    showWithPrimed(NVelocity.of(0.5, X), NVelocity.of(0.1, 0.2, 0.8));
  }

  private void parallelVelocities(NVelocity boost, NVelocity v) {
    add(Util.NL + "When boost-velocity and object-velocity are on the same line, the velocity formula for v commutes.");
    showWithUnprimed(boost, v);
    add(Util.NL + "When boost-velocity and object-velocity are on the same line, the velocity formula for v' does NOT commute (sign reversal).");
    showWithPrimed(boost, v);
  }
  
  private void showWithUnprimed(NVelocity boost, NVelocity v) {
    add("Boost: " + boost + " Velocity v':" + v);
    NVelocity sum1 = VelocityTransformation.unprimedVelocity(boost, v);
    add("Order (boost,v) resultant-v:" + sum1 + " mag:" + mag(sum1));
    
    NVelocity sum2 = VelocityTransformation.unprimedVelocity(v, boost);
    add("Order (v,boost) resultant-v:" + sum2 + " mag:" + mag(sum2));
    
    add("Angle between the two results:" + round(Util.radsToDegs(sum2.angle(sum1))) +"°");
  }
  
  private void showWithPrimed(NVelocity boost, NVelocity v) {
    add("Boost: " + boost + " Velocity v:" + v);
    NVelocity sum1 = VelocityTransformation.primedVelocity(boost, v);
    add("Order (boost,v) resultant-v':" + sum1 + " mag:" + mag(sum1));
    
    NVelocity sum2 = VelocityTransformation.primedVelocity(v, boost);
    add("Order (v,boost) resultant-v':" + sum2 + " mag:" + mag(sum2));
    
    add("Angle between the two results:" + round(Util.radsToDegs(sum2.angle(sum1))) +"°");
  }
  
  private double mag(NVelocity v) {
    return round(v.magnitude());
  }
  
  private double round(double value) {
    return Util.round(value, 5);
  }
}
