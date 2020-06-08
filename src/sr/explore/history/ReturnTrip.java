package sr.explore.history;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.Speed;
import sr.core.Util;
import sr.core.history.History;
import sr.core.history.HistoryFromLegs;
import sr.core.history.Leg;
import sr.core.history.UniformVelocity;
import sr.core.transform.CoordTransform;
import sr.core.transform.Displace;
import sr.core.transform.NoOpTransform;

/**
 Simple return trip from the origin event A to B then back to A's position, all along the X axis, 
 all at the same uniform speed.
 There's a discontinuity at the turn-around point B.
 There are 2 {@link Leg}s to the trip.
*/
public final class ReturnTrip extends HistoryFromLegs {
  
  /** Run for various values, and output a file. */
  public static void main(String... args) {
    double distance = 1.0;
    List<String> lines = new ArrayList<>();
    lines.add("β distance   τmax            tmax");
    lines.add("---------------------------------");
    for(Speed speed : Speed.nonExtremeValues()) {
      History trip = new ReturnTrip(speed.β(), distance);
      lines.add(trip.toString());
    }
    Util.writeToFile(OneWayTrip.class, "return-trip-"+distance + ".txt", lines);
  }
  
  /**
   Constructor.
   @param β the speed of each leg
   @param distance the distance traveled on each leg 
  */
  public ReturnTrip(double β, double distance) {
    this.β = β;
    this.distance = distance;
    this.τHalfWay = τHalfWay();
  }

  /** Proper time at the start-event is 0. */
  @Override public double τmin() { return 0; }
  
  @Override protected List<Leg> initLegs() {
    History hist1 = new UniformVelocity(Axis.X, β, 0.0, τHalfWay);
    Leg leg1 = new Leg(hist1, new NoOpTransform()); 
    //match position and time, but no need to match speed (discontinuous anyway)
    CoordTransform backToLeg1 = Displace.originTo(leg1.history().end());

    History hist2 = new UniformVelocity(Axis.X, -β, τHalfWay, 2*τHalfWay);
    Leg leg2 = new Leg(hist2, backToLeg1);
    
    return Arrays.asList(leg1, leg2);
  }
  
  public double distance() { return distance; }
  
  @Override public String toString() {
    String s = "  ";
    return β+s+ distance+s + τmax()+s+ end().ct();
  }
  
  //PRIVATE 
  
  private double β;
  private double distance;
  private double τHalfWay;
  
  private double τHalfWay() {
    double ctHalfway = distance / β;
    double τHalfWay = ctHalfway / Physics.Γ(β);
    return τHalfWay;
  }
}
