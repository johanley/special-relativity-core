package sr.explore.noncolinear.velocitytransform;

import static sr.core.Axis.X;
import static sr.core.Axis.Y;

import sr.core.Util;
import sr.core.VelocityTransformation;
import sr.core.vector3.Velocity;
import sr.output.text.TextOutput;

/** 
 The cases in which the velocity transformation formula commutes and doesn't commute.
*/
public final class Commutation extends TextOutput {
  
  public static void main(String[] args) {
    Commutation velocity = new Commutation();
    velocity.explore();
  }

  void explore() {
    lines.add("Velocity transformation formula, and whether or not it commutes.");
    lines.add("There are two variants of the formula, both of which must be checked: the formula for v', and the formula for v.");
    
    parallelVelocities(Velocity.of(X, 0.5), Velocity.of(X, 0.7));
    nonParallelVelocities();
    
    lines.add(Util.NL + "Right-hand rule: rotate the first resultant-v toward the second, by the above angle.");
    lines.add("The pole of the rotation is parallel to the cross-product (first x second).");
    lines.add("The angle is the same size as the Silberstein (Thomas-Wigner) rotation.");
    
    outputToConsoleAnd("commutation.txt");
  }

  private void nonParallelVelocities() {
    lines.add(Util.NL + "When boost-velocity and object-velocity are at a right angle, the velocity formula for v does NOT commute.");
    showWithUnprimed(Velocity.of(X, 0.5), Velocity.of(Y, 0.7));

    lines.add(Util.NL + "When boost-velocity and object-velocity are at any old angle, the velocity formula for v does NOT commute.");
    showWithUnprimed(Velocity.of(X, 0.5), Velocity.of(0.1, 0.2, 0.8));
    
    lines.add(Util.NL + "When boost-velocity and object-velocity are at a right angle, the velocity formula for v' does NOT commute.");
    showWithPrimed(Velocity.of(X, 0.5), Velocity.of(Y, 0.7));

    lines.add(Util.NL + "When boost-velocity and object-velocity are at any old angle, the velocity formula for v' does NOT commute.");
    showWithPrimed(Velocity.of(X, 0.5), Velocity.of(0.1, 0.2, 0.8));
  }

  private void parallelVelocities(Velocity boost, Velocity v) {
    lines.add(Util.NL + "When boost-velocity and object-velocity are on the same line, the velocity formula for v commutes.");
    showWithUnprimed(boost, v);
    lines.add(Util.NL + "When boost-velocity and object-velocity are on the same line, the velocity formula for v' does NOT commute (sign reversal).");
    showWithPrimed(boost, v);
  }
  
  private void showWithUnprimed(Velocity boost, Velocity v) {
    lines.add("Boost: " + boost + " Velocity v':" + v);
    Velocity sum1 = VelocityTransformation.unprimedVelocity(boost, v);
    lines.add("Order (boost,v) resultant-v:" + sum1 + " mag:" + mag(sum1));
    
    Velocity sum2 = VelocityTransformation.unprimedVelocity(v, boost);
    lines.add("Order (v,boost) resultant-v:" + sum2 + " mag:" + mag(sum2));
    
    lines.add("Angle between the two results:" + round(Util.radsToDegs(sum2.angle(sum1))) +"°");
  }
  
  private void showWithPrimed(Velocity boost, Velocity v) {
    lines.add("Boost: " + boost + " Velocity v:" + v);
    Velocity sum1 = VelocityTransformation.primedVelocity(boost, v);
    lines.add("Order (boost,v) resultant-v':" + sum1 + " mag:" + mag(sum1));
    
    Velocity sum2 = VelocityTransformation.primedVelocity(v, boost);
    lines.add("Order (v,boost) resultant-v':" + sum2 + " mag:" + mag(sum2));
    
    lines.add("Angle between the two results:" + round(Util.radsToDegs(sum2.angle(sum1))) +"°");
  }
  
  private double mag(Velocity v) {
    return round(v.magnitude());
  }
  
  private double round(double value) {
    return Util.round(value, 5);
  }
}
