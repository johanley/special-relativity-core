package sr.explore.velocitytransform;

import static sr.core.Axis.X;

import sr.core.Axis;
import sr.core.NVelocityTransformation;
import sr.core.Util;
import sr.core.component.ops.NSense;
import sr.core.vec3.NAxisAngle;
import sr.core.vec3.NThreeVector;
import sr.core.vec3.NVelocity;
import sr.explore.Exploration;
import sr.output.text.Table;
import sr.output.text.TextOutput;

/** 
  Find the angle between v_a and v_b (same speeds) which maximizes the resulting angle between (v_a + v_b) and (v_b + v_a).
*/
public final class MaxAngleBetweenResultVectors extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    MaxAngleBetweenResultVectors velocity = new MaxAngleBetweenResultVectors();
    velocity.explore();
  }

  @Override public void explore() {
    add("Velocity transformation formula for v, the unprimed velocity." + Util.NL);
    add("Find the angle between v_a and v_b (same speeds) which maximizes the resulting angle between (v_a + v_b) and (v_b + v_a).");  
    add("Rotate v_b using Z as the pole (using an integral number of degrees)." + Util.NL);

    add("For low speeds, the 90.0° angle gives the max separation between (v_a + v_b) and (v_b + v_a).");
    add("As speeds become extreme, the angle approaches 180° as a limit." + Util.NL);

    add("(This doesn't explore the case of velocities having different speeds.)" + Util.NL);

    add(header.row("v_a", "v_b", "Rotation", "Angle Between"));
    add(header.row("", "", "v_b wrt v_a", "(v_a + v_b) and (v_b + v_a)"));
    add(dashes(120));
    
    for (int speed = 1; speed < 100; ++speed) {
      findTheLargestAngleBetweenWhenAdding(NVelocity.of(speed / 100.0, X));
    }
    findTheLargestAngleBetweenWhenAdding(NVelocity.of(0.999, X));
    findTheLargestAngleBetweenWhenAdding(NVelocity.of(0.9999, X));
    findTheLargestAngleBetweenWhenAdding(NVelocity.of(0.99999, X));
    
    outputToConsoleAnd("maximize-angle-between-result-vectors.txt");
  }
  
  private void findTheLargestAngleBetweenWhenAdding(NVelocity a) {
    NVelocity b = NVelocity.of(a); //to start with
    int rotationAngle = 0;
    NVelocity vWithMaxAngle = null;
    double maxAngleBetween = 0;
    for(int degrees = 1; degrees < 180; ++degrees ) {
      NThreeVector b_rotated = b.rotate(NAxisAngle.of(Util.degsToRads(degrees), Axis.Z), NSense.ChangeComponents);
      NVelocity b_rotated_v = NVelocity.of(b_rotated.x(), b_rotated.y(), b_rotated.z());
      NVelocity sum1 = NVelocityTransformation.unprimedVelocity(a, b_rotated_v);
      NVelocity sum2 = NVelocityTransformation.unprimedVelocity(b_rotated_v, a);
      double angleBetween = sum2.angle(sum1);
      if (angleBetween > maxAngleBetween) {
        maxAngleBetween = angleBetween;
        rotationAngle = degrees;
        vWithMaxAngle = b_rotated_v;
      }
    }
    add(table.row(a, vWithMaxAngle, rotationAngle * 1.0, Util.radsToDegs(maxAngleBetween)));
  }

  // v_a, v_b, angle between v_a and v_b, angle between (v_a + v_b) and (v_a + v_b)
  private Table table = new Table("%-25s", "%-25s", "%8.1f°", "%12.5f°");
  private Table header = new Table("%-25s", "%-25s", "%-15s", "%-15s");

}
