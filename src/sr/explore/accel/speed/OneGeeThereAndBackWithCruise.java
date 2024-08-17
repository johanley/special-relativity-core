package sr.explore.accel.speed;

import sr.core.Axis;
import sr.core.Util;
import sr.core.history.DeltaBase;
import sr.core.history.History;
import sr.core.history.MoveableHistory;
import sr.core.history.StitchedHistoryBuilder;
import sr.core.history.UniformAcceleration;
import sr.core.history.UniformVelocity;
import sr.core.vector3.Position;
import sr.core.vector4.Event;
import sr.core.vector4.transform.Reflection;
import sr.output.text.Table;
import sr.output.text.TextOutput;

/**
An out-and-back return trip, mostly at an acceleration of -/+ 1g, but with additional cruising stages at the highest speed.

<P>Here's an example itinerary, expressed using proper-time, for a trip that accelerates at +/- 1g for 24 years, and 
cruises for 2 years, in between changing the direction of acceleration:
 <ul>
  <li>6 years: accelerate at +1g   (+X direction)
  <li>1 year : cruise at top speed (+X direction)
  <li>12 years: brake at -1g       (-X direction)
  <li>1 year : cruise at top speed (-X direction)
  <li>6 years: accelerate at +1g   (+X direction)
 </ul>

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
           |             *
           |                *
           |                  *
           |                    *
           |                      *
           |                        *
           |                         *
           |                          *
           |                         *
           |                        *
           |                      *
           |                    *
           |                  *
           |                *
           |              *
           |            *
           |          *
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
public final class OneGeeThereAndBackWithCruise extends TextOutput {
  
  public static void main(String[] args) {
    OneGeeThereAndBackWithCruise oneGee = new OneGeeThereAndBackWithCruise();
    oneGee.explore();
  }
  
  void explore() {
    lines.add("A return trip in a relativistic rocket accelerating at 1g.");
    lines.add("Five parts:");
    lines.add("  - +1g acceleration");
    lines.add("  - cruise at top speed (outbound) for 1 year (proper-time)");
    lines.add("  - -1g braking");
    lines.add("  - cruise at top speed (inbound) for 1 year (proper-time)");
    lines.add("  - +1g acceleration");
    lines.add("At the end of the trip, the rocket has returned to its starting point.");
    lines.add("If light-years and years are used as units, then 1g has the numeric value of " + ONE_GEE + "." + Util.NL);
    table();
    outputToConsoleAnd("one-gee-there-and-back-with-cruise.txt");
  }

  /** The numeric value of 1g, expressed using light-years as the distance unit and year as the time-unit. {@value}. */
  public static final double ONE_GEE = 1.03; //light-years, year as the unit!

  private void table() {
    lines.add(tableHeader.row("Proper-time", "Coordinate-distance", "Coordinate-time"));
    lines.add(tableHeader.row("(years)", "(light-years)", "(years)"));
    lines.add(dashes(52));
    for(int yearsAccel = 2; yearsAccel <= NUM_YEARS_ACCEL; ++yearsAccel) {
      explore(yearsAccel, NUM_YEARS_CRUISING);
    }
  }
 
  private void explore(double τ_years_accel, double τ_years_cruising) {
    History history = roundTripAtOneGee(τ_years_accel, τ_years_cruising);
    double τ_years = τ_years_accel + τ_years_cruising;
    double end_ct = history.ct(τ_years);
    Event end_event = history.event(end_ct);
    lines.add(table.row(τ_years, end_event.x(), end_event.ct()));
  }
  
  // Proper-time cτ, Distance light-years, Coordinate-time ct
  private Table table = new Table("%-4s", "%20.2f", "%20.2f");
  private Table tableHeader = new Table("%-15s", "%-22s", "%-20s");
  private static final int NUM_YEARS_ACCEL = 24;
  private static final int NUM_YEARS_CRUISING = 2;
  private static final Axis X = Axis.X;

  private History roundTripAtOneGee(double τ_years_accel, double τ_years_cruising) {
    
    /*
     * Example : 24 years_accel + 2 years_cruising = (6 + 1 + 6) + (6 + 1 +6)
     *                                             = 6 + 1 + 12 + 1 + 6
     * Be careful about the meaning of branch point and delta-base 
     */
    
    //accelerate 6 years
    MoveableHistory leg = UniformAcceleration.of(Position.origin(), X, ONE_GEE);
    StitchedHistoryBuilder builder = StitchedHistoryBuilder.startingWith(leg);

    //cruise 1 year
    Event branch_point = leg.eventFromProperTime(τ_years_accel * 0.25);
    DeltaBase delta_base = DeltaBase.of(branch_point, leg.τ(branch_point.ct()));
    leg = UniformVelocity.of(delta_base, leg.velocity(branch_point.ct()));
    builder.addTheNext(leg, branch_point.ct());
    
    Event half_way_out = leg.eventFromProperTime(τ_years_accel * 0.25 + τ_years_cruising * 0.25);
    Event all_way_out = half_way_out.plus(half_way_out);
    
    //brake 12 years
    branch_point = leg.eventFromProperTime(τ_years_accel * 0.25 + τ_years_cruising * 0.5);
    delta_base = DeltaBase.of(all_way_out, (τ_years_accel +  τ_years_cruising) * 0.5); 
    leg = UniformAcceleration.of(delta_base, X, -ONE_GEE);
    builder.addTheNext(leg, branch_point.ct());

    //cruise 1 year
    branch_point = leg.eventFromProperTime(τ_years_accel * 0.75 + τ_years_cruising * 0.5);
    delta_base = DeltaBase.of(branch_point, leg.τ(branch_point.ct()));
    leg = UniformVelocity.of(delta_base, leg.velocity(branch_point.ct()));
    builder.addTheNext(leg, branch_point.ct());

    //accel 6 years
    branch_point = leg.eventFromProperTime(τ_years_accel * 0.75 + τ_years_cruising);
    Reflection flipX = Reflection.of(X);
    delta_base = DeltaBase.of(all_way_out.plus(flipX.changeEvent(all_way_out)), τ_years_accel + τ_years_cruising); 
    leg = UniformAcceleration.of(delta_base, X, ONE_GEE);
    builder.addTheNext(leg, branch_point.ct());
    
    return builder.build();
  }
}
