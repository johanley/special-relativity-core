package sr.explore.dogleg;

import sr.core.Axis;
import sr.core.Util;
import sr.core.history.History;
import sr.core.history.StationaryParticle;
import sr.core.history.UniformVelocity;
import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.Boost;
import sr.core.transform.CoordTransform;
import sr.core.transform.CoordTransformPipeline;
import sr.core.transform.FourVector;

/**  
 Demonstrate Thomas-Wigner rotation using only the Lorentz Transformation and simple histories.
 
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
*/
public final class ThomasRotationDirectlyFromLT {
  
  public static void main(String... args) {
    ThomasRotationDirectlyFromLT test = new ThomasRotationDirectlyFromLT();
    test.stickHistory();
  }

  
  /**
   Start with a stationary stick in K''. 
   Get the histories of the ends of the stick (parameterized by proper-time).
   Boost 'backwards' to K', then K.
   In K, get a time-slice through the two histories, and infer the direction of the stick as seen in K.
  */
  private void stickHistory() {
    //the stick is stationary in K''
    //a and b are the ends of the stick on the x-axis, from x=1 to x=2
    //here are their histories in K''
    History historyA = new StationaryParticle(1.0, 0.0, 0.0,  0.0, 1.0);
    History historyB = new StationaryParticle(2.0, 0.0, 0.0,  0.0, 1.0);
    double τ0 = 0.0; //any proper time will do: it's stationary in K''
    double restLength = historyB.event(τ0).minus(historyA.event(τ0)).spatialMagnitude();
    Util.log("Rest length of the stick in K'': " + restLength); //1.0
    
    //corner-boost from K'' to K
    double β1 = 0.8;
    double β2 = 0.6;
    CoordTransform cornerBoost = CoordTransformPipeline.join(
      Boost.alongThe(Axis.Y, -β2), //minus signs, because we're going backwards here
      Boost.alongThe(Axis.X, -β1)        
    );
    Util.log("Reverse-boosts from K'' to K "+ cornerBoost);
    
    //time-slice 
    //find 2 events, one taken from each history, that have the same coord-time
    //found by TRIAL AND ERROR! proper times; these depend on the speeds chosen.
    double τA = 0.9; 
    double τB = 0.26;
    FourVector aK = cornerBoost.toNewFrame(historyA.event(τA));
    FourVector bK = cornerBoost.toNewFrame(historyB.event(τB));
    Util.log("event for end-a in K: " + aK + " one end of the stick, seen in K");
    Util.log("event for end-b in K: " + bK + " other end of the stick, seen in K");
    
    //find the angle
    //get the displacement between the two ends of the stick, and figure out the 
    //angle the stick is making with the x axis (basic trig)
    FourVector stick = bK.minus(aK);
    Util.log("stick in K (b-a): "+stick + " has ct=0 (time-slice)") ;
    double angle = Math.atan2(stick.y(), stick.x());
    Util.log("Angle of stick w.r.t the X-axis in K: " + Util.radsToDegs(angle) + " degrees. Right direction, wrong amount."); //-38.6
    Util.log("Length of the stick in K: " + stick.spatialMagnitude() + " shows some contraction");  //0.768
    
    ShowEquivalence cb = new ShowEquivalence(Axis.Z, β1, β2);
    Util.log(" ");
    Util.log("Calculated θw directly:" + Util.radsToDegs(cb.equivalent().θw) + " degrees"); //-18.92
    Util.log("Calculated βdirection:" + Util.radsToDegs(cb.equivalent().βdirection) + " degrees"); //24.228
  }

}
