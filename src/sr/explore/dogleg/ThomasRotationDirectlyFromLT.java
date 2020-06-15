package sr.explore.dogleg;

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
import sr.core.transform.CoordTransformPipeline;
import sr.core.transform.FourVector;

/**  
 Explore Thomas-Wigner rotation using only the Lorentz Transformation and simple histories.
 
 <P>Use three frames K, K', then K'':
 <ul>
  <li>in K, first boost along X to K'.
  <li>in K', second boost along Y to K''.
 </ul>
 
  <P>Uses a stick to represent the direction of the X''-axis in K''.
  We want to measure the direction of the stick in frame K.

 <P>Algorithm:
 <ul>
  <li>in K'', form the histories of the two ends of the stick.
  The stick is stationary in K'', so this is just two vertical worldlines.
  <li>transform <em>backwards</em> from K'' to K' to K
  <li>get the histories of the ends of the stick, as meausured in K
  <li>take a time-slice across the two histories (ct = constant in K); using a time-slice 
  is always needed when you want to measure the spatial geometry
  <li>infer the direction of the stick, as seen in K, with respect to the X-axis 
 </ul>
 
 <P>The above algo does indeed show the geometry of the stick in K.
 But you need to be careful, because the geometry/orientation of the stick is affected by 2 items: 
 <ul>
  <li>the flattening effect of all boosts
  <li>the Thomas-Wigner rotation
 </ul>
 So, looking at the raw histories will show both of these effects added together.
*/
public final class ThomasRotationDirectlyFromLT {
  
  public static void main(String... args) {
    ThomasRotationDirectlyFromLT test = new ThomasRotationDirectlyFromLT();
    List<String> lines = new ArrayList<>();
    test.stickHistory(lines);
    Util.writeToFile(ThomasRotationDirectlyFromLT.class, "thomas-from-lt.txt", lines);
    for(String line : lines) {
      System.out.println(line);
    }
  }

  /**
   Start with a stationary stick in K''. 
   Get the histories of the ends of the stick (parameterized by proper-time).
   Boost 'backwards' from K'' to K', then from K' to K.
   In K, get a time-slice through the two histories, and infer the direction of the stick as seen in K.
   
   <P>Compare the direction of the stick with computed values that come from 
   both flattening and the Thomas-Wigner rotation.
  */
  private void stickHistory(List<String> lines) {
    //the stick is stationary in K''
    //a and b are the ends of the stick on the x-axis, from x=1 to x=2
    //here are their histories in K''
    History historyA = new StationaryParticle(1.0, 0.0, 0.0,  0.0, 1.0);
    History historyB = new StationaryParticle(2.0, 0.0, 0.0,  0.0, 1.0);
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
    lines.add("Corner-boost from K'' to K: "+ cornerBoost);
    lines.add("");
    lines.add("All of the items below are in K");
    lines.add("Raw histories");
    
    //time-slice 
    //find 2 events, one taken from each history, that have the same coord-time
    //these depend on the speeds chosen.
    double τA = 0.9; 
    FourVector evA = cornerBoost.toNewFrame(historyA.event(τA));
    
    Function<FourVector, Double> zero = event -> (cornerBoost.toNewFrame(event).ct() - evA.ct());
    EventFinder finder = new EventFinder(historyB, zero, 0.000001) ;
    double tau = finder.searchWithNewtonsMethod(0.0000001);
    lines.add("Zero: " + tau + " (" + finder.numIterations() + " iterations)");
    FourVector evB = cornerBoost.toNewFrame(historyB.event(tau));
    
    lines.add("event for end-a: " + evA + " one end of the stick");
    lines.add("event for end-b: " + evB + " other end of the stick");
    
    //find the angle
    //get the displacement between the two ends of the stick, and figure out the 
    //angle the stick is making with the x axis (basic trig)
    FourVector stick = evB.minus(evA);
    lines.add("stick in K (b-a): "+stick + " has ct=0 (time-slice)") ;
    double angle = Math.atan2(stick.y(), stick.x());
    lines.add("Angle of stick w.r.t the X-axis: " + Util.radsToDegs(angle) + " deg"); 
    lines.add("Length of the stick: " + stick.spatialMagnitude() + " shows some contraction");  
    
    ShowEquivalence cb = new ShowEquivalence(Axis.Z, β1, β2);
    lines.add(" ");
    lines.add("Calculated angles, to disentangle the 2 effects.");
    lines.add("Calculated βequiv: " + cb.equivalent().β); 
    lines.add("Calculated βdirection: " + Util.radsToDegs(cb.equivalent().βdirection) + " deg wrt X-axis"); 
    lines.add("Calculated θw: " + Util.radsToDegs(cb.equivalent().θw) + " deg wrt X-axis --- EFFECT #1"); 
    lines.add("Calculated βdirection + θw: " + Util.radsToDegs(-cb.equivalent().βdirection + cb.equivalent().θw) + " deg wrt βdirection"); 
    lines.add("Calculated flattened(βdirection + θw): " + Util.radsToDegs(Physics.stickAngleAfterBoost(-cb.equivalent().βdirection + cb.equivalent().θw, cb.equivalent().β)) + " deg wrt βdirection --- EFFECT #2"); 
    lines.add(" ");
    lines.add("Angle of stick w.r.t the βdirection (not X-axis) from raw histories: " + Util.radsToDegs(angle + (-1)*cb.equivalent().βdirection) + " deg SAME AS ABOVE"); 
  }
}