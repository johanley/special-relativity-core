package sr.explore.optics.flyby;

import static sr.core.Util.radsToDegs;

import sr.core.Physics;
import sr.core.event.Event;

/** 
 The detection of a photon at the detector.
 Most of the physics for a flyby is implemented in the class constructor.
 
 <P>Physical quantities are in the rest frame of the detector.
*/
final class DetectionEvent {
  
  /** The <b>core calculation</b> is done by this constructor. */
  DetectionEvent(Event emissionEvent, Double β, MainSequenceStar star){
    this.emissionTime = emissionEvent.ct();
    this.distanceToEmissionEvent = emissionEvent.spatialMagnitude();
    
    //c=1 here; no other value will do
    double lightTravelTime = distanceToEmissionEvent / 1.0;
    this.detectionTime = emissionEvent.ct() + lightTravelTime;
    
    double cosTheta = emissionEvent.x() / distanceToEmissionEvent;
    //the photon-direction with respect to the axis-of-motion; the 'geometrical' direction
    double angle = Math.PI - Math.acos(cosTheta) /*0..pi*/; 
    //the observed apparent direction, with aberration applied
    this.θ = Physics.aberrationForDetectorDirection(angle, β);
    
    this.D = Physics.D(β, θ);
    this.T = Physics.T(D, star.surfaceTemp());
    double magDistanceEffect = Physics.apparentVisualMagnitude(star.absoluteMag(), distanceToEmissionEvent /*light-years!*/);
    double ΔmagDopplerEffect = Physics.Δmag(D, star.surfaceTemp());
    this.V = magDistanceEffect + ΔmagDopplerEffect;
  }
  
  /** 
   The detector-direction (pointing to the star), with respect to the axis-of-motion. 
   Radians, [0,pi]. 
  */
  double θ;
  
  /** The Doppler factor. Dimensionless. */
  double D;
  
  /** The effective temperature of the star's black-body spectrum in Kelvins. Doppler-shifted. */
  double T;
  
  /** The apparent visual magnitude of the star seen by the detector. */
  double V;
  
  /** The coordinate-time when the photon is detected. Time-of-emission + travel-time for the photon. */
  double detectionTime; 
  
  /** The coordinate-time when the photon was emitted. */
  double emissionTime;
  
  /** How far away the star was from the detector when it emitted the photon. */
  double distanceToEmissionEvent;
  
  @Override public String toString() {
    //%[argument_index$][flags][width][.precision]conversion
    // '%1$8.3f' means 8 chars, 3 decimals (floating point data)
    return String.format("%1$8.3fy %2$8.2f° %3$8.2f  %4$8.2fV  %5$8.3fly", detectionTime, radsToDegs(θ), D, V, distanceToEmissionEvent);
  }
}
