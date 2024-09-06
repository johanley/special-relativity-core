package sr.explore.accel.speed;

import static sr.core.Physics.ONE_GEE;

import sr.core.Axis;
import sr.core.Util;
import sr.core.history.timelike.StitchedTimelikeHistory;
import sr.core.history.timelike.TimelikeDeltaBase;
import sr.core.history.timelike.TimelikeHistory;
import sr.core.history.timelike.TimelikeMoveableHistory;
import sr.core.history.timelike.UniformAcceleration;
import sr.core.vector3.Position;
import sr.core.vector4.Event;
import sr.core.vector4.transform.Reflection;
import sr.explore.Exploration;
import sr.output.text.Table;
import sr.output.text.TextOutput;

/**
An out-and-back return trip, all at an acceleration of -/+ 1g.


<P>The history has this general appearance:

<pre>
           CT
           ^
           |*
           |*
           | *
           |  *
           |    *
           |       *
           |          *
           |              *
           |                  *
           |                    *
           |                     *
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
public final class OneGeeThereAndBack extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    OneGeeThereAndBack oneGee = new OneGeeThereAndBack();
    oneGee.explore();
  }
  
  @Override public void explore() {
    add("A return trip in a relativistic rocket accelerating at 1g.");
    add("Three parts (in terms of fractions of total proper-time):");
    add("  - 0.00 - 0.25: +1g acceleration");
    add("  - 0.25 - 0.75: -1g braking");
    add("  - 0.75 - 1.00: +1g acceleration");
    add("At the end of the trip, the rocket has returned to its starting point.");
    add("If light-years and years are used as units, then 1g has the numeric value of " + ONE_GEE + "." + Util.NL);
    table();
    outputToConsoleAnd("one-gee-there-and-back.txt");
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
    TimelikeHistory history = roundTripAtOneGee(τ_years);
    double end_ct = history.ct(τ_years);
    Event end_event = history.event(end_ct);
    add(table.row(τ_years, end_event.x(), end_event.ct()));
  }
  
  // Proper-time cτ, Distance light-years, Coordinate-time ct
  private Table table = new Table("%-4s", "%20.2f", "%20.2f");
  private Table tableHeader = new Table("%-15s", "%-22s", "%-20s");
  private static final int NUM_YEARS = 24;
  private static final Axis X = Axis.X;

  private TimelikeHistory roundTripAtOneGee(double τ_years) {
    TimelikeMoveableHistory leg = UniformAcceleration.of(Position.origin(), X, ONE_GEE);
    StitchedTimelikeHistory builder = StitchedTimelikeHistory.startingWith(leg);

    Event quarterWay = leg.eventFromProperTime(τ_years * 0.25);
    //and these two events by symmetry:
    Event halfWay = quarterWay.plus(quarterWay); 
    Reflection reflect = Reflection.of(X);
    Event allTheWay = halfWay.plus(reflect.changeVector(halfWay)); 
    
    //the delta-bases aren't the same as the branch points:
    
    TimelikeDeltaBase deltaBase = TimelikeDeltaBase.of(halfWay, τ_years * 0.5);
    leg = UniformAcceleration.of(deltaBase, X, -ONE_GEE);
    builder.addTheNext(leg, quarterWay.ct());

    deltaBase = TimelikeDeltaBase.of(allTheWay, τ_years);
    leg = UniformAcceleration.of(deltaBase, X, ONE_GEE);
    builder.addTheNext(leg, 3 * quarterWay.ct());
    
    return builder.build();
  }
}
