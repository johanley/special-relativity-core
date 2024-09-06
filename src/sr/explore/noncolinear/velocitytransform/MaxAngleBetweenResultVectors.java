package sr.explore.noncolinear.velocitytransform;

import static sr.core.Axis.X;

import sr.core.Axis;
import sr.core.Util;
import sr.core.VelocityTransformation;
import sr.core.vector3.ThreeVector;
import sr.core.vector3.Velocity;
import sr.core.vector3.transform.SpatialRotation;
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
      findTheLargestAngleBetweenWhenAdding(Velocity.of(X, speed / 100.0));
    }
    findTheLargestAngleBetweenWhenAdding(Velocity.of(X, 0.999));
    findTheLargestAngleBetweenWhenAdding(Velocity.of(X, 0.9999));
    findTheLargestAngleBetweenWhenAdding(Velocity.of(X, 0.99999));
    
    outputToConsoleAnd("maximize-angle-between-result-vectors.txt");
  }
  
  private void findTheLargestAngleBetweenWhenAdding(Velocity a) {
    Velocity b = Velocity.of(a); //to start with
    int rotationAngle = 0;
    Velocity vWithMaxAngle = null;
    double maxAngleBetween = 0;
    for(int degrees = 1; degrees < 180; ++degrees ) {
      SpatialRotation rot = SpatialRotation.of(Axis.Z, Util.degsToRads(degrees));
      ThreeVector b_rotated = rot.changeVector(b);
      Velocity b_rotated_v = Velocity.of(b_rotated.x(), b_rotated.y(), b_rotated.z());
      Velocity sum1 = VelocityTransformation.unprimedVelocity(a, b_rotated_v);
      Velocity sum2 = VelocityTransformation.unprimedVelocity(b_rotated_v, a);
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
