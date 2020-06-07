package sr.explore.speeds;

import java.util.ArrayList;
import java.util.List;

import sr.core.SpeedValues;
import sr.core.Util;

/** 
 Simply list speeds and their corresponding Lorentz factors.
 Includes the speed of extreme cosmic rays. 
*/
public class SpeedsAndGammas {
  
  public static void main(String... args) {
    List<String> lines = new ArrayList<>();
    lines.add("# β,  Γ");
    for(SpeedValues speed : SpeedValues.values()) {
      lines.add(speed.βBigDecimal() + " " + speed.Γ());
    }
    Util.writeToFile(SpeedsAndGammas.class, "speeds-and-gammas.txt", lines);
  }

}
