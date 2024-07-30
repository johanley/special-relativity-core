package sr.explore.accel.speed;

import sr.core.Axis;
import sr.core.Util;
import sr.core.event.Event;
import sr.core.history.DeltaBase;
import sr.core.history.History;
import sr.core.history.MoveableHistory;
import sr.core.history.StitchedHistoryBuilder;
import sr.core.history.UniformAcceleration;
import sr.core.vector.Position;
import sr.output.text.Table;
import sr.output.text.TextOutput;

/**
Accelerate at 1g for the first half of a trip, then turn around and brake at 1g for the second half.


<P>The history has this general appearance:

<pre>
           CT
           ^
           |                     *
           |                    *
           |                   *
           |                 *
           |              *
           |           *
           |        *
           |      *
           |    *
           |  *
           | *
           |*
-----------*-----------------&gt; X
           |
</pre>
*/
public final class OneGeeThereAndStay extends TextOutput {
  
  public static void main(String[] args) {
    OneGeeThereAndStay oneGee = new OneGeeThereAndStay();
    oneGee.explore();
  }
  
  void explore() {
    lines.add("Travel in a relativistic rocket accelerating at 1g.");
    lines.add("Accelerate for the first half of the trip, then reverse and brake for the second half.");
    lines.add("If light-years and years are used as units, then 1g has the numeric value of " + ONE_GEE + "." + Util.NL);
    table();
    outputToConsoleAnd("one-gee-there-and-stay.txt");
  }

  /** The numeric value of 1g, expressed using light-years as the distance unit and year as the time-unit. {@value}. */
  public static final double ONE_GEE = 1.03; //light-years, year as the unit!

  private void table() {
    lines.add(tableHeader.row("Proper-time", "Coordinate-distance", "Coordinate-time"));
    lines.add(tableHeader.row("(years)", "(light-years)", "(years)"));
    lines.add(dashes(52));
    for(int yearsProperTime = 1; yearsProperTime <= NUM_YEARS; ++yearsProperTime) {
      explore(yearsProperTime);
    }
  }
 
  private void explore(double τ_years) {
    History history = accelerateThenBrake(τ_years);
    double end_ct = history.ct(τ_years);
    Event end_event = history.event(end_ct);
    lines.add(table.row(τ_years, end_event.x(), end_event.ct()));
  }
  
  // Proper-time cτ, Distance light-years, Coordinate-time ct
  private Table table = new Table("%-4s", "%20.2f", "%20.2f");
  private Table tableHeader = new Table("%-15s", "%-22s", "%-20s");
  private static final int NUM_YEARS = 20;

  private History accelerateThenBrake(double τ_years) {
    double τ_halfWay = τ_years * 0.5;
    
    MoveableHistory acceleration = UniformAcceleration.of(Position.origin(), Axis.X, ONE_GEE);
    Event halfWay = acceleration.eventFromProperTime(τ_halfWay);
    //note how the delta-base is computed, by symmetry:
    MoveableHistory braking = UniformAcceleration.of(DeltaBase.of(halfWay.plus(halfWay), τ_years), Axis.X, -ONE_GEE);
    
    StitchedHistoryBuilder builder = StitchedHistoryBuilder.startingWith(acceleration);
    builder.addTheNext(braking, halfWay.ct());
    return builder.build();
  }
}
