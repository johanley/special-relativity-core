package sr.explore.dogleg;

import sr.core.Axis;
import sr.core.Speed;
import sr.core.Util;
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
    lines.add("Silberstein (Thomas-Wigner) rotation for various combined speeds.");
    lines.add("Here, the pair of velocities are at always at right angles to each other.");
    lines.add("");
    lines.add(tableHeader.row("β1", "β2", "θw"));
    lines.add(dashes(55));
    showRangeθw();
    outputToConsoleAnd("silberstein-rotation-range.txt");
  }
  
  void showRangeθw() {
    for (Speed β1 : Speed.nonExtremeValues()) {
      for (Speed β2 : Speed.nonExtremeValues()) {
        ShowEquivalence range = new ShowEquivalence(Axis.X, β1.β(), β2.β());
        Double θw = range.equivalent().θw;
        lines.add(table.row(β1.β(), β2.β(), round(Util.radsToDegs(θw))));
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
