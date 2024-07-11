package sr.explore.lightsliceofastick;

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
import sr.explore.output.text.Table;
import sr.explore.output.text.TextOutput;

/**
 Intersection of the light-cone of a detector with the history of a moving stick.
 
 <P>Two distinct effects are combined here:
 <ul>
  <li>flattening (the Lorentz-Fitzgerald contraction)
  <li>how the light-cone slices across a history
 </ul>
 
 <P>This class explores only the approaching and receding cases, with no transverse motion.
 <P>The stick always points in its direction of motion.

 <P>Implementation note: the numeric details of the history of the stick and the history of the detector
 need to be such that the light-slice emanating from an event on the detector's history does indeed intersect 
 with the history of the stick. 
*/
public final class LightSliceOfAStick extends TextOutput {
  
  public static void main(String[] args) {
    LightSliceOfAStick stickOnLightCone = new LightSliceOfAStick();
    stickOnLightCone.explore();
  }
  
  void explore() {
    recession();
    approach();
    outputTo(OUTPUT_FILE_NAME);
  }
  
  /**
   Receding from the detector: the stick's light-slice length is always less than the time-slice length.
   In the ultra-relativistic limit, the light-slice length approaches exactly half the time-slice contracted length. 
  */
  void recession() {
    lines.add("Intersection of the light-cone of a detector with the history of a stick (of unit length)." + Util.NL);
    lines.add("1. The stick is RECEDING from the detector, and pointing in its direction of motion.");
    lines.add("The light-slice length is always less than the time-slice length.");
    lines.add("A β approaches 1.0, the light-slice length asymptotically approaches 50% of the time-slice length." + Util.NL);
    lines.add(table.row("stick recession speed", "light-slice", "time-slice"));
    lines.add(table.row("β", "length", "length"));
    lines.add(Util.separator(DASHES));
    for (Speed speed : Speed.nonExtremeValues()) {
      double length = apparentStickLength(speed.β(), DETECTION_EVENT);
      lines.add(table.row(speed.β(), round(length), round(1.0/Physics.Γ(speed.β()))));
    }
  }

  /**
   Approaching the detector: the stick's light-slice is always longer than the rest-length.  
   This case is different because of 'scissor' effects. 
  */
  void approach() {
    lines.add(Util.NL + "2. The stick is APPROACHING the detector, and pointing in its direction of motion.");
    lines.add("The light-slice length is always greater than the rest-length.");
    lines.add("This demonstrates the 'scissors effect' between a past light cone and the history of an approaching object." + Util.NL);
    lines.add(table.row("stick approach speed", "light-slice", "rest"));
    lines.add(table.row("β", "length", "length"));
    lines.add(Util.separator(DASHES));
    for (Speed speed : Speed.nonExtremeValues()) {
      //because of the histories I've defined here, the past light cone of the detector doesn't intersect the stick's 
      //history in the most extreme cases
      if (speed.β() < 0.9999) {
        double length = apparentStickLength(-speed.β(), DETECTION_EVENT);
        lines.add(table.row(speed.β(), round(length), 1.0));
      }
    }
  }
  
  private Table table = new Table("%-26s", "%-18s", "%-18s");
  private static final String OUTPUT_FILE_NAME = "light-slice-of-a-stick.txt";
  private static final int DASHES = 60;

  private static final double NEARBY = 1.0;
  private static final double DISTANT = 100.0;
  private static final double MAX_TAU = 1000.0;
  
  /**
   In frame K, the stick is represented with 2 histories, one for each end of the stick, A and B (stationary in K).
   The stick-end A is at the origin, and the stick-end B along the X-axis at x=1.
  */
  private static final History HIST_STICK_END_A = new StationaryParticle(0.0, 0.0, 0.0,   0.0, MAX_TAU); 
  private static final History HIST_STICK_END_B = new StationaryParticle(NEARBY, 0.0, 0.0,   0.0, MAX_TAU); 
  
  /** 
   Detection-event on the history of the detector.
   The detector is placed "off to the right", distant from the stick.
   The detection event itself is "up at the top", to ensure its past light cone indeed intersects with the 
   stick's history in all cases used here.  
  */
  private static final FourVector DETECTION_EVENT =  new StationaryParticle(DISTANT, 0.0, 0.0,   0, DISTANT).event(DISTANT);

  
  /**
   Find the apparent length of the stick.
   Use the intersection of the stick's history with the past light-cone of an event 
   from the detector's history.
   
   <P>The stick is at rest in K, from the origin to (X,Y,Z) = (1,0,0). 
   K' is boosted along the X-axis of K by β. 
   In K', after ct'=0, the stick is receding from the detector.  

   @param β the speed of the boost from K to K'. Positive for the stick receding from the detector, negative for approaching it.
   @return the apparent length of the stick, as seen by the light-slice.
  */
  private double apparentStickLength(double β, FourVector theDetectionEvent) {
    //K to K': boost along the X-axis at the given speed
    //in K', the stick is receding at speed β in the negative-X direction
    CoordTransform boostX = Boost.alongThe(Axis.X, β);
    FourVector eventA = eventOnPastLightConeOf(theDetectionEvent, HIST_STICK_END_A, boostX);
    FourVector eventB = eventOnPastLightConeOf(theDetectionEvent, HIST_STICK_END_B, boostX);
    //now infer the apparent length of the stick from this pair of events on the past light-cone of the detector
    return eventB.minus(eventA).spatialMagnitude();
  }

  /** Find an event from the stick's history that's on the past light-cone of the detection-event. */
  private FourVector eventOnPastLightConeOf(FourVector detection, History history, CoordTransform transform) {
    double CLOSE_ENOUGH =  0.00001;
    Function<FourVector, Double> onTheLightCone = event -> (
        detection.minus(transform.toNewFrame(event)).magnitudeSq()
     );
    EventFinder root = new EventFinder(history, onTheLightCone, CLOSE_ENOUGH);
    double τA = root.searchWithNewtonsMethod(CLOSE_ENOUGH);
    FourVector result = transform.toNewFrame(history.event(τA));
    return result;
  }
  
  private double round(double value) {
    return Util.round(value, 4);
  }
}
