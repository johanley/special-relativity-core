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
 A single boost along an axis preserves the directions of all axes.
 
 Two non-colinear boosts rotate axes. Take 3 frames K, K', then K''.
 <ul>
  <li>in K, first boost along X to K'.
  <li>in K', second boost along Y to K''.
 </ul>
 
 <P>In K' (in the middle): the axes of K and K'' are all parallel to the axes of K'.
 
 <P>But K and K'' DON'T see their respective X and Y axes as being parallel. 
 For that pair, only the Z axes are parallel, in the above case.
 
 <P>To see this, think of sticks, and how they're measured. 
 To measure the length of a stick in a frame, you need a time-slice in the frame.
 The same is true of all 3D geometrical properties of objects: you always need a time-slice.
 So we need a time-slice in order to see the direction of the axis-stick.
*/
public final class TwoBoostsRotateAxes {
  
  public static void main(String... args) {
    TwoBoostsRotateAxes test = new TwoBoostsRotateAxes();
    //test.twoBoostsThenLookAtAnAxis();
    //test.stickHistoryGoingForwards();
    test.stickHistory2();
  }

  private void stickHistoryGoingForwards() {
    //the stick is stationary in K
    //a and b are the ends of a stick on the x-axis, from x=1 to x=2
    //let's avoid the origin here
    History historyA = new StationaryParticle(1.0, 0.0, 0.0,  0.0, 1.0);
    History historyB = new StationaryParticle(2.0, 0.0, 0.0,  0.0, 1.0);
    
    //two boosts, along X, then Y
    double β1 = 0.8;
    double β2 = 0.6;
    CoordTransform cornerBoost = CoordTransformPipeline.join(
      Boost.alongThe(Axis.X, β1),
      Boost.alongThe(Axis.Y, β2)        
    );
    
    //by trial and error, I choose two different proper-times for A and B, that give the SAME coord-time;
    //so, we have time slice, with ct=the same across both histories
    double τA = 0.8;
    double τB = 0.16;
    FourVector aKprimeprime = cornerBoost.toNewFourVector(historyA.event(τA));
    FourVector bKprimeprime = cornerBoost.toNewFourVector(historyB.event(τB));
    Util.log("a'' event: " + aKprimeprime);
    Util.log("b'' event: " + bKprimeprime);
    //time-slice: now calc the angle between the space parts only (x, y)
    //now we need a displacement between the two ends of the stick, and to figure out the 
    //angle the stick is making with the x axis
    FourVector diff = bKprimeprime.minus(aKprimeprime);
    Util.log("b-a:"+diff);
    double numer = diff.y();
    double denom = diff.x();
    double angle = Math.atan2(numer, denom);
    Util.log("Angle of stick w.r.t the X-axis: " + Util.radsToDegs(angle) + " degrees"); //-38.6
    //WRONG. Correct direction, but wrong magnitude
    //my calc of Thomas Rotation has -18.9246 degrees (I'm a bit over double that figure) 
    //0.8 0.6 0.8772684879784525 24.227745317954163 -18.924644416051237
    //0.6 0.8 0.8772684879784525 46.8476102659946 -18.924644416051237 
    ShowEquivalence cb = new ShowEquivalence(Axis.Z, β1, β2);
    Util.log(" ");
    Util.log("Calculated θw:" + Util.radsToDegs(cb.equivalent().θw) + " degrees");
    Util.log("Calculated βdirection:" + Util.radsToDegs(cb.equivalent().βdirection) + " degrees");
  }
  
  /**
   Start with a stationary stick in K''. 
   Get the histories of the ends of the stick (parameterized by proper-time).
   Boost 'backwards' to K', then K.
   In K, get a time-slice through the two histories, and infer the direction of the stick as seen in K.
  */
  private void stickHistory2() {
    //the stick is stationary in K''
    //a and b are the ends of the stick on the x-axis, from x=1 to x=2
    //here are their histories in K''
    History historyA = new StationaryParticle(1.0, 0.0, 0.0,  0.0, 1.0);
    History historyB = new StationaryParticle(2.0, 0.0, 0.0,  0.0, 1.0);
    
    //corner-boost from K'' to K
    double β1 = 0.8;
    double β2 = 0.6;
    //this is the inverse of other methods in this class
    CoordTransform cornerBoost = CoordTransformPipeline.join(
      Boost.alongThe(Axis.Y, -β2), //minus signs!
      Boost.alongThe(Axis.X, -β1)        
    );
    
    //time-slice 
    //find 2 events, one taken from each history, that have the same coord-time
    double τA = 0.9; //found by trial and error! proper times; these depend on the speeds chosen.
    double τB = 0.26;
    FourVector aK = cornerBoost.toNewFrame(historyA.event(τA));
    FourVector bK = cornerBoost.toNewFrame(historyB.event(τB));
    Util.log("event for end-a in K: " + aK + " one end of the stick, seen in K");
    Util.log("event for end-b in K: " + bK + " other end of the stick, seen in K");
    
    //find the angle
    //now we need the displacement between the two ends of the stick, and to figure out the 
    //angle the stick is making with the x axis (basic trig)
    FourVector stick = bK.minus(aK);
    Util.log("stick in K (b-a): "+stick + " has ct=0 (time-slice)") ;
    double angle = Math.atan2(stick.y(), stick.x());
    Util.log("Angle of stick w.r.t the X-axis in K: " + Util.radsToDegs(angle) + " degrees. Right direction, wrong amount."); //-38.6
    ShowEquivalence cb = new ShowEquivalence(Axis.Z, β1, β2);
    Util.log(" ");
    Util.log("Calculated θw directly:" + Util.radsToDegs(cb.equivalent().θw) + " degrees");
    Util.log("Calculated βdirection:" + Util.radsToDegs(cb.equivalent().βdirection) + " degrees");
  }

}
