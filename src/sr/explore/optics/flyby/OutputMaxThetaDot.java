package sr.explore.optics.flyby;

import static sr.core.Util.*;

/** 
 The fastest rate at which the star changes its theta angle.
 
 This output is meant for computing one stat from multiple fly-by's.
 
 <P>This stat depends on the minimum distance (impact parameter).
 The smaller the minimum distance, the higher the max value of theta-dot.

 <P>Example results:
 <pre>
  0.1   light years: Max theta-dot: 0.196  deg/day
  0.01  light years: Max theta-dot: 1.3554 deg/day
  0.001 light years: Max theta-dot: 4.8662 deg/day
 </pre> 
 <b>The typical Hollywood simulation showing stars whizzing by is unrealistic.</b>
 For comparison, the Moon moves about 13 deg/day with respect to the background stars.
 
 <P>Example scenario: flying through the thin disk of the Milky Way (95% of the stars are in the thin disk, not the thick disk), 
 in the solar neighbourhood. 
 A <em>rough</em> calculation of the probability of a close-approach to ANY star during the ENTIRE transit through the thin disk
 of the Milky Way:
 <pre>
  0.1   light-years: about 0.15     (occasional)
  0.01  light-years: about 0.0015   (rare)
  0.001 light-years: about 0.000015 (very rare, about 1 in 70,000) 
 </pre>
 
 <P>(These are overestimates, because they assume a uniform stellar density. 
 The stellar density perpendicular to the disk actually decreases with distance from the central plane of the Milky Way.)
 
 <P>In an extreme environment such as the center of a galaxy, the stellar density is much higher, and 
 close approaches would be much more common.
*/
final class OutputMaxThetaDot implements OutputSummary {
  
  OutputMaxThetaDot(){ }
  
  /** Here the per-fly-by maximum is computed. */
  @Override public void accept(DetectionEvent d) {
    if (count == 0) {
      //first detection for this flyby/object
      previousDetection = d;
    }
    else {
      //all other detections for this flyby/object
      double numerator = d.θ - previousDetection.θ;
      double denominator = d.detectionTime - previousDetection.detectionTime;
      double thetaDot = Math.abs(numerator / denominator);
      if (thetaDot > maxThetaDot) {
        maxThetaDot = thetaDot;
        maxDetection = d;
      }
    }
    ++count;
  }
  
  /** 
   Executed at the end of processing for each fly-by. 
   Here the global-maximum is re-computed. 
  */
  @Override public void render() {
    if (globalMaxThetaDot == 0) {
      //first flyby
      globalMaxThetaDot = maxThetaDot;
      globalMaxDetection = maxDetection;
    }
    else {
      //all subsequent fly-by's
      if (maxThetaDot > globalMaxThetaDot) {
        globalMaxThetaDot = maxThetaDot;
        globalMaxDetection = maxDetection;
      }
    }
  }
  
  /** Radians per year. */
  static Double globalMaxThetaDot() { return globalMaxThetaDot; }
  static DetectionEvent globalDetectionWithMaxThetaDot() { return globalMaxDetection; }
  
  static String globalMax() {
    double degsPerDay = radsToDegs(globalMaxThetaDot)/365.25;
    return "Max theta-dot: " + round(degsPerDay,4) + " deg/day, Detection: " + globalMaxDetection;
  }

  //PRIVATE 
  
  //the global maximum is separate from the per-flyby maximum
  
  private int count = 0;
  private DetectionEvent previousDetection;
  private DetectionEvent maxDetection;
  private Double maxThetaDot = 0.0;
  
  private static DetectionEvent globalMaxDetection;
  private static Double globalMaxThetaDot = 0.0;
}
