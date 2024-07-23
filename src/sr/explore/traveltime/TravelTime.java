package sr.explore.traveltime;

import sr.core.SpeedValues;
import sr.core.Util;
import sr.output.text.Table;
import sr.output.text.TextOutput;

/** 
 Travel-time (proper time) for various trips, according to the traveler. 
*/
public final class TravelTime extends TextOutput {
  
  public static void main(String... args) {
    TravelTime tt = new TravelTime();
    tt.explore();
  }
  
  public void explore() {
    double lightyears = 50.0;
    oneWayTripUniformVelocity(lightyears);
    twoWayTripUniformSpeed(lightyears);
  }
  
  /** 
   Simplest case.
   A trip at uniform velocity acts like a wormhole: the traveler gets from A to B in a short travel-time, 
   according to their own clock. 
  */
  void oneWayTripUniformVelocity(double lightyears) {
    lines.add("One-way trip at uniform velocity, traveling a distance of " + lightyears + " light-years.");
    lines.add("Proper-time is the traveler's wrist-watch time.");
    lines.add("Acts like a wormhole, from the point of view of the traveler." + Util.NL);
    lines.add(table.row("β speed", "proper-time (y)", "home-time (y)"));
    lines.add(Util.separator(65));
    for(SpeedValues speed : SpeedValues.nonExtremeValues()) {
      if (speed.β() > 0) {
        double coordTime = lightyears / speed.β();
        double travelTime = coordTime / speed.Γ();
        lines.add(table.row(speed.β(), round(travelTime), round(coordTime)));
      }
    }
    outputToConsoleAnd("travel-time-one-way-trip.txt");
  }

  /** 
   A two-way trip.
   Both the outbound and inbound parts of the trips are at the same speed.
   There's a discontinuity at the turnaround point.
   A two-way trip acts like a time machine into the future: the traveler gets into the deep future faster than usual.
  */
  void twoWayTripUniformSpeed(double lightyears) {
    lines.add("Round trip, same speed both outbound and inbound, traveling out " + lightyears + " light years and back.");
    lines.add("Proper-time is the traveler's wrist-watch time.");
    lines.add("For the traveler, this acts like a time machine into the future." + Util.NL);
    lines.add(table.row("β speed", "proper-time (y)", "home-time (y)"));
    lines.add(Util.separator(65));
    for(SpeedValues speed : SpeedValues.nonExtremeValues()) {
      if (speed.β() > 0) {
        double coordTime = lightyears / speed.β();
        double travelTime = coordTime / speed.Γ();
        lines.add(table.row(speed.β(), round(2.0*travelTime), round(2.0*coordTime)));
      }
    }
    outputToConsoleAnd("travel-time-round-trip.txt");
  }
  
  private Table table = new Table("%-20s", "%-20s", "%-20s");

  private double round(Double value) {
    return Util.round(value, 4);
  }

}
