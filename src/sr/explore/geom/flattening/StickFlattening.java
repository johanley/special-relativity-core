package sr.explore.geom.flattening;

import static sr.core.Axis.X;

import java.util.function.Function;

import sr.core.Physics;
import sr.core.Util;
import sr.core.component.Event;
import sr.core.component.Position;
import sr.core.component.ops.Sense;
import sr.core.hist.timelike.FindEvent;
import sr.core.hist.timelike.TimelikeHistory;
import sr.core.hist.timelike.UniformVelocity;
import sr.core.vec3.Velocity;
import sr.core.vec4.FourDelta;
import sr.explore.Exploration;
import sr.output.text.TextOutput;

/** 
 Measure the flattening effect (Lorentz-Fitgerald contraction) on a stick, using a time-slice.
 As always, a time-slice is needed to see the geometrical properties of an object (length, orientation).
 
 <P>First, the stick is pointed in the direction of the boost (along the X-axis).
 
 <P>Second, place a stick at an angle with respect to the X-axis (not 0 or 90 degrees). 
 Do a boost along the X-axis.
 In the boosted frame, the angle of the stick with respect to the X-axis will change.
 The stick will "flatten" along the direction of the boost.
 This changes the direction in which the stick is pointing. 
 At ultra-relativistic speeds, the rotated stick will approach the direction of 90 degrees away from the X-axis.
*/
public class StickFlattening extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    Exploration stickFlattening = new StickFlattening();
    stickFlattening.explore();
  }
  
  @Override public void explore() {
    stickAlongAxis(0.6);
    stickAlongAxis(0.9999);
    add(Util.NL + "Conclusion 1: in the boosted frame, the stick's length is reduced, and asymptotically approaches 0.");
    
    stickAngledToXAxis(0.6);
    stickAngledToXAxis(0.9999);
    add(Util.NL + "Conclusion 2: in the boosted frame, the stick's angle with respect to the X-axis is increased, and asymptotically approaches 90°.");
    
    stickAngledToXAxisWithEquivalentBoostParams();
    
    outputToConsoleAnd("stick-flattening.txt");
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
  void stickAlongAxis(Double β) {
    add(Util.NL + "1. Boost speed " + β + ". Stick along the X-axis in the unboosted frame.");
    add(SEP);
    add("Time-slice in K (same ct coords), to see the geometry of the stationary stick:");
    //the stick is stationary in K
    //the stick is along the x-axis, from x=1 to x=2
    TimelikeHistory histA = UniformVelocity.stationary(Position.of(X, 1.0));
    TimelikeHistory histB = UniformVelocity.stationary(Position.of(X, 2.0));
    //time-slice in K; any time will do, since it's stationary in K
    double ct = 0.0; 
    add("K a: " + histA.event(ct));
    add("K b: " + histB.event(ct));
    FourDelta delta_K = FourDelta.of(histA.event(ct), histB.event(ct));
    add("K b-a: " + delta_K);
    add("K stick length:" + delta_K.spatialMagnitude() + Util.NL);
    
    //K': boost along the X axis
    Velocity boost_v = Velocity.of(β, X);
    
    //time-slice in K': find two events that have the same ct' value in K'
    //events are identified using ct along the history
    Event aBoosted = histA.event(0.18).boost(boost_v, Sense.ChangeGrid); //start with some event on A's history
    //root: the difference in K' of the ct' coord vanishes
    Function<Event, Double> criterion = event -> (event.boost(boost_v, Sense.ChangeGrid).ct() - aBoosted.ct());
    FindEvent findEvent = new FindEvent(histB, criterion);
    double ctB = findEvent.search(0.0);
    Event bBoosted = histB.event(ctB).boost(boost_v, Sense.ChangeGrid);
    FourDelta delta_Kp = FourDelta.of(aBoosted, bBoosted);
    
    add("Boost: boost "+ boost_v);
    add("Time-slice pair of events in K' (same ct' coords), to see the geometry of the moving stick:");
    add("K' a: " + aBoosted);
    add("K' b: " + bBoosted);
    add("K' b-a: " + delta_Kp);
    add("K' stick length: " + Util.round(delta_Kp.spatialMagnitude(), 4));
    add("1/Γ from formula: " + 1.0/Physics.Γ(β));
  }
  
  /**
   Measure the flattening  (Lorentz-Fitgerald contraction) of a stick which is at an angle to the X-axis (the line of a boost).
   
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
    <li>the stick is 'squished' in the X-direction only; its angle with respect to the X-axis increases from 45° to a higher value
    <li>for ultra-relativistic speeds, the angle asymptotically approaches 90°
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
  void stickAngledToXAxis(Double β) {
    add(Util.NL + "2. Boost speed " + β + ". Stick angled 45° to the X-axis in the unboosted frame, from the origin to (X,Y,Z)=(1,1,0). ");
    add(SEP);
    add("Time-slice in K (same ct coords), to see the geometry of the stationary stick:");
    //the stick is stationary in K, from the origin to x=1, y=1, z=0
    TimelikeHistory histA = UniformVelocity.stationary(Position.origin()); 
    TimelikeHistory histB = UniformVelocity.stationary(Position.of(1.0, 1.0, 0.0)); //other end of the stick
    FourDelta diff = FourDelta.of(histA.event(0.0), histB.event(0.0));
    add("K a:" + histA.event(0.0));
    add("K b:" + histB.event(0.0));
    add("K b-a:" + diff);
    add("K stick length:" + diff.spatialMagnitude());
    double angle1 = Math.atan2(diff.y(), diff.x());
    add("K stick angle with respect to the X-axis: " + Util.radsToDegs(angle1) + "°" + Util.NL);
    
    //K': boost along the X axis
    Velocity boost_v = Velocity.of(β, X);
    add("Boost:boost " + boost_v);
    add("Time-slice pair of events in K' (same ct' coords), to see the geometry of the moving stick:");
    
    //find events that have the same ct' value in K'
    Event aBoosted = histA.event(0.18).boost(boost_v, Sense.ChangeGrid); //start with some event on A's history
    
    Function<Event, Double> criterion = event -> (event.boost(boost_v, Sense.ChangeGrid).ct() - aBoosted.ct());
    FindEvent findEvent = new FindEvent(histB, criterion);
    double ctB = findEvent.search(0.0);
    Event bBoosted = histB.event(ctB).boost(boost_v, Sense.ChangeGrid);
    
    add("K' a: " + aBoosted);
    add("K' b: " + bBoosted);
    diff = FourDelta.of(aBoosted, bBoosted);
    add("K' b-a: " + diff);
    add("K' stick length: " + diff.spatialMagnitude());
    double angle2 = Math.atan2(diff.y(), diff.x());
    add("K' stick angle with respect to the X-axis: " + Util.round(Util.radsToDegs(angle2), 4) + "°");
    add("K' stick angle directly from a formula: " + Util.round(Util.radsToDegs(Physics.stickAngleAfterBoost(angle1, β)),4) + "°");
  }
  
  /** 
   Compare numbers with the exploration of the kinematic (Thomas-Wigner) rotation.
   
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
    <li>18.925° is the kinematic (Thomas-Wigner) rotation angle taken from the original elbow-boost example (with two non-collinear boosts). 
   </ul>
  */
  void stickAngledToXAxisWithEquivalentBoostParams() {
    add(Util.NL + "3. Stick of unit length is angled to the X-axis, with one end at the origin and the other in the XY-plane.");
    add("Two params (stick-angle and boost-speed) are taken from the elbow-boost calculation (with two non-collinear boosts).");
    add(SEP);
    //the angle between the motion and the X-axis in K
    double restAngle = Util.degsToRads(24.227745317954163);
    double L0 = 1.0;
    TimelikeHistory histA = UniformVelocity.stationary(Position.origin()); 
    TimelikeHistory histB = UniformVelocity.stationary(Position.of(L0*Math.cos(restAngle), L0*Math.sin(restAngle), 0.0)); //other end of the stick
    FourDelta diff = FourDelta.of(histA.event(0.0), histB.event(0.0));
    add("K b-a:" + diff);
    add("K stick length:" + diff.spatialMagnitude());
    double angleOrig = Math.atan2(diff.y(), diff.x());
    add("K stick angle wrt X-axis: " + Util.radsToDegs(angleOrig) + "°");
    
    //K': boost along the X axis
    double β = 0.8772684879784525;
    Velocity boost_v = Velocity.of(β, X);
    add(Util.NL + "Boost: boost " + boost_v);
    add("Time-slice pair of events in K' (same ct' coords), to see the geometry of the moving stick:");
    //find events that have the same ct value in K'
    Event aBoosted = histA.event(0.15).boost(boost_v, Sense.ChangeGrid);
    
    Function<Event, Double> criterion = event -> (event.boost(boost_v, Sense.ChangeGrid).ct() - aBoosted.ct());
    FindEvent findEvent = new FindEvent(histB, criterion);
    double ctB = findEvent.search(0.0);
    Event bBoosted = histB.event(ctB).boost(boost_v, Sense.ChangeGrid);
    
    add("K' a: " + aBoosted);
    add("K' b: " + bBoosted);
    diff = FourDelta.of(aBoosted, bBoosted);
    add("K' b-a: " + diff);
    add("K' b-a stick length: " + diff.spatialMagnitude());
    double angleNew = Math.atan2(diff.y(), diff.x());
    add("K' stick angle with respect to the X-axis: " + Util.radsToDegs(angleNew) + "°");
    /* Calculated θw directly:-18.924644416051237 degrees */
    add("Change in stick angle with respect to the X-axis: " + Util.radsToDegs(angleNew - angleOrig) + "° SAME AS θw, the kinematic (Thomas-Wigner) rotation angle in the elbow-boost case!");
  }
  
  private static final String SEP = Util.separator(100);
}
