package sr.explore.velocity.hyperboloid;

import static sr.core.Util.NL;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.Util;

import static sr.core.Util.*;
import sr.core.component.Position;
import sr.core.hist.timelike.CircularMotion;
import sr.core.hist.timelike.TimelikeDeltaBase;
import sr.core.hist.timelike.TimelikeMoveableHistory;
import sr.core.vec3.AxisAngle;
import sr.core.vec3.Velocity;
import sr.core.vec4.FourVelocity;
import sr.explore.Exploration;
import sr.output.text.Table;
import sr.output.text.TextOutput;

/**
 Kinematic (Wigner) rotation after one revolution equals the area of the 
 corresponding circle on the unit hyperboloid (related to four-velocity).
 
 <P>In general, kinematic rotation equals the area "swept out" by the four-velocity  
 on the unit hyperboloid, using its apex (1,0,0,0) as the center of the "sweep".
*/
public final class KinematicRotationEqualsCircleArea extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    Exploration rotation = new KinematicRotationEqualsCircleArea();
    rotation.explore();
  }
  
  @Override public void explore() {
    double radius = 10.0;
    rotationAfterOneFullCircle(radius);
    outputToConsoleAnd("kinematic-rotation-equals-circle-area.txt");
  }

  //v, r, rotation corresponding to one full circle, area on the unit hyperboloid
  private Table header = new Table("%-8s", "%-12s", "%-13s", "%-20s");
  private Table table = new Table("%6.2f", "%8.3f", "%12.5f", "%12.5f");
  
  private void rotationAfterOneFullCircle(double radius) {
    add("Kinematic rotation (Wigner rotation) equals the angular-defect of a triangle on the unit hyperboloid in four-velocity space.");
    add("The angular-defect of such a triangle also equals the *area* of the triangle.");
    add(NL+"For circular motion, the angular-defect doesn't apply, since there's no triangle.");
    add("But there is a corresponding area - the area of a circle on the unit hyperboloid.");
    add(NL+"The kinematic rotation (Wigner rotation) after a single revolution equals" );
    add("the area of the corresponding circle on the unit hyperboloid in four-velocity space.");
    add("That's simply the area that the four-velocity has 'swept out' on the unit hyperboloid." + NL);
    add(header.row("  β", "Circle", "Rotation θw", "Area of Circle"));
    add(header.row("", "Radius", "(rads)", "On Unit Hyperboloid"));
    add(dashes(75));
    for(int i = 1; i <= 99; ++i) {
      double β = i / 100.0; //avoid integer division
      TimelikeMoveableHistory circle = CircularMotion.of(TimelikeDeltaBase.of(Position.origin()), radius, β, Axis.Z, 0.0);
      double ct_one_rev = 2 * Math.PI * radius / β;
      AxisAngle rotation = circle.rotation(ct_one_rev);
      add(table.row(
        β, 
        radius, 
        rounded(rotation.magnitude()), 
        rounded(areaCircleOnUnitHyperboloid(β))
      ));
    }
  }
  
  private double areaCircleOnUnitHyperboloid(double β) {
    //https://www.whitman.edu/Documents/Academics/Mathematics/2014/brewert.pdf
    double r = arcInterval(β);
    return 2 * Math.PI * (Math.cosh(r) - 1); 
  }
  
  private double arcInterval(double β) {
    FourVelocity at_rest = FourVelocity.of(Velocity.zero());
    FourVelocity u = FourVelocity.of(β, Axis.X);
    return arc_cosh(at_rest.dot(u));
  }
  
  /** Some might prefer this style. I prefer the other, since it's more general. */
  @SuppressWarnings("unused")
  private double arcInterval2(double β) {
    return Util.arc_tanh(β);
  }

  /** This simple alternate calc gives the same result. */
  private double kinematicRotationAfterOneRev(double β) {
    //https://galileoandeinstein.phys.virginia.edu/Elec_Mag/2022_Lectures/EM_68_Thomas_Precession.html
    return 2 * Math.PI * (1 - Physics.Γ(β));
  }
  
  private double rounded(double value) {
    return round(value, 5);
  }
}