package sr.explore.noncolinear.boost.corner;

import static sr.core.Axis.X;
import static sr.core.Axis.Y;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.Util;
import sr.core.VelocityTransformation;
import sr.core.vector3.Velocity;
import sr.core.vector4.Event;
import sr.core.vector4.transform.Boost;
import sr.output.text.TextOutput;

/**
 Behaviour of multiple boosts, applied one after the other.
 
 <P>The behaviour differs according to whether or not the boosts are in the same line (collinear).
 
 <P>Same line: two boosts in sequence along an axis are equivalent to a third boost along the same axis.
 The two boosts commute.

 <P>Not the same line (corner-boost): they two boosts don't commute.
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

    lines.add("1. Boosts commute if (and only if) they're in the same line (collinear).");
    lines.add(dashes(70));
    lines.add("Let's boost an event twice along the X-axis.");
    seeIfOrderMatters(Velocity.of(X, β1), Velocity.of(X, β2));
    lines.add(Util.NL + "The two boosts give the same final event coordinates, regardless of the order of execution.");

    double βequiv = βequivalentColinear(β1, β2);
    Event event = anyOldEvent();
    Event afterEquiv = boostThe(event, X, βequiv);
    lines.add("A single equivalent boost: " + X + " " + Util.round(βequiv, 5) + " " + afterEquiv);
    
    lines.add(Util.NL + "2. Boosts don't commute if they aren't in the same line (non-collinear).");
    lines.add(dashes(70));
    lines.add("Let's boost an event first along the X-axis, then along the Y-axis.");
    seeIfOrderMatters(Velocity.of(X, β1), Velocity.of(Y, β2));
    lines.add(Util.NL + "The two non-collinear boosts give different final event coordinates, according to their order of execution.");
    
    lines.add(Util.NL + "3. As a second example, boosts with the same speed, but in perpendicular directions, also don't commute.");
    lines.add(dashes(100));
    lines.add("Let's boost an event first along the X-axis, then along the Y-axis, and using the same boost speed.");
    seeIfOrderMatters(Velocity.of(X, β1), Velocity.of(Y, β1));
    
    outputToConsoleAnd("corner-boosts-dont-commute.txt");
  }
  
  private Event boostThe(Event event, Axis axis, double β) {
    return Boost.of(axis, β).changeVector(event);
  }

  private double βequivalentColinear(double β1, double β2) {
    Velocity result = VelocityTransformation.unprimedVelocity(Velocity.of(X, β1), Velocity.of(X, β2));
    return result.magnitude();
  }
  
  private void seeIfOrderMatters(Velocity v1, Velocity v2) {
    Event event = anyOldEvent();
    inThisOrder(event, v1, v2);
    lines.add("Now reverse the order of the boosts, for the same event.");
    inThisOrder(event, v2, v1);
  }
  
  private void inThisOrder(Event event, Velocity v1, Velocity v2) {
    lines.add("Event: " + event);
    
    Axis axis1 = v1.axis().get();
    Event firstBoost = boostThe(event, axis1, v1.on(axis1));
    
    Axis axis2 = v2.axis().get();
    Event secondBoost = boostThe(firstBoost, axis2, v2.on(axis2));
    
    lines.add(" Boost 1: " + axis1 + " "+ v1.on(axis1) + " gives " + firstBoost);
    lines.add(" Boost 2: " + axis2 + " "+ v2.on(axis2) + " gives " + secondBoost);
  }
  
  private Event anyOldEvent() {
    return Event.of(2.32, -15.79, 0.0, 0.0);
  }
}