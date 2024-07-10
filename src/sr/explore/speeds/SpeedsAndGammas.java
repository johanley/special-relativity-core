
package sr.explore.speeds;

import sr.core.Speed;
import sr.core.Util;
import sr.explore.Table;
import sr.explore.TextOutput;

/** 
 Simply list speeds and their corresponding Lorentz factors.
 Includes the speed of extreme cosmic rays. 
*/
public class SpeedsAndGammas extends TextOutput {
  
  public static void main(String... args) {
    SpeedsAndGammas speeds = new SpeedsAndGammas();
    speeds.execute();
  }
  
  void execute() {
    lines.add("The Lorentz factor Γ as a function of speed β=v/c:" + Util.NL);
    lines.add(table.row("β", "Γ"));
    lines.add(Util.separator(50));
    for(Speed speed : Speed.values()) {
      lines.add(table.row(speed.βBigDecimal(), speed.Γ()));
    }
    outputLines("speeds-and-gammas.txt");
  }
  
  private Table table = new Table("%-32s", "%-20s");
}
