package sr.explore.history;

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
import sr.core.transform.NoOpTransform;
import sr.explore.output.text.Table;
import sr.explore.output.text.TextOutput;

/**
 Very simple one-way trip from the origin-event to a place B (on the X axis) at a given uniform speed.
 There's only 1 {@link Leg} for this trip.
*/
public final class OneWayTrip extends HistoryFromLegs {

  /** Run for various values, and output a file. */
  public static void main(String... args) {
    double distance = 1.0;
    
    TextOutput lines = new TextOutput();
    Table table = new Table("%-32s", "%-20s");
    lines.add(table.row("β", "distance", "τmax"));
    lines.add(Util.separator(50));
    History trip = null;
    for(Speed speed : Speed.nonExtremeValues()) {
      trip = new OneWayTrip(speed.β(), distance);
      lines.add(trip.toString());
    }
    lines.outputTo("one-way-trip-"+distance + ".txt", trip);
  }
  
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
  
  public double distance() { return distance; }
  
  @Override public String toString() {
    String s = "  ";
    return β+s+ distance+s + τmax();
  }
  
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
