package sr.explore.flyby;

import static sr.core.Util.radsToDegs;

import sr.core.Physics;
import sr.core.transform.FourVector;

/** 
 The detection of a photon at the detector.
 Calculate items that change during the fly-by.
*/
final class Detection {
  
  /** The <b>core calculation</b> is done by this constructor. */
  Detection(FourVector emissionEvent, Double β, MainSequenceStar star){
    double distanceToDetector = emissionEvent.spatialMagnitude(); 
    double lightTravelTime = distanceToDetector / 1.0; //c=1 here; no other value will do
    double cosTheta = emissionEvent.x() / distanceToDetector;
    double theta = Math.PI - Math.acos(cosTheta) /*0..pi*/; //the photon-direction wrt "the axis"; 'geometrical' position
    this.θ = Physics.aberrationForDetectorDirection(theta, β);  //the observed apparent position, with aberration applied
    this.D = Physics.D(β, θ);
    this.T = Physics.T(D, star.surfaceTemp());
    this.V = Physics.apparentVisualMagnitude(star.absoluteMag(), distanceToDetector /*light-years!*/) + Physics.Δmag(D, star.surfaceTemp());
    this.detectionTime = emissionEvent.t() + lightTravelTime;
  }
  
  /** 
   The detector-direction (pointing to the star), with respect to the boost-direction (of the inertial frame with respect to the star!). 
   Radians, [0,pi]. 
  */
  double θ;
  
  /** The Doppler factor. Dimensionless. */
  double D;
  
  /** The effective temperature of the star's black-body spectrum in Kelvins. Doppler-shifted. */
  double T;
  
  /** The apparent visual magnitude. */
  double V;
  
  /** When the photon is detected.*/
  double detectionTime; 
  
  @Override public String toString() {
    //%[argument_index$][flags][width][.precision]conversion
    return String.format("%1$5.3f %2$5.2f %3$5.2f %4$5.2f ", detectionTime, radsToDegs(θ), D, V);
  }
}
