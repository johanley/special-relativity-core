package sr.explore.optics.flyby;

import static sr.core.Util.radsToDegs;

import sr.core.Axis;
import sr.core.component.Event;
import sr.core.component.ops.Sense;
import sr.core.vec3.NDelta;
import sr.core.vec3.NDirection;
import sr.core.vec3.NPhaseGradient;
import sr.core.vec3.NVelocity;
import sr.core.vec4.NFourDelta;
import sr.core.vec4.NFourPhaseGradient;

/** 
 The detection of a photon at the detector.
 Most of the physics for a flyby is implemented in the class constructor.
 
 <P>This data is attached to two frames:
  <ul>
   <li>K, in which the detector is at rest with respect to the star.
   <li>K', in which the detector is moving with respect to the star.
  </ul>
*/
final class DetectionEvent {
  
  /** The <b>core calculation</b> is done by this constructor. */
  DetectionEvent(Event emissionEvent, Double β, MainSequenceStar star){
    this.emissionTime = emissionEvent.ct();
    this.distanceToEmissionEvent = NFourDelta.withRespectToOrigin(emissionEvent).spatialMagnitude();
    
    //c=1 here; no other value will do
    double lightTravelTime = distanceToEmissionEvent / 1.0;
    this.detectionTime = emissionEvent.ct() + lightTravelTime;
    
    //calculate and set D and θ: 
    boostTheWaveVectorComingFromThe(emissionEvent, -β);
    
    /*
    this.T = Star.T(D, star.surfaceTemperature());
    double magDistanceEffect = Star.apparentVisualMagnitude(star.absoluteMagnitude(), distanceToEmissionEvent);
    double ΔmagDopplerEffect = Star.Δmag(D, star.surfaceTemperature());
    */
    this.T = star.surfaceTemperature(D);
    double magDistanceEffect = star.apparentVisualMagnitude(distanceToEmissionEvent /*light-years needed!*/);
    double ΔmagDopplerEffect = star.Δmagnitude(D);
    this.V = magDistanceEffect + ΔmagDopplerEffect;
  }
  
  /**  The K' detector-direction (pointing to the star), with respect to the axis-of-motion.  Radians, [0,pi].  */
  double θ;
  
  /** The K' Doppler factor. Dimensionless. */
  double D;
  
  /** The K' effective temperature of the star's black-body spectrum in Kelvins. */
  double T;
  
  /** The K' apparent visual magnitude of the star seen by the detector. */
  double V;
  
  /** The K coordinate-time when the photon is detected. Time-of-emission + travel-time for the photon. */
  double detectionTime; 
  
  /** The K coordinate-time when the photon was emitted. */
  double emissionTime;
  
  /** The K distance from the star to the detector when the photon is emitted.  */
  double distanceToEmissionEvent;
  
  @Override public String toString() {
    //%[argument_index$][flags][width][.precision]conversion
    // '%1$8.3f' means 8 chars, 3 decimals (floating point data)
    return String.format("%1$8.3fy %2$8.2f° %3$8.2f  %4$8.2fV  %5$8.3fly", detectionTime, radsToDegs(θ), D, V, distanceToEmissionEvent);
  }
  
  /** 
   Boost a {@link NFourPhaseGradient} using a boost along the X axis by β.
   Sets both this.θ and this.D as a side effect.  
  */
  private void boostTheWaveVectorComingFromThe(Event emissionEvent, double β) {
    //in K, the detector is at rest with respect to the star, and at the origin of the coordinate system
    NDirection detector_direction_K = NDirection.of(NDelta.withRespectToOrigin(emissionEvent.position()));

    //photon-direction is opposite to the detector-direction
    //do the calc with a photon, then convert back to the detector's perspective
    NDirection photon_direction = NDirection.of(detector_direction_K.times(-1));
    NFourPhaseGradient photon_K = NFourPhaseGradient.of(NPhaseGradient.of(1.0, photon_direction));
    NFourPhaseGradient photon_Kp = photon_K.boost(NVelocity.of(β, Axis.X), Sense.ChangeGrid);
    
    this.D = photon_Kp.ct() / photon_K.ct();
    
    NDirection detector_direction_Kp = NDirection.of(photon_Kp.spatialComponents().times(-1));
    double angle =  Math.atan2(detector_direction_Kp.y(), detector_direction_Kp.x()); //0 to +pi wrt +X axis
    this.θ =  Math.PI - angle; //0..pi wrt -X axis
  }
}
