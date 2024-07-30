package sr.explore.accel.linear;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.Util;
import sr.core.event.Event;
import sr.core.history.MoveableHistory;
import sr.core.history.UniformAcceleration;
import sr.core.vector.Position;
import sr.output.text.Table;
import sr.output.text.TextOutput;

/**
 Accelerate at 1g forever.
 
 Under 1g of constant acceleration, one could cross the Milky Way in about 12 years of proper-time.
 
 <P>The history has this general appearance:
 
<pre>
            CT
            ^
            |           *
            |        *
            |      *
            |    *
            |  *
            | *
            |*
 -----------*-----------------&gt; X
            |
            | 
            |     
 </pre>
 */
public final class OneGeeForever extends TextOutput {
  
  public static void main(String[] args) {
    OneGeeForever oneGee = new OneGeeForever();
    oneGee.explore();
  }
  
  void explore() {
    lines.add("Travel in a relativistic rocket accelerating at 1g.");
    lines.add("If light-years and years are used as units, then 1g has the numeric value of " + ONE_GEE + ".");
    lines.add("The Milky Way galaxy is about 100,000 light years across.");
    lines.add("You would cross the Milky Way in about 12 years of proper-time."+ Util.NL);
    table();
    
    outputToConsoleAnd("one-gee-forever.txt");
  }

  /** The numeric value of 1g, expressed using light-years as the distance unit and year as the time-unit. {@value}. */
  public static final double ONE_GEE = 1.03; //light-years, year as the unit!

  private void table() {
    lines.add(tableHeader.row("Proper-time", "Coordinate-distance", "Coordinate-time", "Terminal speed", "Terminal Γ"));
    lines.add(tableHeader.row("(years)", "(light-years)", "(years)", "(β)", ""));
    for(int yearsProperTime = 1; yearsProperTime <= NUM_YEARS; ++yearsProperTime) {
      explore(yearsProperTime);
    }
  }
 
  private void explore(int yearsProperTime) {
    MoveableHistory history = UniformAcceleration.of(Position.origin(), Axis.X, ONE_GEE);
    Event endsAt = history.eventFromProperTime(yearsProperTime);
    double coordinateTime = endsAt.ct();
    double terminalSpeed = history.velocity(coordinateTime).magnitude();
    lines.add(table.row(yearsProperTime, endsAt.x(), coordinateTime, terminalSpeed, Physics.Γ(terminalSpeed)));
  }
  
  // Proper-time cτ, Distance light-years, Coordinate-time ct
  private Table table = new Table("%-4s", "%20.2f", "%20.2f", "%28.15f", "%28.15f");
  private Table tableHeader = new Table("%-15s", "%-22s", "%-18s", "%-28s", "%-20s");
  private static final int NUM_YEARS = 12;
}
