package sr.explore.noncolinear.boost.silbersteinrotation;

import sr.core.Axis;
import sr.core.SpeedValues;
import sr.core.Util;
import sr.explore.noncolinear.boost.corner.EquivalentBoostPlusRotation;
import sr.output.text.Table;
import sr.output.text.TextOutput;

/** 
 Explore the range of values of the Silberstein (Thomas-Wigner) rotation angle. 
 The angle is large at the highest speeds.
*/
public final class SilbersteinRotationRange extends TextOutput {

  public static void main(String... args) {
    SilbersteinRotationRange range = new SilbersteinRotationRange();
    range.explore();
  }
  
  void explore() {
    add("Silberstein (Thomas-Wigner) rotation for various combined speeds.");
    add("Here, the pair of velocities are at always at right angles to each other.");
    add("");
    add(tableHeader.row("β1", "β2", "θw"));
    add(dashes(55));
    showRangeθw();
    outputToConsoleAnd("silberstein-rotation-range.txt");
  }
  
  void showRangeθw() {
    for (SpeedValues β1 : SpeedValues.nonExtremeValues()) {
      for (SpeedValues β2 : SpeedValues.nonExtremeValues()) {
        EquivalentBoostPlusRotation range = new EquivalentBoostPlusRotation(Axis.Z, β1.β(), β2.β());
        Double θw = range.equivalent().θw;
        add(table.row(β1.β(), β2.β(), round(Util.radsToDegs(θw))));
      }
    }
  }
  
  // β1, β2, θw
  private Table table = new Table("%-21s", "%-21s", "%8.2f°");
  private Table tableHeader = new Table("%-21s", "%-21s", "%6s°");
  
  private double round(double value) {
    return Util.round(value, 2);
  }

}
