package sr.explore.history;

import java.util.Arrays;
import java.util.List;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.history.History;
import sr.core.history.HistoryFromLegs;
import sr.core.history.Leg;
import sr.core.history.UniformVelocity;
import sr.core.transform.NoOpTransform;

/**
 Very simple one-way trip from the origin-event to a place B (on the X axis) at uniform speed.
 There's only 1 {@link Leg} for this trip.
*/
public final class OneWayTrip extends HistoryFromLegs {
  
  /**
   Constructor.
   @param β the speed of the single leg
   @param distance the distance traveled on the single leg 
  */
  public OneWayTrip(double β, double distance) {
    this.β = β;
    this.distance = distance;
    this.τMax = τMax(); 
  }

  /** Proper time at the start-event is 0. */
  @Override public double τmin() { return 0; }
  
  @Override protected List<Leg> initLegs() {
    History hist = new UniformVelocity(Axis.X, β, 0.0, τMax);
    Leg leg = new Leg(hist, new NoOpTransform()); 
    return Arrays.asList(leg);
  }
  
  /** Constant value, independent of τ. */
  @Override public double β(double τ) { return β; }
  public double distance() { return distance; }
  
  //PRIVATE 
  
  private double β;
  private double distance;
  private double τMax;
  
  private double τMax() {
    double ctEndpoint = distance / β;
    double result = ctEndpoint / Physics.Γ(β);
    return result;
  }
}
