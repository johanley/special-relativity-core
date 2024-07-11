package sr.explore.flattening;

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
import sr.explore.output.text.TextOutput;

/** 
 Measure the flattening effect on a stick, using a time-slice.
 As always, a time-slice is needed to see the geometrical properties of an object (length, orientation).
 
 <P>First, the stick is pointed in the direction of the boost (along the X-axis).
 
 <P>Second, place a stick at an angle with respect to the X-axis (not 0 or 90 degrees). 
 Do a boost along the X-axis.
 In the boosted frame, the angle of the stick with respect to the X-axis will change.
 The stick will "rotate away" from the direction of the boost. 
 At ultra-relativistic speeds, the rotated stick will approach the direction of 90 degrees away from the X-axis.
*/
public class StickFlattening extends TextOutput {
  
  public static void main(String[] args) {
    StickFlattening stickFlattening = new StickFlattening();
    stickFlattening.explore();
  }
  
  public void explore() {
    stickAlongAxis(lines, 0.6);
    stickAlongAxis(lines, 0.9999);
    lines.add(Util.NL + "Conclusion 1: in the boosted frame, the stick's length is reduced, and asymptotically approaches 0.");
    
    stickAngledToXAxis(lines, 0.6);
    stickAngledToXAxis(lines, 0.9999);
    lines.add(Util.NL + "Conclusion 2: in the boosted frame, the stick's angle with respect to the X-axis is increased, and asymptotically approaches 90°.");
    
    stickAngledToXAxisWithEquivalentBoostParams(lines);
    
    outputTo("stick-flattening.txt");
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
   
   @param β the boost speed along the X-direction 
  */
  void stickAlongAxis(List<String> lines, Double β) {
    lines.add(Util.NL + "1. Boost speed " + β + ". Stick along the X-axis in the unboosted grid.");
    lines.add(SEP);
    lines.add("Time-slice in K (same ct coords), to see the geometry of the stationary stick:");
    //the stick is stationary in K
    //the stick is along the x-axis, from x=1 to x=2
    History histA = new StationaryParticle(1.0, 0.0, 0.0,   0.0, 100.0);
    History histB = new StationaryParticle(2.0, 0.0, 0.0,   0.0, 100.0);
    double τ = 0.0; //any proper time will do, since it's stationary in K
    lines.add("K a: " + histA.event(τ));
    lines.add("K b: " + histB.event(τ));
    FourVector bMinusA = histB.event(τ).minus(histA.event(τ));
    lines.add("K b-a: " + bMinusA);
    lines.add("K stick length:" + bMinusA.spatialMagnitude() + Util.NL);
    
    //K': boost along the X axis
    CoordTransform boostX = Boost.alongThe(Axis.X, β);
    
    //time-slice: find two events that have the same ct' value in K'
    //events are identified using τ along the history
    FourVector aBoosted = boostX.toNewFrame(histA.event(0.18)); //start with some event on A's history
    Function<FourVector, Double> criterion = event -> (boostX.toNewFrame(event).ct() - aBoosted.ct());
    EventFinder finder = new EventFinder(histB, criterion, 0.000001) ;
    double tau = finder.searchWithNewtonsMethod(0.0000001);
    //lines.add("Zero: τ=" + tau + " (" + finder.numIterations() + " iterations)");
    FourVector bBoosted = boostX.toNewFrame(histB.event(tau));
    
    lines.add("Boost: "+ boostX);
    lines.add("Time-slice pair of events in K' (same ct' coords), to see the geometry of the moving stick:");
    lines.add("K' a: " + aBoosted);
    lines.add("K' b: " + bBoosted);
    lines.add("K' b-a: " + bBoosted.minus(aBoosted));
    lines.add("K' stick length: " + Util.round(bBoosted.minus(aBoosted).spatialMagnitude(), 4));
    lines.add("1/Γ from formula: " + 1.0/Physics.Γ(β));
  }
  

  /**
   Measure the flattening of a stick that's at an angle to the X-axis (the line of a boost).
   
   In K:
   <ul>
    <li>the stick is stationary
    <li>it's on the XY-plane.
    <li>it's angled at 45° to the X-axis. 
    <li>it goes from the origin to (x=1, y=1, z=0), and its length is sqrt(2) = 1.41421356.
   </ul>
   
   In K', boosted with the given speed β with respect to K along the X axis:
   <ul>
    <li>measure the stick in K'
    <li>its sides are now of length 1.0 (y) and 0.8(x), and its length is 1.2806248
    <li>the stick is 'squished' in the X-direction only; its angle w.r.t. the X axis increases from 45° to a higher value
    <li>for ultrarelativistic speeds, the angle asymptotically approaches 90°
   </ul>
   
   Ref : Problem Book in Relativity and Gravitation (Lightman and others)
   Has this very problem (Problem 1.7). 
   Answer for the relation between the before after angles: cot θ = (1/Γ) cot θ'
   This agrees with my computation below.
   UNEXPECTED: the DIFFERENCE in the θ's equals the Thomas-Wigner rotation angle!
   Ref: https://users.physics.ox.ac.uk/~smithb/website/coursenotes/rel_A.pdf, section 5.7 
   mentions the same math in the context of Thomas rotation.
   
   @param β the boost speed along the X-direction 
  */
  void stickAngledToXAxis(List<String> lines, Double β) {
    lines.add(Util.NL + "2. Boost speed " + β + ". Stick angled 45° to the X-axis in the unboosted grid, from the origin to (X,Y,Z)=(1,1,0). ");
    lines.add(SEP);
    lines.add("Time-slice in K (same ct coords), to see the geometry of the stationary stick:");
    //the stick is stationary in K, from the origin to x=1, y=1, z=0
    History histA = new StationaryParticle(0.0, 0.0, 0.0,   0.0, 100.0); //origin
    History histB = new StationaryParticle(1.0, 1.0, 0.0,   0.0, 100.0); //other end of the stick
    FourVector diff = histB.event(0.0).minus(histA.event(0.0));
    lines.add("K a:" + histA.event(0.0));
    lines.add("K b:" + histB.event(0.0));
    lines.add("K b-a:" + diff);
    lines.add("K stick length:" + diff.spatialMagnitude());
    double angle1 = Math.atan2(diff.y(), diff.x());
    lines.add("K stick angle with respect to the X-axis: " + Util.radsToDegs(angle1) + "°" + Util.NL);
    
    //K': boost along the X axis
    CoordTransform boostX = Boost.alongThe(Axis.X, β);
    lines.add("Boost:" + boostX);
    lines.add("Time-slice pair of events in K' (same ct' coords), to see the geometry of the moving stick:");
    
    //find events that have the same ct' value in K'
    FourVector aBoosted = boostX.toNewFrame(histA.event(0.18)); //start with some event on A's history
    
    Function<FourVector, Double> criterion = event -> (boostX.toNewFrame(event).ct() - aBoosted.ct());
    EventFinder finder = new EventFinder(histB, criterion, 0.000001) ;
    double tau = finder.searchWithNewtonsMethod(0.0000001);
    //lines.add("Zero: " + tau + " (" + finder.numIterations() + " iterations)");
    FourVector bBoosted = boostX.toNewFrame(histB.event(tau));
    
    lines.add("K' a: " + aBoosted);
    lines.add("K' b: " + bBoosted);
    diff = bBoosted.minus(aBoosted);
    lines.add("K' b-a: " + diff);
    lines.add("K' stick length: " + diff.spatialMagnitude());
    double angle2 = Math.atan2(diff.y(), diff.x());
    lines.add("K' stick angle with respect to the X-axis: " + Util.round(Util.radsToDegs(angle2), 4) + "°");
    lines.add("K' stick angle directly from a formula: " + Util.round(Util.radsToDegs(Physics.stickAngleAfterBoost(angle1, β)),4) + "°");
  }
  
  /** 
   Compare numbers with the exploration of the Silberstein (Thomas-Wigner) rotation.
   
   In K:
   <ul>
    <li>the stick is stationary
    <li>it's on the XY-plane.
    <li>it's angled at 24.228° with respect to the X-axis. 
    <li>it goes from the origin to (X,Y,Z) = (0.9119, 0.4104, 0.0) and its length is 1.
   </ul>
   
   In K', boosted with the given speed β with respect to K along the X axis:
   <ul>
    <li>the stick is squished in the X-direction only
    <li>its angle with respect to the X axis increases to 43.152°, an increase of 18.925°.
    <li>18.925° is the Silberstein (Thomas-Wigner) rotation angle taken from the original corner-boost example. 
   </ul>
  */
  void stickAngledToXAxisWithEquivalentBoostParams(List<String> lines) {
    lines.add(Util.NL + "3. Stick of unit length is angled to the X-axis, with one end at the origin and the other in the XY-plane.");
    lines.add("Two params (stick-angle and boost-speed) are taken from the corner-boost calculation.");
    lines.add(SEP);
    //the angle between the motion and the X-axis in K
    double restAngle = Util.degsToRads(24.227745317954163);
    double L0 = 1.0;
    History histA = new StationaryParticle(0.0, 0.0, 0.0,   0.0, 100.0); //origin
    History histB = new StationaryParticle(L0*Math.cos(restAngle), L0*Math.sin(restAngle), 0.0,   0.0, 100.0); //other end of the stick
    FourVector diff = histB.event(0.0).minus(histA.event(0.0));
    lines.add("K b-a:" + diff);
    lines.add("K stick length:" + diff.spatialMagnitude());
    double angleOrig = Math.atan2(diff.y(), diff.x());
    lines.add("K stick angle wrt X-axis: " + Util.radsToDegs(angleOrig) + "°");
    
    //K': boost along the X axis
    double β = 0.8772684879784525;
    CoordTransform boostX = Boost.alongThe(Axis.X, β);
    lines.add(Util.NL + "Boost: " + boostX);
    lines.add("Time-slice pair of events in K' (same ct' coords), to see the geometry of the moving stick:");
    //find events that have the same ct value in K'
    FourVector aBoosted = boostX.toNewFrame(histA.event(0.15));
    
    //FourVector bBoosted = boostX.toNewFrame(histB.event(0.95));
    Function<FourVector, Double> criterion = event -> (boostX.toNewFrame(event).ct() - aBoosted.ct());
    EventFinder finder = new EventFinder(histB, criterion, 0.000001) ;
    double tau = finder.searchWithNewtonsMethod(0.0000001);
    //lines.add("Zero: " + tau + " (" + finder.numIterations() + " iterations)");
    FourVector bBoosted = boostX.toNewFrame(histB.event(tau));
    
    lines.add("K' a: " + aBoosted);
    lines.add("K' b: " + bBoosted);
    diff = bBoosted.minus(aBoosted);
    lines.add("K' b-a: " + diff);
    lines.add("K' b-a stick length: " + diff.spatialMagnitude());
    double angleNew = Math.atan2(diff.y(), diff.x());
    lines.add("K' stick angle with respect to the X-axis: " + Util.radsToDegs(angleNew) + "°");
    /* Calculated θw directly:-18.924644416051237 degrees */
    lines.add("Change in stick angle with respect to the X-axis: " + Util.radsToDegs(angleNew - angleOrig) + "° SAME AS θw, the Silberstein (Thomas-Wigner) rotation angle in the corner-boost case!");
  }
  
  private static final String SEP = Util.separator(100);
}
