package sr.explore.flattening;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import sr.core.Axis;
import sr.core.EventFinder;
import sr.core.Physics;
import sr.core.Util;
import sr.core.history.History;
import sr.core.history.StationaryParticle;
import sr.core.transform.Boost;
import sr.core.transform.CoordTransform;
import sr.core.transform.FourVector;

/** 
 Measure the effect of flattening on a stick, using a time-slice.
 As always, a time-slice is needed to see the geometrical properties of an object (length, orientation).
 
 <P>Place a stack at an angle with respect to the X-axis (not 0 or 90 degrees). 
 Do a boost along the X-axis.
 In the boosted frame, the angle of the stick with respect to the X-axis will change.
 The stick will "rotate away" from the direction of the boost. 
*/
public class StickFlattening {
  
  public static void main(String[] args) {
    StickFlattening stick = new StickFlattening();
    List<String> lines = new ArrayList<>();
    stick.stickAlongAxis(lines);
    stick.stickAngledToXAxis(lines);
    stick.stickAngledToXAxisWithEquivalentBoostParams(lines);
    
    Util.writeToFile(StickFlattening.class, "stick-flattening.txt", lines);
    for(String line : lines) {
      System.out.println(line);
    }
  }
  
  /**
   Measure the length contraction for a stick along the X-axis (the line of a boost).
   
   In K:
   <ul>
    <li>the stick is stationary
    <li>it's on the X-axis
    <li>it goes from the x=1 to x=2, and its length is 1.0
   </ul>
   
   In K', boosted at 0.6 with respect to K along the X axis:
   <ul>
    <li>measure the stick in K'
    <li>its length is now 1/1.25 = 0.8
   </ul>
  */
  void stickAlongAxis(List<String> lines) {
    lines.add("Stick along the X-axis.");
    //the stick is stationary in K
    //the stick is along the x-axis, from x=1 to x=2
    History histA = new StationaryParticle(1.0, 0.0, 0.0,   0.0, 1.0);
    History histB = new StationaryParticle(2.0, 0.0, 0.0,   0.0, 1.0);
    double τ = 0.0; //any proper time will do, since it's stationary in K
    FourVector bMinusA = histB.event(τ).minus(histA.event(τ));
    lines.add("K stick length:" + bMinusA.spatialMagnitude());
    
    //K': boost along the X axis
    double β = 0.6;
    CoordTransform boostX = Boost.alongThe(Axis.X, β);
    //find events that have the same ct value in K' (root finding)
    FourVector aBoosted = boostX.toNewFrame(histA.event(0.18));
    
    Function<FourVector, Double> criterion = event -> (boostX.toNewFrame(event).ct() - aBoosted.ct());
    EventFinder finder = new EventFinder(histB, criterion, 0.000001) ;
    double tau = finder.searchWithNewtonsMethod(0.0000001);
    lines.add("Zero: " + tau + " (" + finder.numIterations() + " iterations)");
    FourVector bBoosted = boostX.toNewFrame(histB.event(tau));
    
    lines.add("K' time-slice");
    lines.add("K' a: " + aBoosted);
    lines.add("K' b: " + bBoosted);
    lines.add("K' b-a: " + bBoosted.minus(aBoosted));
    lines.add("K' b-a len from LT: " + bBoosted.minus(aBoosted).spatialMagnitude());
    lines.add("1/gamma from formula: " + 1.0/Physics.Γ(β));
  }
  

  /**
   Measure the flattening of a stick that's at an angle to the X-axis (the line of a boost).
   
   In K:
   <ul>
    <li>the stick is stationary
    <li>it's on the XY-plane.
    <li>it's angled at 45 degrees to the X-axis. 
    <li>it goes from the origin to (x=1, y=1, z=0), and its length is sqrt(2) = 1.41421356.
   </ul>
   
   In K', boosted at 0.6 with respect to K along the X axis:
   <ul>
    <li>measure the stick in K'
    <li>its sides are now of length 1.0 (y) and 0.8(x), and its length is 1.2806248
    <li>the stick is 'squished' in the X-direction only; its angle w.r.t. the X axis increases from 45 deg to 51.34 deg
   </ul>
   
   Ref : Problem Book in Relativity and Gravitation (Lightman and others)
   Has this very problem (Problem 1.7). 
   Answer for the relation between the before after angles: cot θ = (1/Γ) cot θ'
   This agrees with my computation below.
   UNEXPECTED: the DIFFERENCE in the θ's equals the Thomas-Wigner rotation angle!
   Ref: https://users.physics.ox.ac.uk/~smithb/website/coursenotes/rel_A.pdf, section 5.7 
   mentions the same math in the context of Thomas rotation. 
  */
  void stickAngledToXAxis(List<String> lines) {
    lines.add(" ");
    lines.add("Stick angled to the X-axis.");
    //the stick is stationary in K, from the origin to x=1, y=1, z=0
    History histA = new StationaryParticle(0.0, 0.0, 0.0,   0.0, 1.0); //origin
    History histB = new StationaryParticle(1.0, 1.0, 0.0,   0.0, 1.0); //other end of the stick
    FourVector diff = histB.event(0.0).minus(histA.event(0.0));
    lines.add("K b-a:" + diff);
    lines.add("K stick length:" + diff.spatialMagnitude());
    double angle1 = Math.atan2(diff.y(), diff.x());
    lines.add("K stick angle wrt X-axis: " + Util.radsToDegs(angle1) + " deg");
    
    //K': boost along the X axis
    lines.add("K' time-slice");
    double β = 0.6;
    CoordTransform boostX = Boost.alongThe(Axis.X, β);
    
    //find events that have the same ct value in K'
    FourVector aBoosted = boostX.toNewFrame(histA.event(0.18));
    
    Function<FourVector, Double> criterion = event -> (boostX.toNewFrame(event).ct() - aBoosted.ct());
    EventFinder finder = new EventFinder(histB, criterion, 0.000001) ;
    double tau = finder.searchWithNewtonsMethod(0.0000001);
    lines.add("Zero: " + tau + " (" + finder.numIterations() + " iterations)");
    FourVector bBoosted = boostX.toNewFrame(histB.event(tau));
    
    lines.add("K' a: " + aBoosted);
    lines.add("K' b: " + bBoosted);
    diff = bBoosted.minus(aBoosted);
    lines.add("K' b-a: " + diff);
    lines.add("K' b-a len: " + diff.spatialMagnitude());
    double angle2 = Math.atan2(diff.y(), diff.x());
    lines.add("K' stick angle wrt X-axis: " + Util.radsToDegs(angle2) + " deg");
    lines.add("K' stick angle from formula: " + Util.radsToDegs(Physics.stickAngleAfterBoost(angle1, β)) + " deg");
    lines.add("1/gamma from formula: " + 1.0/Physics.Γ(β));
  }
  
  /** Compare numbers with exploration of the Thomas-Wigner rotation. */
  void stickAngledToXAxisWithEquivalentBoostParams(List<String> lines) {
    lines.add(" ");
    lines.add("Stick angled to the X-axis, with 2 params (speed and angle) taken from the corner-boost calc.");
    //the angle between the motion and the X-axis in K
    double restAngle = Util.degsToRads(24.227745317954163);
    double L0 = 1.0;
    History histA = new StationaryParticle(0.0, 0.0, 0.0,   0.0, 1.0); //origin
    History histB = new StationaryParticle(L0*Math.cos(restAngle), L0*Math.sin(restAngle), 0.0,   0.0, 1.0); //other end of the stick
    FourVector diff = histB.event(0.0).minus(histA.event(0.0));
    lines.add("K b-a:" + diff);
    lines.add("K stick length:" + diff.spatialMagnitude());
    double angleOrig = Math.atan2(diff.y(), diff.x());
    lines.add("K stick angle wrt X-axis: " + Util.radsToDegs(angleOrig) + " deg");
    
    //K': boost along the X axis
    double β = 0.8772684879784525;
    CoordTransform boostX = Boost.alongThe(Axis.X, β);
    lines.add("" );
    lines.add("Boost: " + boostX);
    lines.add("K' time-slice");
    //find events that have the same ct value in K'
    FourVector aBoosted = boostX.toNewFrame(histA.event(0.15));
    
    //FourVector bBoosted = boostX.toNewFrame(histB.event(0.95));
    Function<FourVector, Double> criterion = event -> (boostX.toNewFrame(event).ct() - aBoosted.ct());
    EventFinder finder = new EventFinder(histB, criterion, 0.000001) ;
    double tau = finder.searchWithNewtonsMethod(0.0000001);
    lines.add("Zero: " + tau + " (" + finder.numIterations() + " iterations)");
    FourVector bBoosted = boostX.toNewFrame(histB.event(tau));
    
    lines.add("K' a: " + aBoosted);
    lines.add("K' b: " + bBoosted);
    diff = bBoosted.minus(aBoosted);
    lines.add("K' b-a: " + diff);
    lines.add("K' b-a len from LT: " + diff.spatialMagnitude());
    double angleNew = Math.atan2(diff.y(), diff.x());
    lines.add("K' stick angle wrt X-axis: " + Util.radsToDegs(angleNew) + " deg");
    /* Calculated θw directly:-18.924644416051237 degrees */
    lines.add("Change in stick angle wrt X-axis: " + Util.radsToDegs(angleNew - angleOrig) + " deg SAME AS θw, the Thomas-Wigner rotation angle!");
    
    lines.add("1/Γ from formula: " + 1.0/Physics.Γ(β));
    lines.add("Γ from formula: " + Physics.Γ(β));
  }
}
