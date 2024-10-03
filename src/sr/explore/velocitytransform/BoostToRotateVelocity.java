package sr.explore.velocitytransform;

import static sr.core.Util.NL;
import static sr.core.Util.degsToRads;
import static sr.core.Util.round;

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
  Find a boost that will rotate a given {@link NVelocity} by a given amount.
  Here the boost changes the direction of a velocity, but not its speed.
  
  <P>Set up:
  <ul>
    <li>frame K
    <li>second frame K' moving with velocity <em>+boost_v</em> with respect to K 
  </ul>
  
  <P>Calculate the boost with respect to K' need to generate a velocity in K which equates to <em>boost_v</em> rotated by a given angle.
  
  <P>All velocities involved define a plane, taken here as the XY plane.
  
  <P>For the sense of the rotation, use the right-handed rule, with the original <em>boost_v</em> in the +X direction, and the 
  {@link NAxisAngle} of the rotation directed in the +Z-direction.  
*/
public final class BoostToRotateVelocity extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    BoostToRotateVelocity rotated_v = new BoostToRotateVelocity();
    rotated_v.explore();
  }
  
  @Override public void explore() {
    rotateBy(NVelocity.of(0.6, 0.0, 0.0), 20.0);
    tableForManyDegrees();
    outputToConsoleAnd("boost-to-rotate-velocity.txt");
  }

  /** @param angle in degrees   */
  private void rotateBy(NVelocity boost_v, double angle) {
    add("Find a boost that will rotate a velocity vector by " + angle + "°."+NL);
    NVelocity boost_v_rotated = rotated(boost_v, degsToRads(angle));
    
    // we throw a ball in K', whose v' in K equates to boost_v_rotated
    // we can calc the v' of that throw like so:
    NVelocity v_Kp = NVelocityTransformation.primedVelocity(boost_v, boost_v_rotated);

    show("boost_v_K ", boost_v);
    show("boost_v_rotated_K ", boost_v_rotated);
    show("The desired boost needed in K' is v_Kp ", v_Kp);
    
    add(NL+"As a check, re-do the calculation using the formula that takes v_Kp as a parameter.");
    NVelocity v_K = NVelocityTransformation.unprimedVelocity(boost_v, v_Kp);
    show("v_K (same as boost_v_rotated) ", v_K);
  }
  
  private NVelocity rotated(NVelocity boost_v, double angle) {
    return boost_v.rotate(NAxisAngle.of(angle, Axis.Z), NSense.ChangeComponents);
  }
  
  private void show(String msg, NThreeVector vector) {
    add(msg +  vector + " size " + roundIt(vector.magnitude()));
  }
  
  private double roundIt(double value) {
    return round(value, 6);
  }

  // boost, rotation needed, new rotated vector
  private Table table = new Table("%-25s", "%-12s", "%-25s");
  private Table header = new Table("%-25s", "%-12s", "%-25s");
  
  private void tableForManyDegrees() {
    add(NL+ dashes(100) + NL);
    NVelocity v = NVelocity.of(0.6, 0.0, 0.0);
    add("Table of various angles for rotating the following velocity with a boost."+NL);
    add("V=" + v + NL);
    add(header.row("Boost that", "Rotation ", "Rotated V"));
    add(header.row("rotates V (rounded)", "amount", "(rounded)"));
    add(dashes(70));
    for(int degrees = 1; degrees <= 179; ++degrees) {
      NVelocity boost_v_rotated = rotated(v, degsToRads(degrees));
      NVelocity v_Kp = NVelocityTransformation.primedVelocity(v, boost_v_rotated);
      add(table.row(v_Kp, degrees + "°", boost_v_rotated + " (" + angleFromVector(boost_v_rotated) + "°)"));
    }
  }
  
  private double angleFromVector(NVelocity boost_v_rotated) {
    return roundIt(Util.radsToDegs(Math.atan2(boost_v_rotated.y(), boost_v_rotated.x())));
  }

}
