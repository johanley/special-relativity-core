package sr.explore.speeds;

import sr.core.Speed;
import sr.core.Util;
import sr.output.text.Table;
import sr.output.text.TextOutput;

/** 
 Simply list speeds and their corresponding Lorentz factors.
 Includes the speed of extreme cosmic rays. 
*/
public final class SpeedsAndGammas extends TextOutput {
  
  public static void main(String... args) {
    SpeedsAndGammas speeds = new SpeedsAndGammas();
    speeds.explore();
  }
  
  void explore() {
    lines.add("The Lorentz factor Γ as a function of speed β=v/c.");
    lines.add("I find it useful to remember approximate values for β=0.99 and β=0.999." + Util.NL);
    lines.add(table.row("β", "Γ"));
    lines.add(dashes(50));
    for(Speed speed : Speed.values()) {
      lines.add(table.row(speed.βBigDecimal(), speed.Γ()));
    }
    outputToConsoleAnd("speeds-and-gammas.txt");
  }
  
  private Table table = new Table("%-32s", "%-20s");
}
