package sr.explore.accel.speed;

import static sr.core.Physics.ONE_GEE;

import sr.core.Axis;
import sr.core.Util;
import sr.core.history.timelike.TimelikeMoveableHistory;
import sr.core.history.timelike.UniformAcceleration;
import sr.core.vector3.Position;
import sr.core.vector4.Event;
import sr.explore.Exploration;
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
 An electron in a uniform electric field follows this type of history.
 */
public final class OneGeeForever extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    OneGeeForever oneGee = new OneGeeForever();
    oneGee.explore();
  }
  
  @Override public void explore() {
    add("Travel in a relativistic rocket accelerating at 1g.");
    add("If light-years and years are used as units, then 1g has the numeric value of " + ONE_GEE + ".");
    add("The Milky Way galaxy is about 100,000 light years across.");
    add("You would cross the Milky Way in about 12 years of proper-time."+ Util.NL);
    table();
    
    outputToConsoleAnd("one-gee-forever.txt");
  }

  private void table() {
    add(tableHeader.row("Proper-time", "Coordinate-distance", "Coordinate-time", "Speed", "Γ"));
    add(tableHeader.row("(years)", "(light-years)", "(years)", "(β)", ""));
    add(dashes(100));
    for(int yearsProperTime = 1; yearsProperTime <= NUM_YEARS; ++yearsProperTime) {
      explore(yearsProperTime);
    }
  }
 
  private void explore(int yearsProperTime) {
    TimelikeMoveableHistory history = UniformAcceleration.of(Position.origin(), Axis.X, ONE_GEE);
    Event endsAt = history.eventFromProperTime(yearsProperTime);
    double coordinateTime = endsAt.ct();
    double terminalSpeed = history.velocity(coordinateTime).magnitude();
    double terminalGamma = history.velocity(coordinateTime).Γ();
    add(table.row(yearsProperTime, endsAt.x(), coordinateTime, terminalSpeed, terminalGamma));
  }
  
  // Proper-time cτ, Distance light-years, Coordinate-time ct
  private Table table = new Table("%-4s", "%20.2f", "%20.2f", "%28.15f", "%28.15f");
  private Table tableHeader = new Table("%-15s", "%-22s", "%-18s", "%-28s", "%-20s");
  private static final int NUM_YEARS = 12;
}
