package sr.explore.flyby;

import static sr.core.Util.*;

import sr.core.Physics;
import sr.core.transform.FourVector;

/**
 <b>Relativistic fly-by of a star by a detector; runs the simulation.</b>
 
 In science fiction films, you often have a vehicle flying quickly through space. 
 Out the window, you see stars flying by, like fireflies in a field at night.
 There are many errors in such representations. 
 
 <P>This class investigates the case of a relativistic fly-by of a single star.
 In the inertial frame used here, the star moves relativistically with respect to a detector at rest at the origin.
 Here's the assumed geometry:
 <pre>
               ^ y
               | 
               |
 star --o- → ----- → -o----- → --o-- → "the axis of motion" 
               | 
            --detector-----x
               |
 </pre>
 
 <ul>
  <li>The star is modeled as a point light source with a black-body spectrum.
  <li>The star is a main sequence star. In its rest frame it has a given absolute temperature (which maps to a spectral class).
  <li>The star moves uniformly (in the positive x-direction) at a given speed β.
  <li>It passes by a single detector, placed at the origin of the coord system.
  <li>At closest approach, the star is at a given minimum-distance (along the positive y-axis) away from the detector.
 </ul>
 
 <P>Input:
 <ul>
  <li>the speed of the star β (along the positive x-axis)
  <li>the basic spectral class of the main sequence star.
  <li>the distance to the star at closest approach (the impact parameter, in light-years)
  <li>the initial x-coord x0 of the star (light-years). <em>See below for important info about this item!</em> 
  <li>a time-step to apply (in years)
 </ul>
 
 <P>For each time step, the star is moved forward, emits a photon, and the photon is detected (at a later time)
 by the detector at the origin. The conditions at the detector are reported back to the caller.
 
 <P>Output (all as functions of detection-time t in years):
 <ul>
  <li>the detection-time
  <li>the angle θ between the axis of motion of the star and the photon-direction at the detector.
  This angle starts out near zero, and increases towards 180 degrees. 
  Note that the photon-direction is opposite to the detector-direction.
  The angle θ also equals the angle between the detector-direction and the ray <em>opposite</em> to the star's axis of motion. 
  <li>the Doppler factor D (which comes from from β and θ)
  <li>the apparent temperature T of the star, which maps (roughly) to the color perceived by the human eye
  <li>the apparent visual magnitude of the star V
 </ul>
 
 <P>Items to note about these simulations:
  <ul>
   <li>the stars never stream by quickly, as seen in Hollywood animations. The motion is never that rapid (not even close). 
   <li>the great majority of stars are dim and red, and they aren't visible far away from the axis of motion. Too dim.
   (The Sun is a G-type star, and is brighter than an M-type star.)   
   <li>only the brightest (and rarest) stars remain visible far from the axis of motion.
  </ul>
*/
public final class RelativisticFlyBy {
  
  /**  Calculate a fly-by, and process the results. */
  public static void main(String... args) {
    log("Running...");
    
    double x0 = -30.0; //light-years in a REST FRAME! this item is different from the others!
    double timeStep = 0.01; //years
    
    /*
    RelativisticFlyBy flyby = new RelativisticFlyBy(MainSequenceStar.O, 0.87, 0.1, x0, timeStep);
    OutputFlyBy highlights = new OutputHighlights(flyby, false);
    flyby.compute(highlights);
    highlights.render();
    */
    
    Double[] minimumDistances = {1.0, 0.1, 0.1};
    Double[] speeds = {0.87, 0.99};
    for(MainSequenceStar star : MainSequenceStar.values()) {
      for (Double speed : speeds) {
        for (Double minimumDistance: minimumDistances) {
          RelativisticFlyBy flyby = new RelativisticFlyBy(star, speed, minimumDistance, x0, timeStep);
          
          OutputFlyBy highlights = new OutputHighlights(flyby, true);
          OutputFlyBy maxThetaDot = new OutputMaxThetaDot(flyby);
          
          flyby.compute(highlights, maxThetaDot);
          
          highlights.render();
          maxThetaDot.render();
        }
      }
    }
    log(OutputMaxThetaDot.globalMax());
    log("Done.");
  }
  
  /**
   Constructor.
   See the class comment for a description of the geometry/inertial frame used here. 
   
   <P>Unique Units: years for time, and light-years for distance. 
   (Implementation note: it's only the calc for finding V the visual mag that needs light-years.)
    
   @param β speed of the star along the positive x-axis (0..1)
   @param star the main sequence star's spectral class (and related data)
   @param minimumDistance the distance along the y-axis at closest approach to the detector (the impact parameter; positive; light-years)
   
   @param x0 the initial x-coord (at t=0) of the star (negative, light-years), AS MEASURED IN A FRAME IN WHICH THE STAR IS AT REST. 
   IMPORTANT/TRICKY: this param is Lorentz-contracted by this implementation. 
   If a stick has a rest-length of x0, it has a moving-length of x0/gamma. 
   This makes it easier to compare results at different speeds, because the geometry represents the same setup (but measured in different 
   inertial frames with different boosts).
   
   @param timeStep time between successive photon-emission events (years). 
   The fly-by is traced out by incrementing the time by this amount, and calculating the 
   position at which a "new photon" is emitted towards the detector, where it is detected at a later time. 
  */
  public RelativisticFlyBy(MainSequenceStar star, Double β, Double minimumDistance, Double x0, Double timeStep) {
    this.β = β;
    this.star = star;
    this.minimumDistance = minimumDistance;
    this.x0 = x0/Physics.Γ(β); //Lorentz contraction!
    this.timeStep = timeStep;
    validate();
  }
  
  /**
   Cycle through the events in the history of the star, and compute how it looks to the detector at the origin.
   
   Algorithm: the star emits photons periodically (timeStep in coord-time).
   The photon is then detected some time later by the detector at the origin, where a correction 
   to its direction is applied to get the detector-direction (aberration) with respect to the boost-direction.
   
   @param consumers how to process the computed results 
  */
  public void compute(OutputFlyBy... consumers) {
    /*
     Start with an emission, and compute the light-travel time to the detector. 
     In effect, this computes the intersection of the photon with the past light cone of the detector.
     Over time, this intersection point moves over a 'hump' on the past light cone of the detector.
    */
    double initialTime = initialTime();
    FourVector emission = star(initialTime);
    int count = 0;
    while (count < 10000) {
      Detection detection = new Detection(emission, β, star);
      for(OutputFlyBy consumer: consumers) {
        consumer.accept(detection);
      }
      ++count;
      emission = star(initialTime + count * timeStep);
    }
  }

  @Override public String toString() {
    return star.name() + "-type " + β + "c minimum-distance " + minimumDistance;
  }
  
  Double β() {return β;}
  MainSequenceStar star() { return star;}
  Double minimumDistance() { return minimumDistance; }
  
  // PRIVATE
  
  /** [0..1) */
  private Double β;
  private MainSequenceStar star;
  private Double minimumDistance;
  private Double x0;
  private Double timeStep;
  
  private void validate() {
    mustHave(β > 0, "β should be positive");
    mustHave(star != null, "Star must be present");
    mustHave(minimumDistance > 0, "Minimum distance should be positive");
    mustHave(x0 < 0, "Initial x-coord should be negative");
    mustHave(timeStep > 0, "Time-step must be positive");
  }

  /** 
   t=0 is taken simply as the time when the star is at x0.
   This time of course differs from the time that event is detected by the detector at the origin.
  */
  private double initialTime() {
    return 0; 
  }

  /** 
   An event in the history of the star, as a function of coord-time t.
   Uniform motion in a line, parallel to the x-axis.
   IMPORTANT: the time t here is the time of EMISSION of a photon towards the detector.
   Light-travel time: the photon is ABSORBED at some LATER time, which is computed elsewhere. 
  */
  private FourVector star(Double emissionTime) {
   double x = x0/*Lorentz-contracted!*/ + β * emissionTime;
   FourVector result = FourVector.from(emissionTime, x, minimumDistance, 0.0);
   return result;
  }
}
