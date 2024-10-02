package sr.explore.clocks;

import static sr.core.Util.NL;

import sr.core.Axis;
import sr.core.SpeedValues;
import sr.core.Util;
import sr.core.component.NPosition;
import sr.core.hist.timelike.NThereAndBack;
import sr.core.hist.timelike.NTimelikeDeltaBase;
import sr.core.hist.timelike.NTimelikeHistory;
import sr.core.hist.timelike.NUniformVelocity;
import sr.core.vec3.NVelocity;
import sr.explore.Exploration;
import sr.output.text.Table;
import sr.output.text.TextOutput;

/** Travel-time (proper time) for various trips, according to the traveler. */
public final class TravelTime extends TextOutput implements Exploration {
  
  public static void main(String... args) {
    TravelTime tt = new TravelTime();
    tt.explore();
  }
  
  @Override public void explore() {
    double lightyears = 50.0;
    oneWayTripUniformVelocity(lightyears);
    twoWayTripUniformSpeed(lightyears);
    outputToConsoleAnd("travel-time.txt");
  }
  
  /** 
   Simplest case.
   A trip at uniform velocity acts like a wormhole: the traveler gets from A to B in a short travel-time, 
   according to their own clock. 
  */
  void oneWayTripUniformVelocity(double lightyears) {
    add("One-way trip through space at a uniform velocity.");
    add("Proper-time is the traveler's wrist-watch time.");
    add(NL + "From the point of view of the traveler, extreme high-speed travel acts like a worm-hole.");
    add(NL + "Distance traveled: " + lightyears + " light-years." + NL);
    add(table.row("β", "Proper-time", "Coordinate-time"));
    add(table.row("", "τ (years)", "ct (years)"));
    add(dashes(55));
    for(SpeedValues speed : SpeedValues.nonExtremeValues()) {
      NTimelikeHistory history = NUniformVelocity.of(NPosition.origin(), NVelocity.of(speed.β(), Axis.X));
      double ct = lightyears / speed.β();
      double τ = history.τ(ct);
      add(table.row(speed.β(), round(τ), round(ct)));
    }
  }

  /** 
   A two-way trip.
   Both the outbound and inbound parts of the trips are at the same speed.
   There's a discontinuity at the turnaround point.
   A two-way trip acts like a time machine into the future: the traveler gets into the deep future faster than usual.
  */
  void twoWayTripUniformSpeed(double lightyears) {
    add(NL + "Round trip, same speed both outbound and inbound.");
    add(NL + "From the point of view of the traveler, extreme high-speed return trips act as a time-machine into the future.");
    add(NL + "Distance traveled: " + lightyears + " light-years, out and back." + NL);
    add(table.row("β", "Proper-time", "Coordinate-time"));
    add(table.row("", "τ (years)", "ct (years)"));
    add(dashes(55));
    for(SpeedValues speed : SpeedValues.nonExtremeValues()) {
      NTimelikeHistory history = NThereAndBack.of(NTimelikeDeltaBase.of(NPosition.origin()), NVelocity.of(speed.β(), Axis.X));
      double ct = lightyears / speed.β();
      double τ = history.τ(ct);
      add(table.row(speed.β(), round(2.0*τ), round(2.0*ct)));
    }
  }
  
  private Table table = new Table("%-20s", "%-20s", "%-20s");

  private double round(Double value) {
    return Util.round(value, 6);
  }

}
