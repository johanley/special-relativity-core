
package sr.explore.speeds;

import java.util.ArrayList;
import java.util.List;

import sr.core.Speed;
import sr.core.Util;

/** 
 Simply list speeds and their corresponding Lorentz factors.
 Includes the speed of extreme cosmic rays. 
*/
public class SpeedsAndGammas {
  
  public static void main(String... args) {
    List<String> lines = new ArrayList<>();
    lines.add("Lorentz factor Γ as a function of speed β=v/c:" + Util.NL);
    lines.add(column(1,"β") + column(2,"Γ"));
    lines.add(Util.separator(50));
    for(Speed speed : Speed.values()) {
      lines.add(column(1, speed.βBigDecimal()) + column(2, speed.Γ()));
    }
    for(String line : lines) {
      System.out.println(line);
    }
    Util.writeToFile(SpeedsAndGammas.class, "speeds-and-gammas.txt", lines);
  }
  
  private static final String[] COLS = {"%-32s", "%-20s"};
  
  private static String column(int idx, Object thing) {
    return String.format(COLS[idx-1], thing);
  }
}
