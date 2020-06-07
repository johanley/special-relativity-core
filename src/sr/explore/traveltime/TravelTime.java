package sr.explore.traveltime;

import java.util.ArrayList;
import java.util.List;

import sr.core.SpeedValues;
import sr.core.Util;

/** 
 Travel-time (proper time) for various trips (histories). 
*/
public final class TravelTime {
  
  public static void main(String... args) {
    TravelTime tt = new TravelTime();
    tt.oneWayTripUniformVelocity(50.0);
    tt.twoWayTripUniformSpeed(50.0);
  }
  
  /** 
   Simplest case.
   A trip at uniform velocity acts like a wormhole: the 
   traveler gets from A to B in a short travel-time. 
  */
  void oneWayTripUniformVelocity(double lightyears) {
    List<String> lines = new ArrayList<>();
    lines.add("# Uniform velocity. Distance " + lightyears + " l.y.");
    lines.add("# Acts like a wormhole");
    lines.add("# speed, travel-time (years), coord-time (years)");
    for(SpeedValues speed : SpeedValues.nonExtremeValues()) {
      if (speed.β() > 0) {
        double coordTime = lightyears / speed.β();
        double travelTime = coordTime / speed.Γ();
        lines.add(speed.β() + " " + travelTime + " " + coordTime);
      }
    }
    Util.writeToFile(this.getClass(), "travel-time-one-way-" + lightyears + ".txt", lines);
  }
  

  /** 
   A two-way trip.
   Both the outbound and inbound legs are at the same speed.
   There's a discontinuity at the turnaround point.
   A two-way trip acts like a time machine into the future: the 
   traveler gets into the deep future faster than usual.
  */
  void twoWayTripUniformSpeed(double lightyears) {
    List<String> lines = new ArrayList<>();
    lines.add("# Two-way trip, same speed on both legs. Distance " + lightyears + " l.y.");
    lines.add("# Acts like a time machine into the future");
    lines.add("# speed, travel-time (years), coord-time (years)");
    for(SpeedValues speed : SpeedValues.nonExtremeValues()) {
      if (speed.β() > 0) {
        double coordTime = lightyears / speed.β();
        double travelTime = coordTime / speed.Γ();
        lines.add(speed.β() + " " + 2.0*travelTime + " " + 2.0*coordTime);
      }
    }
    Util.writeToFile(this.getClass(), "travel-time-two-way-" + lightyears + ".txt", lines);
  }

}
