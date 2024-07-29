package sr.explore.accel.linear;

import sr.core.Axis;
import sr.core.Util;
import sr.core.event.Event;
import sr.core.history.MoveableHistory;
import sr.core.history.UniformAcceleration;
import sr.core.vector.Position;
import sr.output.text.Table;
import sr.output.text.TextOutput;

public final class OneGee extends TextOutput {
  
  public static void main(String[] args) {
    OneGee oneGee = new OneGee();
    oneGee.explore();
  }
  
  void explore() {
    lines.add("Travel in a relativistic rocket accelerating at 1g.");
    lines.add("If light-years and years are used as units, then 1g has the numeric value of " + ONE_GEE + ".");
    lines.add("The Milky Way galaxy is about 100,000 light years across.");
    lines.add("You would cross the Milky Way in about 12 years of proper-time."+ Util.NL);
    table(1);
    
    lines.add(Util.NL + "If you multiply the above results by 4, then that data represents a there-and-back trip,");
    lines.add("with a turnarounds at the 1/4-way and 3/4-way points of the trip."+Util.NL);
    table(4);
    
    outputToConsole();
  }

  /** The numeric value of 1g, expressed using light-years as the distance unit and year as the time-unit. {@value}. */
  public static double ONE_GEE = 1.03; //light-years, year as the unit!

  private void table(int mult) {
    lines.add(tableHeader.row("Proper-time", "Coordinate-distance", "Coordinate-time"));
    lines.add(tableHeader.row("(years)", "(light-years)", "(years)"));
    for(int yearsProperTime = 1; yearsProperTime <= NUM_YEARS; ++yearsProperTime) {
      explore(yearsProperTime, mult);
    }
  }
 
  private void explore(int yearsProperTime, int mult) {
    MoveableHistory history = UniformAcceleration.of(Position.origin(), Axis.X, ONE_GEE);
    Event endsAt = history.eventFromProperTime(yearsProperTime);
    double coordinateTime = endsAt.ct();
    lines.add(table.row(mult * yearsProperTime, mult*endsAt.x(), mult*coordinateTime));
  }
  
  // Proper-time cτ, Distance light-years, Coordinate-time ct
  private Table table = new Table("%-4s", "%20.2f", "%20.2f");
  private Table tableHeader = new Table("%-15s", "%-22s", "%-20s");
  private static final int NUM_YEARS = 12;
}
