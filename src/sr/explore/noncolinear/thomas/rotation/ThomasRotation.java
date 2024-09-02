package sr.explore.noncolinear.thomas.rotation;

import sr.core.Axis;
import sr.core.Util;
import sr.core.history.timelike.CircularMotion;
import sr.core.history.timelike.TimelikeDeltaBase;
import sr.core.history.timelike.TimelikeMoveableHistory;
import sr.core.vector3.AxisAngle;
import sr.core.vector3.Position;
import sr.core.vector3.Velocity;
import sr.core.vector4.Event;
import sr.output.text.Table;
import sr.output.text.TextOutput;

/**
 Circular motion, and rotation of the K' frame co-moving with the object.   
*/
public final class ThomasRotation extends TextOutput {
  
  public static void main(String[] args) {
    ThomasRotation rotation = new ThomasRotation();
    rotation.explore();
  }
  
  void explore() {
    double radius = 10.0;
    double β = 0.2;
    circleDetails(radius, β);
    rotationAfterOneFullCircle(radius);
    outputToConsoleAnd("thomas-rotation.txt");
  }

  //details
  //ct, phase, rot, pos, velo, with respect to the +X-axis 
  private Table header = new Table("%-7s", "%-10s", "%-12s", "%-24s", "%-26s");
  private Table table = new Table("%-4s", "%8.3f°", "%10.3f°   ", "%-24s", "%-24s");
  
  //v, r, rotation corresponding to one full circle
  private Table header2 = new Table("%-8s", "%-12s", "%-20s");
  private Table table2 = new Table("%6.2f", "%8.3f", "%12.3f°");
  
  private void circleDetails(double radius, double β) {
    add("Circular motion, and rotation of the K' frame co-moving with the object (Thomas precession).");
    add(Util.NL+"Details of a single revolution.");
    add(Util.NL+"Radius: " + radius + " Speed:" + β);
    add(Util.NL+"The negative sign of Silberstein rotation (θw) indicates its direction is opposite to that of the circular motion.");
    
    add(Util.NL + header.row("ct", "Phase", "Rotation", "Position", "Velocity"));
    add(header.row("", "", "θw", "", ""));
    add(dashes(80));
    TimelikeMoveableHistory circle = CircularMotion.of(TimelikeDeltaBase.of(Position.origin()), radius, β, Axis.Z, 0.0);
    long ct_one_rev = Math.round(2*Math.PI * radius / β) + 5;
    for(int ct = 0; ct <= ct_one_rev; ++ct) {
      Event e = circle.event(ct);
      Velocity v = circle.velocity(ct);
      AxisAngle rot = circle.rotation(ct);
      add(table.row(ct, deg(phase(e.position())), -deg(rot.magnitude()), e.position(), v)); 
    }
  }
  
  private void rotationAfterOneFullCircle(double radius) {
    add(Util.NL + dashes(100));
    add(Util.NL + "Silberstein rotation after a single revolution becomes extreme in the ultra-relativistic case." + Util.NL);
    add(header2.row("  β", "Radius", "Rotation θw"));
    add(dashes(31));
    for(int i = 1; i <= 99; ++i) {
      double β = i / 100.0;
      TimelikeMoveableHistory circle = CircularMotion.of(TimelikeDeltaBase.of(Position.origin()), radius, β, Axis.Z, 0.0);
      double ct_one_rev = 2*Math.PI * radius / β;
      AxisAngle rotation = circle.rotation(ct_one_rev);
      add(table2.row(β, radius, -deg(rotation.magnitude())));
    }
  }
  
  private double deg(double value) {
    return Util.round(Util.radsToDegs(value), 3);
  }
  
  /** 0..2pi. */ 
  private double phase(Position position) {
    double result = Math.atan2(position.y(), position.x());
    if (result < 0) {
      result = result + 2 * Math.PI;
    }
    return result;
  }
}
