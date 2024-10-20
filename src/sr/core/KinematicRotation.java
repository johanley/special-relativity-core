package sr.core;

import sr.core.vec3.AxisAngle;
import sr.core.vec3.Direction;
import sr.core.vec3.ThreeVector;
import sr.core.vec3.Velocity;
import sr.core.vec4.FourVelocity;

/** 
 Kinematic (Wigner) rotation corresponding to the addition of two velocities.
 A non-zero result is returned only if the two velocities are non-zero, and not in the same line.
*/
public final class KinematicRotation {

  /** 
   Factory method. 
   The speeds must be in the range [0,1), excluding 1.
   
   <P>If either speed is 0, then the kinematic rotation is 0.
   If the velocities are along the same line, then the kinematic rotation is 0.
   
   <P>The order of the parameters is significant.
   This class adds the two velocities in the following sense 
   (see {@link VelocityTransformation#unprimedVelocity(Velocity, Velocity)}):
   <code>VelocityTransformation.unprimedVelocity(veloOne, veloTwo);</code> 
   
   <P>The axis of kinematic rotation is parallel to the direction of the cross product <em>veloTwo x veloOne</em>. 
  */
  public static KinematicRotation of(Velocity veloOne, Velocity veloTwo) {
    return new KinematicRotation(veloOne, veloTwo);
  }  
  /**
   The full kinematic (Wigner) rotation as a rotation, with both direction and magnitude.
   The direction is parallel to the direction of the cross-product <em>veloTwo x veloOne</em>.
   
   @return (0,0,0) if either speed is 0, or if the velocities are in the same line. 
  */
  public AxisAngle rotation() {
    AxisAngle result = AxisAngle.zero();
    if (noZeroes()) {
      if (!sameLine()) {
        Direction direction = Direction.of(veloTwo.cross(veloOne));
        ThreeVector rot = direction.times(θw());
        result = AxisAngle.of(rot.x(), rot.y(), rot.z());
      }
    }
    return result;
  }
  
  /** 
   The magnitude of the kinematic (Wigner) rotation angle.
  
   <P>This implementation calculates the angular defect of a triangle on the unit hyperboloid, 
   generated by the given velocities, and a velocity at rest.
   (All velocities are converted to four-velocities.)
  
   @return range 0..π; returns 0 if either speed is 0, or if the velocities are in the same line. 
  */
  public double θw() {
    double result = 0.0;
    if (noZeroes()) {
      if (!sameLine()) {
        //Important: use the idea that v1 is a boost from K to K', and v2 is a boost from K' to K''
        //use the perspective of the intermediate frame K' to form the triangle, with one v at rest
        Velocity veloOneRev_Kp = Velocity.of(veloOne.times(-1));
        FourVelocity a_Kp = FourVelocity.of(veloOneRev_Kp);
        FourVelocity at_rest_Kp = FourVelocity.of(Velocity.zero());
        FourVelocity c_Kp = FourVelocity.of(veloTwo);
        //the velo's need to be in the same frame here!:
        HyperbolicTriangle triangle = HyperbolicTriangle.fromFourVelocities(a_Kp, at_rest_Kp, c_Kp);
        result = triangle.angularDefect();
      }
    }
    return result;
  }

  /** 
   The magnitude of the kinematic (Wigner) rotation angle.
   
   <P>This implementation uses the technique described in Silberstein's relativity 
   <a href='https://archive.org/details/theoryofrelativi00silbrich/page/n7/mode/2up'>textbook</a>. 
   It calculates the angle between the two resultant vectors <em>veloOne + veloTwo</em> and <em>veloTwo + veloOne</em>.
  
   @return range 0..π; returns 0 if either speed is 0, or if the velocities are in the same line. 
  */
  public double θwAngleBetweenTwoResultants() {
    double result = 0.0;
    if (noZeroes()) {
      if (!sameLine()) {
        Velocity a = veloAddition();
        Velocity b = veloAdditionReversed();
        result = a.angle(b);
      }
    }
    return result;
  }

  private Velocity veloOne;
  private Velocity veloTwo;

  private KinematicRotation(Velocity one, Velocity two) {
    check(one, two);
    this.veloOne = one;
    this.veloTwo = two;
  }

  private Velocity veloAddition() {
    return VelocityTransformation.unprimedVelocity(
      veloOne, 
      veloTwo 
    );
  }
  
  /** Reverse the order of parameters to the transformation formula. */
  private Velocity veloAdditionReversed() {
    return VelocityTransformation.unprimedVelocity(
      veloTwo, 
      veloOne 
    );
  }
  
  private void check(Velocity... velocities) {
    for(Velocity v : velocities) {
       if (v.magnitude() == 1.0) {
         throw new IllegalArgumentException("Speed cannot be 1.");
       }
    }
  }
  
  private boolean noZeroes() {
    return 
      veloOne.magnitude() > 0 && 
      veloTwo.magnitude() > 0
   ;
  }
  
  private static double onlyTinyDiff = Epsilon.ε();

  private boolean sameLine() {
    double angleBetween = veloOne.angle(veloTwo); //0..π
    return 
      angleBetween < onlyTinyDiff ||           //parallel 
      (Math.PI - angleBetween) < onlyTinyDiff  //anti-parallel
    ;
  }
}