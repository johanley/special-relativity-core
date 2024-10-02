package sr.explore.accel.elbow.boost;

import static sr.core.Axis.X;

import sr.core.Axis;
import sr.core.NVelocityTransformation;
import sr.core.Util;
import sr.core.component.NEvent;
import sr.core.component.ops.NSense;
import sr.core.vec3.NDirection;
import sr.core.vec3.NVelocity;
import sr.explore.Exploration;
import sr.output.text.TextOutput;

/**
 Behaviour of multiple boosts, applied one after the other.
 
 <P>The behaviour differs according to whether or not the boosts are in the same line (collinear).
 
 <P>Same line: two boosts in sequence along an axis are equivalent to a third boost along the same axis.
 The two boosts commute.

 <P>Not the same line (an elbow-boost): they two boosts don't commute.
 An elbow-boost is equivalent to a boost plus a rotation, here called a kinematic rotation (Wigner rotation).
*/
public final class CornerBoostsDontCommute extends TextOutput implements Exploration {
  
  public static void main(String... args) {
    CornerBoostsDontCommute test = new CornerBoostsDontCommute();
    test.explore();
  }
  
  /** Test consecutive boosts, both in the same line, and not in the same line. */
  @Override public void explore() {
    double β1 = 0.6; //used to be minus
    double β2 = 0.8;
    NVelocity β1_minus_x = NVelocity.of(β1, NDirection.of(-1,0,0));
    NVelocity β2_plus_x = NVelocity.of(β2, NDirection.of(+1,0,0));
    NVelocity β2_plus_y = NVelocity.of(β2, NDirection.of(0,+1,0));
    NVelocity β1_minus_y = NVelocity.of(β1, NDirection.of(0,-1,0));

    add("1. Boosts commute if (and only if) they're in the same line (collinear).");
    add(dashes(70));
    add("Let's boost an event twice along the X-axis.");
    seeIfOrderMatters(β1_minus_x, β2_plus_x);
    add(Util.NL + "The two boosts give the same final event coordinates, regardless of the order of execution.");

    double βequiv = βequivalentColinear(β1_minus_x, β2_plus_x);
    NEvent event = anyOldEvent();
    NEvent afterEquiv = boostThe(event, NVelocity.of(βequiv, X));
    add("A single equivalent boost: " + X + " " + Util.round(βequiv, 5) + " " + afterEquiv);
    
    add(Util.NL + "2. Boosts don't commute if they aren't in the same line (non-collinear).");
    add(dashes(70));
    add("Let's boost an event first along the X-axis, then along the Y-axis.");
    seeIfOrderMatters(β1_minus_x, β2_plus_y);
    add(Util.NL + "The two non-collinear boosts give different final event coordinates, according to their order of execution.");
    
    add(Util.NL + "3. As a second example, boosts with the same speed, but in perpendicular directions, also don't commute.");
    add(dashes(100));
    add("Let's boost an event first along the X-axis, then along the Y-axis, and using the same boost speed.");
    seeIfOrderMatters(β1_minus_x, β1_minus_y);
    
    outputToConsoleAnd("corner-boosts-dont-commute.txt");
  }
  
  private NEvent boostThe(NEvent event, NVelocity v) {
    return event.boost(v, NSense.ChangeComponents);
  }

  private double βequivalentColinear(NVelocity v_a, NVelocity v_b) {
    NVelocity result = NVelocityTransformation.unprimedVelocity(v_a, v_b);
    return result.magnitude();
  }
  
  private void seeIfOrderMatters(NVelocity v1, NVelocity v2) {
    NEvent event = anyOldEvent();
    inThisOrder(event, v1, v2);
    add("Now reverse the order of the boosts, for the same event.");
    inThisOrder(event, v2, v1);
  }
  
  private void inThisOrder(NEvent event, NVelocity v1, NVelocity v2) {
    add("Event: " + event);
    
    Axis axis1 = v1.axis().get();
    NEvent firstBoost = boostThe(event, v1);
    
    Axis axis2 = v2.axis().get();
    NEvent secondBoost = boostThe(firstBoost, v2);
    
    add(" Boost 1: " + axis1 + " "+ v1.on(axis1) + " gives " + firstBoost);
    add(" Boost 2: " + axis2 + " "+ v2.on(axis2) + " gives " + secondBoost);
  }
  
  private NEvent anyOldEvent() {
    return NEvent.of(2.32, -15.79, 0.0, 0.0);
  }
}