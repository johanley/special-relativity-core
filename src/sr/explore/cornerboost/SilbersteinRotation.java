package sr.explore.cornerboost;

import java.util.function.Function;

import sr.core.Axis;
import sr.core.EventFinder;
import sr.core.Physics;
import sr.core.Util;
import sr.core.history.History;
import sr.core.history.StationaryParticle;
import sr.core.transform.Boost;
import sr.core.transform.CoordTransform;
import sr.core.transform.CoordTransformPipeline;
import sr.core.transform.FourVector;
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
  <li>the flattening effect of all boosts
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
    //the stick is stationary in K''
    //a and b are the ends of the stick on the x-axis, from x=1 to x=2
    //here are their histories in K''
    History historyA = new StationaryParticle(1.0, 0.0, 0.0,  0.0, 1.0);
    History historyB = new StationaryParticle(2.0, 0.0, 0.0,  0.0, 1.0);
    lines.add("Stick is stationary in K''. Points along the +X''-axis. Has ends at X=1 and X=2.");
    double τ0 = 0.0; //any proper time will do here: it's stationary in K''
    double restLength = historyB.event(τ0).minus(historyA.event(τ0)).spatialMagnitude();
    lines.add("Rest length of the stick in K'': " + restLength); 
    
    //corner-boost "backwards" from K'' to K
    double β1 = 0.8;
    double β2 = 0.6;
    CoordTransform cornerBoost = CoordTransformPipeline.join(
      Boost.alongThe(Axis.Y, -β2), //minus signs, because we're going backwards here
      Boost.alongThe(Axis.X, -β1)        
    );
    lines.add("Corner-boost backwards from K'' to K: "+ cornerBoost);
    lines.add("");
    lines.add("All of the items below are in K." + Util.NL);
    
    //time-slice 
    //find 2 events, one taken from each history, that have the same coord-time
    //these depend on the speeds chosen.
    double τA = 0.9; 
    FourVector evA = cornerBoost.toNewFrame(historyA.event(τA));
    
    Function<FourVector, Double> zero = event -> (cornerBoost.toNewFrame(event).ct() - evA.ct());
    EventFinder finder = new EventFinder(historyB, zero, 0.000001) ;
    double tau = finder.searchWithNewtonsMethod(0.0000001);
    //lines.add("Zero: " + tau + " (" + finder.numIterations() + " iterations)");
    FourVector evB = cornerBoost.toNewFrame(historyB.event(tau));
    
    lines.add("Time-slice across the stick's history in K.");
    lines.add("Examine two events, one for each end of the stick.");
    lines.add("Event for A: " + evA + " one end of the stick.");
    lines.add("Event for B: " + evB + " other end of the stick.");
    
    //find the angle
    //get the displacement between the two ends of the stick, and figure out the 
    //angle the stick is making with the x axis (basic trig)
    FourVector stick = evB.minus(evA);
    lines.add("Difference (B - A): "+stick + " has ct=0 (time-slice).") ;
    double angle = Math.atan2(stick.y(), stick.x());
    lines.add("Angle of the stick with respect to the X-axis: " + round(Util.radsToDegs(angle)) + "°"); 
    lines.add("Length of the stick: " + round(stick.spatialMagnitude()) + ". It shows some contraction.");  
    
    EquivalentBoostPlusRotation calc = new EquivalentBoostPlusRotation(Axis.Z, β1, β2);
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
    lines.add("Angle of the stick with respect to the equivDirection (not the X-axis) from raw histories: " + degrees(angle - calc.equivalent().βdirection) + " - SAME AS ABOVE"); 
  }
 
  private String degrees(double rads) {
    return round(Util.radsToDegs(rads)) + "°";
  }
  
  private double round(double value) {
    return Util.round(value, 3);
  }
}