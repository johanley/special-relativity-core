package sr.explore.dogleg;

import java.util.ArrayList;
import java.util.List;

import sr.core.Axis;
import static sr.core.Axis.*;
import sr.core.Physics;
import sr.core.Util;
import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.Boost;
import sr.core.transform.FourVector;

/**
 Behaviour of multiple boosts, applied one after the other.
 
 <P>The behaviour differs according to whether or not the boosts are in the same line (collinear).
 
 <P>Same line: two boosts in sequence along an axis are equivalent to a third boost along the same axis.
 The boosts commute.
 The velocity for the equivalent boost can be found from the transformation of velocity 
 {@link Physics#transformVelocityColinear(double, double)}. 

 <P>Not same line (dogleg): they don't commute.
 The dogleg boost is equivalent to a boost plus a rotation (Thomas-Wigner rotation).
*/
public final class DoglegBoostsDontCommute {
  
  public static void main(String... args) {
    DoglegBoostsDontCommute test = new DoglegBoostsDontCommute();
    test.twoConsecutiveBoosts();
  }
  
  /** Test consecutive boosts, both in the same line, and not in the same line. */
  void twoConsecutiveBoosts() {
    double β1 = -0.6;
    double β2 = 0.8;
    Axis axis = X;
    List<String> lines = new ArrayList<>();

    title("Boosts commute IF AND ONLY IF they are in the same line ", lines);
    seeIfOrderMatters(axis, β1, axis, β2, lines);

    double βequiv = Physics.transformVelocityColinear(β1, β2);
    FourVector event = anyOldEvent();
    FourVector afterEquiv = boostThe(event, axis, βequiv);
    lines.add("Equivalent boost: " + βequiv + " " + afterEquiv);
    
    
    title("Boosts don't commute if they aren't in the same line ", lines);
    seeIfOrderMatters(X, β1, Y, β2, lines);
    
    title("Boosts with the same speed, but perpendicular, still don't commute!", lines);
    seeIfOrderMatters(X, β1, Y, β1, lines);
    
    Util.writeToFile(DoglegBoostsDontCommute.class, "dogleg-boosts.txt", lines);
  }
  
  private FourVector boostThe(FourVector event, Axis axis, double β) {
    return Boost.alongThe(axis, β).toNewFourVector(event);
  }
  
  private void seeIfOrderMatters(Axis axis1, double β1, Axis axis2, double β2, List<String> lines) {
    FourVector event = anyOldEvent();
    inThisOrder(event, axis1, β1, axis2, β2, lines);
    inThisOrder(event, axis2, β2, axis1, β1, lines);
  }
  
  private void inThisOrder(FourVector event, Axis axis1, double β1, Axis axis2, double β2, List<String> lines) {
    lines.add("Event: " + event);
    FourVector firstBoost = boostThe(event, axis1, β1);
    FourVector secondBoost = boostThe(firstBoost, axis2, β2);
    lines.add("Boost 1: " + axis1 + " "+ β1 + " " + firstBoost);
    lines.add("Boost 2: " + axis2 + " "+ β2 + " " + secondBoost);
  }
  
  private FourVector anyOldEvent() {
    FourVector event = FourVector.from(2.32, -15.79, 0.0, 0.0, ApplyDisplaceOp.YES);
    return event;
  }
  
  private void title(String text, List<String> lines) {
    lines.add(" ");
    lines.add("# " + text);
  }
}