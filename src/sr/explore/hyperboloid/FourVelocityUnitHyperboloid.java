package sr.explore.hyperboloid;

import static sr.core.Axis.X;
import static sr.core.Util.NL;

import sr.core.KinematicRotation;
import sr.core.Util;
import sr.core.VelocityTransformation;
import sr.core.vec3.Direction;
import sr.core.vec3.Velocity;
import sr.core.vec4.FourVelocity;
import sr.explore.Exploration;
import sr.output.text.TextOutput;
import sr.core.HyperbolicTriangle;

/**
 Perform velocity addition by solving a geometry problem in a curved Riemannian space with constant negative curvature, 
 a <a href='https://en.wikipedia.org/wiki/Hyperbolic_space'>hyperbolic space</a>.
 
 <P>In special relativity, space-time is flat.
 It's interesting to note, however, that there's an aspect of special relativity in which curved geometry plays an important role.
 That aspect is four-velocity <em>u<sup>i</sup></em>. 
 
 <P>The four-velocity has these unusual properties:
 <ul>
  <li>its components are dimensionless.
  <li>its square-magnitude is constant (+1 with the conventions used in this project).
 </ul>
  
 <P>The second property means that the "tip" of the 4-velocity is always confined to the future-branch of a unit hyperboloid.
 A unit hyperboloid is one kind of chart of the <em>hyperbolic plane</em>, a curved Riemannian space with constant negative curvature.
    
 <P>The unit hyperboloid is space-like.
 With the conventions being used here, we need to take the <em>negative</em> of the space-time interval in order 
 to conform with the requirement that its 'distances' zero or more.
*/
public final class FourVelocityUnitHyperboloid extends TextOutput implements Exploration {
  
  public static void main(String[] args) {
    Exploration hyperbolicTriangle = new FourVelocityUnitHyperboloid();
    hyperbolicTriangle.explore();
  }
  
  @Override public void explore() {
    triangleOfFourVelocities();
    compareToOtherCalculations();
    outputToConsoleAnd("four-velocity-unit-hyperboloid.txt");
  }

  /** An object at rest in frame K'. */
  private Velocity at_rest_in_Kp = Velocity.of(0.0, 0.0, 0.0);

  /** A boost from K to K'. */
  private Velocity boost_K_to_Kp = Velocity.of(0.5, X);
  
  /** A boost from K' to K''. */
  private Velocity boost_Kp_to_Kpp = Velocity.of(0.5, Direction.of(1.0, 1.0, 0));

  /** 
   In a frame K', there are two non-colinear velocities.
   Form a triangle on the unit hyperboloid in which one corner corresponds to an object at rest in K', and the 
   other two corners correspond to the two given four-velocities (for K and K'').
   This makes a side-angle-side problem in hyperbolic geometry, with the 'angle' vertex corresponding to zero velocity.
   This is equivalent to solving the velocity addition problem, in the non-colinear case.
   It's also equivalent to finding the kinematic (Wigner) rotation.
  */
  private void triangleOfFourVelocities() {
    add("Elbow-boost from K to K' to K''.");
    add("Take the point of view of K'.");
    FourVelocity a = FourVelocity.of(boost_K_to_Kp.reverseSpatialAxes());
    FourVelocity b = FourVelocity.of(at_rest_in_Kp);  
    FourVelocity c = FourVelocity.of(boost_Kp_to_Kpp);
    add("There are three 4-velocities to consider in the frame K':");
    add("  " + a + " negative of the boost from K to K'.");
    add("  " + b + " at rest in K'.");
    add("  " + c + " the boost from K' to K''.");
    HyperbolicTriangle triangle = HyperbolicTriangle.fromFourVelocities(a, b, c);
    
    add(NL+"** This triplet of 4-velocities make a triangle on the unit hyperboloid. **");
    add("This is because the squared-magnitude of any 4-velocity is always +1 (when c=1).");
    add("Values for the sides of the triangle correspond to the (integrated) space-time interval along an arc on the hyperboloid.");
    add("The unit hyperboloid inherits its metric from the space-time pseudo-metric.");
    
    add(NL+"This becomes a simple problem in hyperbolic geometry.");
    add("You solve it using hyperbolic analogues of the cosine-law and the sine-law from Euclidean geometry.");
    add(NL+"Here's the result, showing the full data for the triangle (with sides 'ABC', and angles 'abc'):");
    add(NL+triangle);
    double speed_Kpp = Math.tanh(triangle.B);
    double angle_Kpp = triangle.a;
    Direction direction_Kpp = Direction.of(Math.cos(angle_Kpp), Math.sin(angle_Kpp), 0.0);
    Velocity v_K = Velocity.of(speed_Kpp, direction_Kpp);
    
    add(NL+"The triangle yields two results of interest:");
    add("  - the addition of two non-co-linear velocities");
    add("  - the corresponding kinematic (Wigner) rotation (whose size is the angular-defect of the triangle)");
    
    add(NL+"The velocity of K'' with respect to K is computed using the triangle's data:");
    add("  Speed comes from tanh(B): " + rounded(speed_Kpp));
    add("  Direction with respect to the +X-axis is the angle 'a': " + rounded(angle_Kpp));
    add("  Hence the velocity of K'' with respect to K is: ");
    add("    Velocity: " + v_K);
    add("    Four-velocity: " + FourVelocity.of(v_K));
    
    add(NL+"The magnitude of the corresponding kinematic (Wigner) rotation equals the angular-defect of the triangle: " + rounded(triangle.angularDefect()));
  }

  private void compareToOtherCalculations() {
    add(NL+dashes(100));
    add(NL+"Now use other techniques to calculate the same things, and compare the results.");
    
    add(NL+"For velocity addition, the alternate calculation gives:");
    Velocity unprimed = VelocityTransformation.unprimedVelocity(boost_K_to_Kp, boost_Kp_to_Kpp);
    add("  Velocity: " + unprimed);
    add("  Four-velocity: " + FourVelocity.of(unprimed));
    
    add(NL+"For the corresponding kinematic (Wigner) rotation, the alternate calculation gives:");
    KinematicRotation kinematicRotation = KinematicRotation.of(boost_K_to_Kp, boost_Kp_to_Kpp);
    add("  θw: " + rounded(kinematicRotation.θwAngleBetweenTwoResultants()));
    add(NL+"These agree with the above results.");
  }
  
  private double rounded(double val) {
    return Util.round(val, 5);
  }
}
