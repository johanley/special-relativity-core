package sr.explore.cornerboost;

import static sr.core.Axis.X;
import static sr.core.Axis.Y;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.Util;
import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.Boost;
import sr.core.transform.FourVector;
import sr.output.text.TextOutput;

/**
 Behaviour of multiple boosts, applied one after the other.
 
 <P>The behaviour differs according to whether or not the boosts are in the same line (collinear).
 
 <P>Same line: two boosts in sequence along an axis are equivalent to a third boost along the same axis.
 The boosts commute.
 The velocity for the equivalent boost can be found from the transformation of velocity 
 {@link Physics#transformVelocityColinear(double, double)}. 

 <P>Not same line (corner-boost): they don't commute.
 A corner-boost is equivalent to a boost plus a rotation, called a Silberstein (or Thomas-Wigner) rotation.
*/
public final class CornerBoostsDontCommute extends TextOutput {
  
  public static void main(String... args) {
    CornerBoostsDontCommute test = new CornerBoostsDontCommute();
    test.twoConsecutiveBoosts();
  }
  
  /** Test consecutive boosts, both in the same line, and not in the same line. */
  void twoConsecutiveBoosts() {
    double β1 = -0.6;
    double β2 = 0.8;
    Axis axis = X;

    lines.add("1. Boosts commute if (and only if) they're in the same line (collinear).");
    lines.add(dashes(70));
    lines.add("Let's boost an event twice along the X-axis.");
    seeIfOrderMatters(axis, β1, axis, β2);

    double βequiv = Physics.transformVelocityColinear(β1, β2);
    FourVector event = anyOldEvent();
    FourVector afterEquiv = boostThe(event, axis, βequiv);
    lines.add(Util.NL + "The two boosts give the same final event coordinates, regardless of the order of execution.");
    lines.add("A single equivalent boost: " + axis + " " + Util.round(βequiv, 5) + " " + afterEquiv);
    
    lines.add(Util.NL + "2. Boosts don't commute if they aren't in the same line (non-collinear).");
    lines.add(dashes(70));
    lines.add("Let's boost an event first along the X-axis, then along the Y-axis.");
    seeIfOrderMatters(X, β1, Y, β2);
    lines.add(Util.NL + "The two non-collinear boosts give different final event coordinates, according to their order of execution.");
    
    lines.add(Util.NL + "3. As a second example, boosts with the same speed, but in perpendicular directions, also don't commute.");
    lines.add(dashes(100));
    lines.add("Let's boost an event first along the X-axis, then along the Y-axis, and using the same boost speed.");
    seeIfOrderMatters(X, β1, Y, β1);
    
    outputToConsoleAnd("corner-boosts-dont-commute.txt");
  }
  
  private FourVector boostThe(FourVector event, Axis axis, double β) {
    return Boost.alongThe(axis, β).toNewFourVector(event);
  }
  
  private void seeIfOrderMatters(Axis axis1, double β1, Axis axis2, double β2) {
    FourVector event = anyOldEvent();
    inThisOrder(event, axis1, β1, axis2, β2);
    lines.add("Now reverse the order of the boosts, for the same event.");
    inThisOrder(event, axis2, β2, axis1, β1);
  }
  
  private void inThisOrder(FourVector event, Axis axis1, double β1, Axis axis2, double β2) {
    lines.add("Event: " + event);
    FourVector firstBoost = boostThe(event, axis1, β1);
    FourVector secondBoost = boostThe(firstBoost, axis2, β2);
    lines.add(" Boost 1: " + axis1 + " "+ β1 + " gives " + firstBoost);
    lines.add(" Boost 2: " + axis2 + " "+ β2 + " gives " + secondBoost);
  }
  
  private FourVector anyOldEvent() {
    FourVector event = FourVector.from(2.32, -15.79, 0.0, 0.0, ApplyDisplaceOp.YES);
    return event;
  }
}