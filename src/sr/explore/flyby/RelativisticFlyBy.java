package sr.explore.flyby;

import static sr.core.Util.log;
import static sr.core.Util.mustHave;

import sr.core.Physics;
import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.FourVector;

/**
 <b>Relativistic fly-by of a star by a detector.</b>
 
 In science fiction films, you often have a spacecraft moving quickly through space. 
 Out the window, you see stars flying by like fireflies in a field at night.
 There are many errors in such representations. 
 
 <P>This class investigates the case of a relativistic fly-by of a single star.
 In the inertial frame used here, the star moves relativistically with respect to a detector at rest at the origin.
 Here's the assumed geometry:
 <pre>
               ^ Y-axis
               | 
               |
 star --o- → ----- → -o----- → --o-- → "the axis of motion" 
               | 
            --detector-----> X-axis
               |
 </pre>

 <P>
 <ul>
  <li>The star is modeled as a point light source having a black-body spectrum.
  <li>The star is a main sequence star. In its rest frame it has a given absolute temperature, which maps to a spectral class.
  <li>The star moves uniformly (in the positive X-direction) at a given speed β.
  <li>It passes by a single detector, placed at the origin of the coordinate system.
  <li>At closest approach, the star is at a given minimum-distance (along the positive Y-axis) away from the detector.
 </ul>
 
  <P>All times are in the rest frame of the detector.
  
  <P>There is one physical quantity that is NOT in the rest frame of the detector.
  Imagine a frame in which the star is at rest, and the detector is approaching.
  Now imagine many detectors approaching the star at different speeds, but have them all match up at a certain moment, at a certain initial distance.
  It's this initial distance that is passed as the <code>x0prime</code> parameter to the constructor .

 <P>Input:
 <ul>
  <li>the speed of the star β (along the positive X-axis).
  <li>the basic spectral class of the main sequence star.
  <li>the distance to the star at closest approach (the impact parameter, in light-years).
  <li>the initial separation between the star and the detector in light-years, as measured in the rest frame of the star (not the rest frame of the detector).
  <em>See below for important info about this item!</em> 
  <li>a time-step to apply (in years). 
 </ul>
 
 <P>For each time step, the star is moved forward, emits a photon, and the photon is detected (at a later time)
 by the detector at the origin. The conditions at the detector are reported back to the caller, such that the results can be analyzed in different ways.
 
 <P>Output (all as functions of detection-time t in years):
 <ul>
  <li>the detection-time.
  <li>the angle θ between the axis of motion of the star and the photon-direction at the detector.
  This angle starts out near zero, and increases towards 180 degrees. 
  Note that the photon-direction is opposite to the detector-direction.
  The angle θ also equals the angle between the detector-direction and the ray <em>opposite</em> to the star's axis of motion. 
  <li>the Doppler factor D (which comes from from β and θ).
  <li>the apparent temperature T of the star, which maps (roughly) to the color perceived by the human eye.
  <li>the apparent visual magnitude of the star V.
 </ul>
 
 <P>Items to note about these simulations (which assume high values for β):
  <ul>
   <li>the stars never stream by quickly, as seen in Hollywood animations. The motion is never that rapid (not even close). 
   <li>the great majority of stars are dim and red, and they aren't visible far away from the axis of motion, because they are too 
   dim for the human eye. (The Sun is a G-type star, and is brighter than an M-type star.)   
   <li>only the brightest (and rarest) stars remain visible far from the axis of motion.
  </ul>
  
 <P>Reference:
  <em>In search of the "starbow": The appearance of the starfield from a relativistic spaceship</em>, 
  John M. McKinley and Paul Doherty, 
  American Journal of Physics, 47, 309 (1979).
*/
public final class RelativisticFlyBy {
  
  /**  Calculate a fly-by, and process the results. */
  public static void main(String... args) {
    double x0prime = -30.0; //light-years in the rest frame of the STAR, not the detector; this item is different from the others!
    double timeStep = 0.01; //years
    
    //compute a single scenario
    RelativisticFlyBy flyby = new RelativisticFlyBy(MainSequenceStar.A, 0.90, 10.0, x0prime, timeStep);
    OutputSummary toConsole = new OutputToConsole();
    OutputSummary highlights = new OutputHighlights(flyby.star().name(), flyby.β(), flyby.minimumDistance, true);
    OutputSummary[] outputs = {highlights};
    flyby.compute(outputs);
    
    for (OutputSummary output : outputs) {
      output.render();
    }

    //compute a range of different scenarios
    /*
    Double[] minimumDistances = {1.0, 0.1};
    Double[] speeds = {0.87, 0.99};
    for(MainSequenceStar star : MainSequenceStar.values()) {
      for (Double speed : speeds) {
        for (Double minimumDistance: minimumDistances) {
          RelativisticFlyBy flyby = new RelativisticFlyBy(star, speed, minimumDistance, x0prime, timeStep);
          
          OutputSummary highlights = new OutputHighlights(flyby.star().name(), flyby.β(), flyby.minimumDistance(), true);
          OutputSummary maxThetaDot = new OutputMaxThetaDot();
          
          flyby.compute(highlights, maxThetaDot);
          
          highlights.render();
          maxThetaDot.render();
        }
      }
    }
    log(OutputMaxThetaDot.globalMax());
    */
  }
  
  /**
   Constructor.
   See the class comment for a description of the geometry/inertial frame used here. 
   
   <P>Unique units: years for time, and light-years for distance. 
   (Implementation note: light-years are needed only for the calculation of the star's visual magnitude <em>V</em>.)
    
   @param β speed of the star along the positive X-axis (0..1)
   @param star the main sequence star's spectral class (and related data)
   @param minimumDistance the distance along the Y-axis at closest approach to the detector (the impact parameter; positive; light-years)
   
   @param x0prime the initial X-coordinate (at t=0) of the star (negative, light-years), AS MEASURED IN A FRAME IN WHICH THE STAR IS AT REST (hence the 'prime'). 
   IMPORTANT TRICK: this parameter is flattened (Lorentz-Fitzgerald contraction) using the given β.
   This trick ensures that the initial distance between the star and the detector, AS SEEN IN THE REST FRAME OF THE STAR, is the same no matter
   what the speed of the detector. This makes it easier to compare different scenarios.
   (In the rest frame of the star, imagine N detectors initially at the same location, but having different speeds.) 
   
   @param timeStep time between successive photon-emission events (years). 
   The fly-by is traced out by incrementing the time by this amount, and calculating the 
   position at which a "new photon" is emitted towards the detector, where it is detected at some later time. 
  */
  public RelativisticFlyBy(MainSequenceStar star, Double β, Double minimumDistance, Double x0prime, Double timeStep) {
    this.β = β;
    this.star = star;
    this.minimumDistance = minimumDistance;
    this.x0 = x0prime/Physics.Γ(β); //flattening!
    this.timeStep = timeStep;
    validate();
  }
  
  /**
   Cycle through the events in the history of the star, and compute how it looks to the detector at the origin.
   
   Algorithm: the star emits photons periodically (<em>timeStep</em> in coordinate-time).
   The photon is then detected some time later by the detector at the origin, where a correction 
   to its direction is applied to get the detector-direction (aberration) with respect to the boost-direction.
   
   @param outputters how to process the computed results 
  */
  public void compute(OutputSummary... outputters) {
    /*
     Start with an emission, and compute the light-travel time to the detector. 
     In effect, this computes the intersection of an event in the photon's history with the past light cone of the detector.
     Over time, this intersection point moves over a 'hump' on the past light cone of the detector, where the top 
     of the hump corresponds to the minimum distance.
    */
    double time = initialTime();
    FourVector emissionEvent = star(time); //the first photon emitted
    int count = 0;
    
    while (count < NUM_EMISSION_EVENTS) {
      DetectionEvent detectionEvent = new DetectionEvent(emissionEvent, β, star);
      for(OutputSummary outputter: outputters) {
        outputter.accept(detectionEvent);
      }
      ++count;
      time = time + timeStep;
      emissionEvent = star(time);
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
  private static final int NUM_EMISSION_EVENTS = 10000;
  
  private void validate() {
    mustHave(β > 0, "β should be positive");
    mustHave(star != null, "Star must be present");
    mustHave(minimumDistance > 0, "Minimum distance should be positive");
    mustHave(x0 < 0, "Initial X-coord should be negative");
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
   An event in the history of the star, as a function of coordinate-time t.
   Uniform motion in a line, parallel to the X-axis.
   IMPORTANT: the time t here is the time of EMISSION of a photon towards the detector.
   Light-travel time: the photon is ABSORBED at some LATER time, which is computed elsewhere. 
  */
  private FourVector star(Double emissionTime) {
   double x = x0/*Lorentz-contracted!*/ + β * emissionTime;
   FourVector result = FourVector.from(emissionTime, x, minimumDistance, 0.0, ApplyDisplaceOp.YES);
   return result;
  }
}
