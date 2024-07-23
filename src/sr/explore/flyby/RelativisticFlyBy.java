package sr.explore.flyby;

import static sr.core.Util.log;
import static sr.core.Util.mustHave;

import sr.core.event.Event;
import sr.core.history.History;
import sr.core.history.UniformVelocity;
import sr.core.vector.Position;
import sr.core.vector.Velocity;

import static sr.core.Axis.*;

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
  (This distance is called the impact parameter.)
  <li>The star is at a given initial distance from the Y-axis.
 </ul>
 
  <P>All times are coordinate-times <em>ct</em> in the rest frame of the detector.
  <ul>
   <li>The time <em>ct=0</em> corresponds to the intersection of the star's path with the YZ-plane.
   <li>Negative <em>ct</em> corresponds to approach, and positive <em>ct</em> corresponds to recession.
  </ul> 
  
 <P>Input:
 <ul>
  <li>the basic spectral class of the main sequence star.
  <li>the speed of the star β (parallel to the positive X-axis).
  <li>the initial X-coordinate of the star.
  <li>the Y-coordinate of the star (the impact parameter), which remains fixed.
  <li>a (spatial) step-size to apply (in light-years). The corresponding time-step to apply is computed using this number. 
 </ul>
 
 <P>For each time step, the star is moved forward, emits a photon, and the photon is detected (at a later time)
 by the detector at the origin. The conditions at the detector are reported back to the caller, such that the results can be analyzed in different ways.
 
 <P>Output (all as functions of detection-time <em>ct</em> in years):
 <ul>
  <li>the detection-time <em>ct</em>.
  <li>the angle θ between the axis-of-motion of the star and the photon-direction at the detector.
  This angle starts out near zero, and increases towards 180 degrees. 
  Note that the photon-direction is opposite to the detector-direction.
  The angle θ also equals the angle between the detector-direction and the ray <em>opposite</em> to the star's axis-of-motion. 
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
    
    /*
    //compute a single scenario
    double speed = 0.9;
    double x0 = -25.0;
    double y = 10.0;
    RelativisticFlyBy2 flyby = new RelativisticFlyBy2(MainSequenceStar.A, speed, x0, y, 0.01);
    OutputSummary toConsole = new OutputToConsole();
    OutputSummary highlights = new OutputHighlights(flyby.star.name(), flyby.β(), flyby.y, false);
    OutputSummary[] outputs = {highlights};
    flyby.compute(outputs);
    
    for (OutputSummary output : outputs) {
      output.render();
    }
    */

    //compute a range of different scenarios
    Double x0 = -25.0;
    double spatialStep = 0.01; //light-years
    Double[] yValues = {1.0, 0.1};
    Double[] speeds = {0.87, 0.99};
    
    for(MainSequenceStar star : MainSequenceStar.values()) {
      for (Double speed : speeds) {
        for (Double y: yValues) {
          RelativisticFlyBy flyby = new RelativisticFlyBy(star, speed, x0, y, spatialStep);

          boolean CONSOLE_ONLY = true;
          boolean FILE_AND_CONSOLE = false;
          OutputSummary highlights = new OutputHighlights(flyby.star().name(), flyby.β(), flyby.startingDistance(), flyby.minimumDistance(), FILE_AND_CONSOLE);
          OutputSummary maxThetaDot = new OutputMaxThetaDot();
          
          flyby.compute(highlights, maxThetaDot);
          
          highlights.render();
          maxThetaDot.render();
        }
      }
    }
    log(OutputMaxThetaDot.globalMax());
  }
  
  /**
   Constructor.
   See the class comment for a description of the geometry/inertial frame used here. 
   
   <P>Unique units: years for time, and light-years for distance. 
   (Implementation note: light-years are needed only for the calculation of the star's visual magnitude <em>V</em>.)
    
   @param star the main sequence star's spectral class (and related data)
   @param β speed of the star along the positive X-axis (0..1)
   @param x0 initial distance from the star to the YZ-plane (negative)
   @param y the distance along the Y-axis at closest approach to the detector (the impact parameter; positive; light-years)
   @param spatialStep the spatial interval between computations; a corresponding time-step is computed.
  */
  public RelativisticFlyBy(MainSequenceStar star, Double β, Double x0, Double y, Double spatialStep) {
    mustHave(star != null, "Star must be present.");
    mustHave(β > 0, "β should be positive.");
    mustHave(β < 1, "β must be less than 1.0.");
    mustHave(x0 < 0, "Initial X coordinate should be negative.");
    mustHave(y > 0, "Impact parameter should be positive.");
    mustHave(spatialStep > 0, "Spatial-step must be positive.");
    this.star = star;
    this.β = β;
    this.x0 = x0;
    this.y = y;
    this.timeStep = spatialStep / β;
    this.history = UniformVelocity.of(Position.of(Y, y), Velocity.of(X, β)); 
  }
  
  /**
   Cycle through events in the history of the star, and compute how it looks to the detector at the origin.
   
   Algorithm: the star emits photons periodically (in coordinate-time).
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
    Event emissionEvent = emissionEventFor(time); //the first photon emitted
    int count = 0;
    while (count < NUM_EMISSION_EVENTS) {
      DetectionEvent detectionEvent = new DetectionEvent(emissionEvent, β, star);
      for(OutputSummary outputter: outputters) {
        outputter.accept(detectionEvent);
      }
      ++count;
      time = time + timeStep;
      emissionEvent = emissionEventFor(time);
    }
  }

  @Override public String toString() {
    return star.name() + "-type " + β + "c minimum-distance " + y;
  }
  
  Double β() {return β;}
  MainSequenceStar star() { return star;}
  Double minimumDistance() { return y; }
  Double startingDistance() { return x0; }
  
  // PRIVATE
  private MainSequenceStar star;
  /** [0..1) */
  private Double β;
  private Double y;
  private Double x0;
  private Double timeStep;
  private History history; 
  private static final int NUM_EMISSION_EVENTS = 10000;
  
  /**  ct=0 corresponds to the time when the star is at minimum distance. */
  private double initialTime() {
    return x0 / β; 
  }

  /** 
   An event in the history of the star, as a function of coordinate-time ct.
   IMPORTANT: the time t here is the time of EMISSION of a photon towards the detector.
   Light-travel time: the photon is ABSORBED at some LATER time, which is computed elsewhere. 
  */
  private Event emissionEventFor(Double emissionTime) {
   return history.event(emissionTime);
  }
}
