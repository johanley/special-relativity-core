package sr.explore.dogleg;

import java.util.ArrayList;
import java.util.List;

import sr.core.Axis;
import sr.core.Speed;
import sr.core.Util;

/** 
 Explore the range of values of the Thomas-Wigner rotation angle. 
 The range is 0..-90 degrees. It's pretty large at the high speeds.
*/
public class ThomasRotationRange {

  public static void main(String... args) {
    ThomasRotationRange range = new ThomasRotationRange();
    List<String> lines = new ArrayList<>();
    range.showRangeθw(lines);
    Util.writeToFile(ThomasRotationRange.class, "thomas-rotation-range.txt", lines);
    for(String line : lines) {
      Util.log(line);
    }
  }
  
  void showRangeθw(List<String> lines) {
    for (Speed β1 : Speed.nonExtremeValues()) {
      for (Speed β2 : Speed.nonExtremeValues()) {
        ShowEquivalence range = new ShowEquivalence(Axis.X, β1.β(), β2.β());
        Double θw = range.equivalent().θw;
        lines.add(β1.β() + " " + β2.β() + " " + Util.radsToDegs(θw));
      }
    }
  }
}
