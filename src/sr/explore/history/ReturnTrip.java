package sr.explore.history;

import java.util.Arrays;
import java.util.List;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.history.History;
import sr.core.history.HistoryFromLegs;
import sr.core.history.Leg;
import sr.core.history.UniformVelocity;
import sr.core.transform.CoordTransform;
import sr.core.transform.Displace;
import sr.core.transform.NoOpTransform;
import sr.core.transform.FourVector;

/**
 Simple return trip from the origin event A to B then back to A's position, all along the X axis, all at the same uniform speed.
 There's a discontinuity at the turn-around point B.
 There are 2 {@link Leg}s to the trip.
*/
public final class ReturnTrip extends HistoryFromLegs {
  
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
    FourVector end1 = leg1.history().event(leg1.history().τmax());
    //match position and time, but no need to match speed (discontinuous anyway)
    CoordTransform transformer1 = Displace.to(end1);

    History hist2 = new UniformVelocity(Axis.X, -β, τHalfWay, 2*τHalfWay);
    Leg leg2 = new Leg(hist2, transformer1);
    //the last leg: no need to build a transformer for it
    
    return Arrays.asList(leg1, leg2);
  }
  
  public double β() { return β; }
  public double distance() { return distance; }
  
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
