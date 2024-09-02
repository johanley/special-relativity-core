package sr.explore.clocks;

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
    add("One-way trip through space at a uniform velocity.");
    add("Rocket-time is the traveler's wrist-watch time (proper time).");
    add(Util.NL + "From the point of view of the traveler, high-speed travel acts like a worm-hole.");
    add(Util.NL + "Distance traveled: " + lightyears + " light-years." + Util.NL);
    add(table.row("β", "Rocket-time (y)", "Home-time (y)"));
    add(Util.separator(65));
    for(SpeedValues speed : SpeedValues.nonExtremeValues()) {
      if (speed.β() > 0) {
        double coordTime = lightyears / speed.β();
        double travelTime = coordTime / speed.Γ();
        add(table.row(speed.β(), round(travelTime), round(coordTime)));
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
    add("Round trip, same speed both outbound and inbound.");
    add("Rocket-time is the traveler's wrist-watch time (proper time).");
    add(Util.NL + "From the point of view of the traveler, high-speed return trips act as a time-machine into the future.");
    add(Util.NL + "Distance traveled: " + lightyears + " light-years, out and back." + Util.NL);
    add(table.row("β", "Rocket-time (y)", "Home-time (y)"));
    add(Util.separator(65));
    for(SpeedValues speed : SpeedValues.nonExtremeValues()) {
      if (speed.β() > 0) {
        double coordTime = lightyears / speed.β();
        double travelTime = coordTime / speed.Γ();
        add(table.row(speed.β(), round(2.0*travelTime), round(2.0*coordTime)));
      }
    }
    outputToConsoleAnd("travel-time-round-trip.txt");
  }
  
  private Table table = new Table("%-20s", "%-20s", "%-20s");

  private double round(Double value) {
    return Util.round(value, 6);
  }

}
