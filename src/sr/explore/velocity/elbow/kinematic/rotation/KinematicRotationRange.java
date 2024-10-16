package sr.explore.velocity.elbow.kinematic.rotation;

import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;
import static sr.core.Util.NL;
import static sr.core.Util.radsToDegs;
import static sr.core.Util.round;

import sr.core.KinematicRotation;
import sr.core.SpeedValues;
import sr.core.Util;
import sr.core.component.ops.Sense;
import sr.core.vec3.AxisAngle;
import sr.core.vec3.Velocity;
import sr.explore.Exploration;
import sr.output.text.Table;
import sr.output.text.TextOutput;

/** 
 Explore the range of values of the kinematic rotation angle (Wigner rotation angle). 
 
 <P>In general, the extreme max angle is π. 
 This is approached when the velocities are in nearly the opposite direction, and both speeds are ultra-relativistic.
 In hyperbolic geometry, this corresponds to the maximum <em>angular defect</em> of a triangle. 
*/
public final class KinematicRotationRange extends TextOutput implements Exploration {

  public static void main(String... args) {
    Exploration range = new KinematicRotationRange();
    range.explore();
  }
  
  @Override public void explore() {
    add("Kinematic (Wigner) rotation (θw) for various combined speeds.");
    add("Here, the two velocities are always at right angles to each other.");
    add("");
    add(tableHeader.row("β1", "β2", "|θw|"));
    add(dashes(55));
    showRangeθwForCornerBoosts(); 
    
    add(dashes(55));
    findAngleHavingMaxRange();
    outputToConsoleAnd("kinematic-rotation-range.txt");
  }
  
  void showRangeθwForCornerBoosts() {
    for (SpeedValues β1 : SpeedValues.nonExtremeValues()) {
      for (SpeedValues β2 : SpeedValues.nonExtremeValues()) {
        Velocity veloOne = Velocity.of(β1.β(), X);
        Velocity veloTwo = Velocity.of(β2.β(), Y);
        KinematicRotation kr = KinematicRotation.of(veloOne, veloTwo);
        add(table.row(
          β1.β(), 
          β2.β(), 
          rounded(radsToDegs(kr.rotation().magnitude()))
        ));
      }
    }
  }
  
  void findAngleHavingMaxRange() {
    add(NL+"For various speeds, find the largest value of θw and the associated angle between the two velocities.");
    add("Step through directions 0.1° at a time, from 0.0° to 180.0°"+NL);
    add(table_max_header.row("β", "Max θw", "Angle Between Velocities"));
    add(dashes(60));
    double maxθw = 0.0;
    double maxDeg = 0.0;
    for (SpeedValues speed : SpeedValues.upToFourNines()) {
      double maxSpeed = speed.β();
      Velocity veloOne = Velocity.of(maxSpeed, 0.0, 0.0); //this style avoids a coerced-to-1 issue 
      for (int deg = 0; deg < 1800; ++deg) {
        double rotateBy = Util.degsToRads(deg/10.0); //watch out for integer div!
        Velocity veloTwo = veloOne.rotate(AxisAngle.of(rotateBy, Z), Sense.ChangeComponents);
        KinematicRotation kr = KinematicRotation.of(veloOne, veloTwo);
        double θw = kr.θwAngleBetweenTwoResultants();
        if (θw > maxθw) {
          maxθw = θw;
          maxDeg = deg;
        }
      }
      add(table_max.row(maxSpeed, rounded(Util.radsToDegs(maxθw)), maxDeg/10.0));
    }
    add(NL+"At lower speeds, the maximum θw occurs when the velocities are nearly perpendicular.");
    add("At higher speeds, the maximum θw occurs when the velocities are nearly anti-parallel.");
    add(NL+"The limiting max value of θw is π.");
    add("This corresponds to the maximum angular-defect of a triangle in hyperbolic geometry.");
  }
  
  // β1, β2, θw
  private Table table = new Table("%-21s", "%-21s", "%8.2f°");
  private Table tableHeader = new Table("%-21s", "%-23s", "%6s°");
  
  // β, θw, angle
  private Table table_max = new Table("%-21s", "%8.2f°", "%8.2f°");
  private Table table_max_header = new Table("%-24s", "%6s°  ", "%6s");
  
  private double rounded(double value) {
    return round(value, 2);
  }
}