
package sr.explore.speeds;

import java.util.ArrayList;
import java.util.List;

import sr.core.Speed;
import sr.core.Util;
import sr.explore.Table;

/** 
 Simply list speeds and their corresponding Lorentz factors.
 Includes the speed of extreme cosmic rays. 
*/
public class SpeedsAndGammas {
  
  public static void main(String... args) {
    Table table = new Table("%-32s", "%-20s");
    List<String> lines = new ArrayList<>();
    lines.add("Lorentz factor Γ as a function of speed β=v/c:" + Util.NL);
    lines.add(table.row("β", "Γ"));
    lines.add(Util.separator(50));
    for(Speed speed : Speed.values()) {
      lines.add(table.row(speed.βBigDecimal(), speed.Γ()));
    }
    for(String line : lines) {
      System.out.println(line);
    }
    Util.writeToFile(SpeedsAndGammas.class, "speeds-and-gammas.txt", lines);
  }
}
