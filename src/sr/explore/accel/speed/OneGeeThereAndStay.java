package sr.explore.accel.speed;

import static sr.core.Physics.ONE_GEE;

import sr.core.Axis;
import sr.core.Util;
import sr.core.component.NEvent;
import sr.core.component.NPosition;
import sr.core.hist.timelike.NStitchedTimelikeHistory;
import sr.core.hist.timelike.NTimelikeDeltaBase;
import sr.core.hist.timelike.NTimelikeHistory;
import sr.core.hist.timelike.NTimelikeMoveableHistory;
import sr.core.hist.timelike.NUniformAcceleration;
import sr.explore.Exploration;
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
public final class OneGeeThereAndStay extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    OneGeeThereAndStay oneGee = new OneGeeThereAndStay();
    oneGee.explore();
  }
  
  @Override public void explore() {
    add("Travel in a relativistic rocket accelerating at 1g.");
    add("Accelerate for the first half of the trip, then reverse and brake for the second half.");
    add("If light-years and years are used as units, then 1g has the numeric value of " + ONE_GEE + "." + Util.NL);
    table();
    outputToConsoleAnd("one-gee-there-and-stay.txt");
  }

  private void table() {
    add(tableHeader.row("Proper-time", "Coordinate-distance", "Coordinate-time"));
    add(tableHeader.row("(years)", "(light-years)", "(years)"));
    add(dashes(52));
    for(int yearsProperTime = 1; yearsProperTime <= NUM_YEARS; ++yearsProperTime) {
      explore(yearsProperTime);
    }
  }
 
  private void explore(double τ_years) {
    NTimelikeHistory history = accelerateThenBrake(τ_years);
    double end_ct = history.ct(τ_years);
    NEvent end_event = history.event(end_ct);
    add(table.row(τ_years, end_event.x(), end_event.ct()));
  }
  
  // Proper-time cτ, Distance light-years, Coordinate-time ct
  private Table table = new Table("%-4s", "%20.2f", "%20.2f");
  private Table tableHeader = new Table("%-15s", "%-22s", "%-20s");
  private static final int NUM_YEARS = 20;

  private NTimelikeHistory accelerateThenBrake(double τ_years) {
    double τ_halfWay = τ_years * 0.5;
    
    NTimelikeMoveableHistory acceleration = NUniformAcceleration.of(NPosition.origin(), Axis.X, ONE_GEE);
    NEvent halfWay = acceleration.eventFromProperTime(τ_halfWay);
    //note how the delta-base is computed, by symmetry:
    NEvent halfWayTimes2 = NEvent.of(
      halfWay.ct()*2,
      halfWay.x()*2,
      halfWay.y()*2,
      halfWay.z()*2
    );
    NTimelikeMoveableHistory braking = NUniformAcceleration.of(NTimelikeDeltaBase.of(halfWayTimes2, τ_years), Axis.X, -ONE_GEE);
    
    NStitchedTimelikeHistory builder = NStitchedTimelikeHistory.startingWith(acceleration);
    builder.addTheNext(braking, halfWay.ct());
    return builder.build();
  }
}
