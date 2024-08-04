package sr.explore.noncolinear.boost.silbersteinrotation;

import java.util.function.Function;

import static sr.core.Axis.*;

import sr.core.Physics;
import sr.core.Util;
import sr.core.event.Event;
import sr.core.event.FindEvent;
import sr.core.event.transform.Boost;
import sr.core.event.transform.Transform;
import sr.core.event.transform.TransformPipeline;
import sr.core.history.History;
import sr.core.history.UniformVelocity;
import sr.core.vector.Position;
import sr.explore.noncolinear.boost.corner.EquivalentBoostPlusRotation;
import sr.output.text.TextOutput;

/**  
 Explore Silberstein (Thomas-Wigner) rotation  using only the Lorentz Transformation and simple histories.
 
 <P>Use three frames K, K', then K'', and a corner-boost:
 <ul>
  <li>in K, first boost along X to K'.
  <li>in K', second boost along Y to K''.
 </ul>
 
  <P>Uses a stick to represent the direction of the X''-axis in K''.
  The task is to measure the direction of the stick in frame K.

 <P>Algorithm:
 <ul>
  <li>in K'', form the histories of the two ends of the stick.
  The stick is stationary in K'', so this is just two vertical worldlines.
  <li>transform <em>backwards</em> from K'' to K' to K
  <li>get the histories of the ends of the stick, with respect to K
  <li>take a time-slice across the two histories (ct = constant in K); using a time-slice 
  is always needed when you want to measure the spatial geometry
  <li>infer the direction of the stick, as seen in K, with respect to the X-axis 
 </ul>
 
 <P>The above algorithm does indeed show the geometry of the stick in K.
 But you need to be careful, because when non-collinear boosts are used, the geometry/orientation of the stick is affected by 2 items: 
 <ul>
  <li>the flattening effect seen with boosts in general
  <li>the Silberstein rotation, seen with non-collinear boosts
 </ul>
*/
public final class SilbersteinRotation extends TextOutput {
  
  public static void main(String... args) {
    SilbersteinRotation rotation = new SilbersteinRotation();
    rotation.explore();
  }
  
  void explore() {
    stickHistory();
    outputToConsoleAnd("silberstein-rotation.txt");
  }

  /**
   Start with a stationary stick in K''. 
   Get the histories of the ends of the stick (parameterized by proper-time).
   Boost 'backwards' from K'' to K', then from K' to K.
   In K, get a time-slice through the two histories, and infer the direction of the stick as seen in K.
   
   <P>Compare the direction of the stick with computed values that come from 
   both flattening and Silberstein (Thomas-Wigner) rotation.
  */
  private void stickHistory() {
    
    /*
     In variable names, use these aliases for the frames:
     K'' : Kpp (as in the number of primes)
     K'  : Kp
     K   : K
    */
    
    //the stick is stationary in K''
    //a and b are the ends of the stick on the x-axis, from x=1 to x=2
    //here are their histories in K''
    History historyA_Kpp = UniformVelocity.stationary(Position.of(X, 1.0));
    History historyB_Kpp = UniformVelocity.stationary(Position.of(X, 2.0));
    lines.add("Stick is stationary in K''. Points along the +X''-axis. Has ends at X=1 and X=2.");
    double ct_Kpp = 0.0; //any ct'' time will do here: it's stationary in K''
    double restLength_Kpp = historyB_Kpp.event(ct_Kpp).minus(historyA_Kpp.event(ct_Kpp)).spatialMagnitude();
    lines.add("Rest length of the stick in K'': " + restLength_Kpp); 
    
    //corner-boost "backwards" from K'' all the way back to K
    double β1 = 0.8;
    double β2 = 0.6;
    Transform cornerBoost = TransformPipeline.join(
      Boost.alongThe(Y, -β2), //minus signs, because we're going backwards here
      Boost.alongThe(X, -β1)        
    );
    lines.add("Corner-boost backwards from K'' to K: "+ cornerBoost);
    lines.add("");
    lines.add("All of the items below are in K." + Util.NL);
    
    //time-slice 
    //find 2 events, one taken from each history, that have the same coord-time
    //these depend on the speeds chosen.
    double ctA_Kpp = 0.9; //any old ct'' value 
    Event eventA_K = cornerBoost.changeFrame(historyA_Kpp.event(ctA_Kpp)); 
    
    Function<Event, Double> criterion = event -> (cornerBoost.changeFrame(event).ct() - eventA_K.ct());
    FindEvent findEvent = new FindEvent(historyB_Kpp, criterion);
    double ctB_Kpp = findEvent.search(0.0); 
    Event eventB_K = cornerBoost.changeFrame(historyB_Kpp.event(ctB_Kpp)); 
    
    lines.add("Time-slice across the stick's history in K.");
    lines.add("Examine two events, one for each end of the stick.");
    lines.add("Event for A: " + eventA_K + " one end of the stick.");
    lines.add("Event for B: " + eventB_K + " other end of the stick.");
    
    //find the angle
    //get the displacement between the two ends of the stick, and figure out the 
    //angle the stick is making with the x axis (basic trig)
    Event stick_K = eventB_K.minus(eventA_K);
    lines.add("Difference (B - A): "+stick_K + " has ct=0 (time-slice).") ;
    double angle_K = Math.atan2(stick_K.y(), stick_K.x());
    lines.add("Angle of the stick with respect to the X-axis: " + round(Util.radsToDegs(angle_K)) + "°"); 
    lines.add("Length of the stick: " + round(stick_K.spatialMagnitude()) + ". It shows some contraction.");  
    
    EquivalentBoostPlusRotation calc = new EquivalentBoostPlusRotation(Z, β1, β2);
    lines.add(" ");
    lines.add("Disentangle two effects: Silberstein rotation versus flattening."+Util.NL);
    lines.add("FIRST DO A ROTATION (because of the Silberstein rotation)."); 
    lines.add("Examine the equivalent-boost with respect to K (the boost+rotation pair that's equivalent to the corner-boost pair):");
    lines.add("  equivβ: " + round(calc.equivalent().β)); 
    lines.add("  equivDirection: " + degrees(calc.equivalent().βdirection) + " with respect to the X-axis."); 
    lines.add("  equivθw: " + degrees(calc.equivalent().θw) + " rotation around the Z-axis (which thus affects the XY plane)."); 
    lines.add("  (equivθw - equivDirection): " + degrees(calc.equivalent().θw -calc.equivalent().βdirection) + " with respect to the equivDirection."+Util.NL); 
    lines.add("SECOND DO A FLATTENING (because of the equivalent-boost)."); 
    lines.add("Use a formula for flattening(equivθw - equivDirection, equivβ): " + degrees(Physics.stickAngleAfterBoost(calc.equivalent().θw - calc.equivalent().βdirection, calc.equivalent().β)) + " with respect to the equivDirection."); 
    lines.add(" ");
    lines.add("The same result comes from examining the raw history of the stick.");
    lines.add("Angle of the stick with respect to the equivDirection (not the X-axis) from raw histories: " + degrees(angle_K - calc.equivalent().βdirection) + " - SAME AS ABOVE"); 
  }
 
  private String degrees(double rads) {
    return round(Util.radsToDegs(rads)) + "°";
  }
  
  private double round(double value) {
    return Util.round(value, 3);
  }
}