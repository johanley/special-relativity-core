package sr.explore.stickonlightcone;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import sr.core.Axis;
import sr.core.EventFinder;
import sr.core.Physics;
import sr.core.Speed;
import sr.core.Util;
import sr.core.history.History;
import sr.core.history.StationaryParticle;
import sr.core.transform.Boost;
import sr.core.transform.CoordTransform;
import sr.core.transform.FourVector;

/**
 Intersection of the light-cone of a detector with the history of a stick.
 
 <P>This exhibits two effects:
 <ul>
  <li>flattening (Lorentz-Fitzgerald contraction)
  <li>light-cone slicing of a history
 </ul>

 <P>Very odd: in the backward direction, the light-slice flattening is increased.
 In the forward direction, the light-slice flattening is <em>reversed</em> into an expansion.
 At 0.9, the light-slice length is 4.4x the rest length, but the time-slice length 
 is 0.14x the rest length.
 
 <P>Receding: the stick's light-slice length is always less than the L-F contracted length.
 In the ultra-relativistic limit, the light-slice length is exactly half the L-F contracted length. 

<P>Approaching: the stick's light-slice is always longer than the rest-length.  
 This case is special because of scissor effects. 
 Geometrically, in ultra-relativistic cases, the detector needs to be very distant from the stick.
 In this implementation, I simply hard-code the geometry, and restrict the maximum speed of approach.
 This suffices to see the effect, without worrying about extreme speeds.
*/
public final class StickOnLightCone {
  
  public static void main(String[] args) {
    List<String> details = new ArrayList<>();
    List<String> summary = new ArrayList<>();
    StickOnLightCone stick = new StickOnLightCone();
    //stick.recede(0.995, lines);
    //stick.approach(-0.02, details); //-0.91 fails because of hard-coded geometry
    stick.recession(details, summary);
    stick.approach(details, summary);
    for(String line : summary) {
      System.out.println(line);
    }
    Util.writeToFile(StickOnLightCone.class, "stick-on-light-cone.txt", summary);
  }
  
  void recession(List<String> detail, List<String> summary) {
    summary.add("Recession: len is always less than the L-F contraction.");
    summary.add("At very high β it's half the L-F length.");
    summary.add("β  light-slice-length  L-F length");
    summary.add("----------------------------------");
    for (Speed speed : Speed.nonExtremeValues()) {
      StickOnLightCone stick = new StickOnLightCone();
      double length = stick.recede(speed.β(), detail);
      summary.add(speed.β() + " " + length + " " + 1.0/Physics.Γ(speed.β()));
    }
  }
  
  void approach(List<String> detail, List<String> summary) {
    summary.add("");
    summary.add("Approach: len is always greater than the rest-length.");
    summary.add("β  light-slice-length  rest-length");
    summary.add("----------------------------------");
    for (Speed speed : Speed.nonExtremeValues()) {
      if (speed.β() < 0.91) {
        StickOnLightCone stick = new StickOnLightCone();
        double length = stick.approach(-speed.β(), detail);
        summary.add(speed.β() + " " + length + " 1.0");
      }
    }
  }
  
  
  /**
   Find the apparent length of the stick receding from a detector.
   Use the intersection of the stick's history with the detector's past light-cone.
   
   <P>The stick is at rest in K. K' is boosted along the X-axis of K by β. 
   In K', after ct'=0, the stick is receding from the detector.  

   @param β the speed of the boost from K to K'. Positive here!
  */
  double recede(double β, List<String> lines) {
    Util.mustHave(β > 0, "β must be positive in this case, because the geometry is hard-coded.");
    lines.add("Speed of recession β: " + β);
    //in frame K, the stick is represented with 2 histories, one for each end of the stick, A and B (stationary in K)
    //the stick-end A at the origin, and stick-end B along the X-axis at x=1
    History histA = new StationaryParticle(0.0, 0.0, 0.0,   -10.0, 10.0); 
    History histB = new StationaryParticle(1.0, 0.0, 0.0,   -10.0, 10.0); 
    double restLength = histB.event(0.0).minus(histA.event(0.0)).spatialMagnitude();
    lines.add("K: Rest-length of the stick: " + restLength);

    //K to K': boost along the X-axis at the given speed
    //in K', the stick is receding at speed β in the negative-x direction
    CoordTransform boostX = Boost.alongThe(Axis.X, β);
    FourVector detectorEv = detectionEventReceding(lines);
    FourVector a = eventOnPastLightConeOf(detectorEv, histA, boostX, lines);
    FourVector b = eventOnPastLightConeOf(detectorEv, histB, boostX, lines);
    //now infer the apparent geometry from the events on the light-cone
    FourVector displacement = b.minus(a);
    double apparentLength = displacement.spatialMagnitude();
    lines.add("K': Apparent length of the stick (light-cone slice): " + apparentLength);
    lines.add("K': Computed length from Lorentz-Fitzgerald contraction (time-slice): " + restLength/Physics.Γ(β));
    return apparentLength;
  }

  /** 
   Detection-event at the detector. The past light-cone of the detector emanates from this event.
   CAREFUL: the detector event's past light cone needs to slice the sticks history in the desired way, 
   for all values of β! No monkey business with 'bad geometry'!
  */
  private FourVector detectionEventReceding(List<String> lines) {
    //in the boosted frame K', there's a detector to the right of the origin, looking at the history of the stick
    //it's off to the right in order to ensure that the past light cone always slices the history of the stick correctly
    History detector = new StationaryParticle(2.0, 0.0, 0.0,   0.0, 10.0); 
    //let's look at the past light-cone of this event on the detector's history
    double τ = 5.0;
    FourVector result = detector.event(τ);
    lines.add("K': Detection event : " + result);
    return result;
  }

  /** Find an event from the stick's history that's on the past light-cone of the detection-event */
  private FourVector eventOnPastLightConeOf(FourVector detection, History history, CoordTransform transform, List<String> lines) {
    double CLOSE_ENOUGH =  0.00001;
    Function<FourVector, Double> onTheLightCone = event -> (
        detection.minus(transform.toNewFrame(event)).magnitudeSq()
     );
    EventFinder root = new EventFinder(history, onTheLightCone, CLOSE_ENOUGH);
    double τA = root.searchWithNewtonsMethod(CLOSE_ENOUGH);
    FourVector result = transform.toNewFrame(history.event(τA));
    lines.add("K': Event on light cone: " + result);
    return result;
  }
  
  
  /**
   There's asymmetry between the approaching case and the receding case.
   Only the approaching case has the scissors-effect.
   This means the detector needs to be distant from the origin, in the geometry used here.
    
   @param β the speed of the boost from K to K'. Range -0.90..0.00 because of hard-coded geometry.
  */
  double approach(double β, List<String> lines) {
    Util.mustHave((β <= 0 && β >= -0.90), "β must be in the range -0.90..0 in this case, because the geometry is hard-coded.");
    lines.add("Speed of approach β: " + β);
    //in frame K, the stick is represented with 2 histories, one for each end of the stick, A and B (stationary in K)
    //the stick-end A at the origin, and stick-end B along the X-axis at x=1
    History histA = new StationaryParticle(0.0, 0.0, 0.0,   0.0, 100.0); 
    History histB = new StationaryParticle(1.0, 0.0, 0.0,   0.0, 100.0); 
    double restLength = histB.event(0.0).minus(histA.event(0.0)).spatialMagnitude();
    lines.add("K: Rest-length of the stick: " + restLength);

    //K to K': boost along the X-axis at the given speed
    //in K', the stick is receding at speed β in the negative-x direction
    CoordTransform boostX = Boost.alongThe(Axis.X, β);
    FourVector detectorEv = detectionEventApproaching(lines);
    FourVector a = eventOnPastLightConeOf(detectorEv, histA, boostX, lines);
    FourVector b = eventOnPastLightConeOf(detectorEv, histB, boostX, lines);
    //now infer the apparent geometry from the events on the light-cone
    FourVector displacement = b.minus(a);
    double apparentLength = displacement.spatialMagnitude();
    lines.add("K': Apparent length of the stick (light-cone slice): " + apparentLength);
    lines.add("K': Computed length from Lorentz-Fitzgerald contraction (time-slice): " + restLength/Physics.Γ(β));
    return apparentLength;
  }
  
  private FourVector detectionEventApproaching(List<String> lines) {
    //in the boosted frame K', there's a detector to the right of the origin, looking at the history of the stick
    //it's off to the right in order to ensure that the past light cone always slices the history of the stick correctly
    History detector = new StationaryParticle(50.0, 0.0, 0.0,   0.0, 55.0); 
    //let's look at the past light-cone of this event on the detector's history
    double τ = 55.0;
    FourVector result = detector.event(τ);
    lines.add("K': Detection event : " + result);
    return result;
  }
}
