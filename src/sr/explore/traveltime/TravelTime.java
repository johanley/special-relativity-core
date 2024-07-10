package sr.explore.traveltime;

import java.util.ArrayList;
import java.util.List;

import sr.core.Speed;
import sr.core.Util;
import sr.explore.Table;

/** 
 Travel-time (proper time) for various trips, according to the traveler. 
*/
public final class TravelTime {
  
  public static void main(String... args) {
    TravelTime tt = new TravelTime();
    tt.oneWayTripUniformVelocity(50.0);
    tt.twoWayTripUniformSpeed(50.0);
  }
  
  /** 
   Simplest case.
   A trip at uniform velocity acts like a wormhole: the traveler gets from A to B in a short travel-time, 
   according to their own clock. 
  */
  void oneWayTripUniformVelocity(double lightyears) {
    List<String> lines = new ArrayList<>();
    lines.add("One-way trip at uniform velocity, traveling a distance of " + lightyears + " light-years.");
    lines.add("Travel-time is proper time.");
    lines.add("Acts like a wormhole, from the point of view of the traveler." + Util.NL);
    lines.add(table.row("β speed", "travel-time (y)", "coord-time (y)"));
    lines.add(Util.separator(65));
    for(Speed speed : Speed.nonExtremeValues()) {
      if (speed.β() > 0) {
        double coordTime = lightyears / speed.β();
        double travelTime = coordTime / speed.Γ();
        lines.add(table.row(speed.β(), round(travelTime), round(coordTime)));
      }
    }
    output(lines, "travel-time-one-way-" + lightyears + ".txt");
  }
  

  /** 
   A two-way trip.
   Both the outbound and inbound parts of the trips are at the same speed.
   There's a discontinuity at the turnaround point.
   A two-way trip acts like a time machine into the future: the traveler gets into the deep future faster than usual.
  */
  void twoWayTripUniformSpeed(double lightyears) {
    List<String> lines = new ArrayList<>();
    lines.add(Util.NL + "Round trip, same speed on both outbound and inbound, traveling out " + lightyears + " light years and back.");
    lines.add("Travel-time is proper time.");
    lines.add("For the traveler, this acts like a time machine into the future." + Util.NL);
    lines.add(table.row("β speed", "travel-time (y)", "coord-time (y)"));
    lines.add(Util.separator(65));
    for(Speed speed : Speed.nonExtremeValues()) {
      if (speed.β() > 0) {
        double coordTime = lightyears / speed.β();
        double travelTime = coordTime / speed.Γ();
        lines.add(table.row(speed.β(), round(2.0*travelTime), round(2.0*coordTime)));
      }
    }
    output(lines, "travel-time-two-way-" + lightyears + ".txt");
  }
  
  private Table table = new Table("%-20s", "%-20s", "%-20s");

  private void output(List<String> lines, String fileName) {
    for(String line : lines) {
      System.out.println(line);
    }
    Util.writeToFile(this.getClass(), fileName, lines);
  }
  
  private double round(Double value) {
    return Util.round(value, 4);
  }

}
