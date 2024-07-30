package sr.explore.accel.linear;

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
public final class OneGeeThereAndBack extends TextOutput {
  
  public static void main(String[] args) {
    OneGeeThereAndBack oneGee = new OneGeeThereAndBack();
    oneGee.explore();
  }
  
  void explore() {
    lines.add("A return trip in a relativistic rocket accelerating at 1g.");
    lines.add("Three parts (in terms of fractions of total proper-time):");
    lines.add("  - 0.00 - 0.25: +1g acceleration");
    lines.add("  - 0.25 - 0.75: -1g braking");
    lines.add("  - 0.75 - 1.00: +1g acceleration");
    lines.add("At the end of the trip, the rocket has returned to its starting point.");
    lines.add("If light-years and years are used as units, then 1g has the numeric value of " + ONE_GEE + "." + Util.NL);
    table();
    outputToConsole();
  }

  /** The numeric value of 1g, expressed using light-years as the distance unit and year as the time-unit {@value}. */
  public static final double ONE_GEE = 1.03; //light-years, year as the unit!

  private void table() {
    lines.add(tableHeader.row("Proper-time", "Coordinate-distance", "Coordinate-time"));
    lines.add(tableHeader.row("(years)", "(light-years)", "(years)"));
    for(int yearsProperTime = 1; yearsProperTime <= NUM_YEARS; ++yearsProperTime) {
      explore(yearsProperTime);
    }
  }
 
  private void explore(double τ_years) {
    History history = roundTripAtOneGee(τ_years);
    double end_ct = history.ct(τ_years);
    Event end_event = history.event(end_ct);
    lines.add(table.row(τ_years, end_event.x(), end_event.ct()));
  }
  
  // Proper-time cτ, Distance light-years, Coordinate-time ct
  private Table table = new Table("%-4s", "%20.2f", "%20.2f");
  private Table tableHeader = new Table("%-15s", "%-22s", "%-20s");
  private static final int NUM_YEARS = 24;
  private static final Axis X = Axis.X;

  private History roundTripAtOneGee(double τ_years) {
    MoveableHistory leg = UniformAcceleration.of(Position.origin(), X, ONE_GEE);
    StitchedHistoryBuilder builder = StitchedHistoryBuilder.startingWith(leg);

    Event quarterWay = leg.eventFromProperTime(τ_years * 0.25);
    //and these two events by symmetry:
    Event halfWay = quarterWay.plus(quarterWay); 
    Event allTheWay = halfWay.plus(halfWay.spatialReflection()); 
    
    //the delta-bases aren't the same as the branch points:
    
    DeltaBase deltaBase = DeltaBase.of(halfWay, τ_years * 0.5);
    leg = UniformAcceleration.of(deltaBase, X, -ONE_GEE);
    builder.addTheNext(leg, quarterWay.ct());

    deltaBase = DeltaBase.of(allTheWay, τ_years);
    leg = UniformAcceleration.of(deltaBase, X, ONE_GEE);
    builder.addTheNext(leg, 3 * quarterWay.ct());
    
    return builder.build();
  }
}
