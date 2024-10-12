package sr.core;

import sr.core.vec3.Velocity;

/** 
 Kinematic (Wigner) rotation corresponding to the addition of two velocities.
 The two velocities are usually not in the same line.
*/
public final class KinematicRotation {

  public static KinematicRotation of(Velocity one, Velocity two) {
    return new KinematicRotation(one, two);
  }
  
  /** Kinematic (Wigner) rotation angle. Range -π..+π. */
  public double θw() {
    //Silberstein's textbook points out that this is one way of calculated the kinematic rotation:
    Velocity a = veloAddition();
    Velocity b = veloAdditionReversed();
    //should this be a.turnsTo(b) ? No, I believe this is correct. 
    //For circular motion, this angle is 'retrograde' with respect to the sense of the given circular motion.
    return b.turnsTo(a);
  }

  private Velocity veloOne;
  private Velocity veloTwo;

  private KinematicRotation(Velocity one, Velocity two) {
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
}