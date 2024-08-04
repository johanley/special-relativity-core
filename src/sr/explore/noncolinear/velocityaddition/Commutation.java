package sr.explore.noncolinear.velocityaddition;

import static sr.core.Axis.X;
import static sr.core.Axis.Y;

import sr.core.Util;
import sr.core.VelocityAddition;
import sr.core.vector.Velocity;
import sr.output.text.TextOutput;

/** 
  Velocity addition commutes only if all velocities are in the same line.
  When you reverse the order of velocities, the result has the same size but a different direction.
*/
public final class Commutation extends TextOutput {
  
  
  public static void main(String[] args) {
    Commutation velocity = new Commutation();
    velocity.explore();
  }

  void explore() {
    lines.add("Velocity addition formula.");
    
    neverExceedsTheSpeedLimit(Velocity.of(X, 0.99), Velocity.of(X, 0.99));
    
    parallelVelocitiesCommute(Velocity.of(X, 0.5), Velocity.of(X, 0.7));
    
    lines.add(Util.NL + "When boost-velocity and object-velocity are at a right angle, the velocity addition formula does NOT commute.");
    nonParallelVelocitiesDontCommute(Velocity.of(X, 0.5), Velocity.of(Y, 0.7));

    lines.add(Util.NL + "When boost-velocity and object-velocity are at any old angle, the velocity addition formula does NOT commute.");
    nonParallelVelocitiesDontCommute(Velocity.of(X, 0.5), Velocity.of(0.1, 0.2, 0.8));
    lines.add("Right-hand rule: rotate the first resultant-v' toward the second, by the above angle.");
    lines.add("The pole of the rotation is parallel to the cross-product (first x second).");
    lines.add("The angle is the same size as the Silberstein (Thomas-Wigner) rotation.");
    
    outputToConsoleAnd("commutation.txt");
  }

  private void neverExceedsTheSpeedLimit(Velocity boost, Velocity v) {
    lines.add(Util.NL + "Velocity addition never exceeds the speed limit.");
    lines.add("Boost: " + boost + " Velocity:" + v);
    Velocity sum = VelocityAddition.primedVelocity(boost, v);
    lines.add("Resultant v': " + sum);
  }
  
  private void parallelVelocitiesCommute(Velocity boost, Velocity v) {
    lines.add(Util.NL + "When boost-velocity and object-velocity are on the same line, the velocity addition formula commutes.");
    show(boost, v);
  }
  
  private void nonParallelVelocitiesDontCommute(Velocity boost, Velocity v) {
    show(boost, v);
  }

  private void show(Velocity boost, Velocity v) {
    lines.add("Boost: " + boost + " Velocity:" + v);
    Velocity sum1 = VelocityAddition.primedVelocity(boost, v);
    lines.add("Order (boost,v) resultant-v':" + sum1 + " mag:" + mag(sum1));
    
    Velocity sum2 = VelocityAddition.primedVelocity(v, boost);
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
