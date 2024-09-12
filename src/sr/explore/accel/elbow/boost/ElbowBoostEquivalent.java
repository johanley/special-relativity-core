package sr.explore.accel.elbow.boost;

/**
 Two successive boosts, the second at some angle to the first, are 
 equivalent to a single boost in a single direction, followed by a rotation.
 
 The first boost defines the X-axis.
 The second boost defines the X-Y plane. 
 The second boost is split into 2 parts, parallel and perpendicular to the X-axis (the first boost). 
 
 Example geometry: Z is the pole, doesn't change. 
 First a boost in the +X-direction from K to K', then a boost in +Y-direction from K' to K''.
 This example has a 90° angle between the boost directions (in K').

 <P>With matrix algebra,
 <pre>By(β2) * Bx(β1) = R(θw) * B </pre>
  or the inverse:
 <pre>B = R-inv(θw) * By(β2) * Bx(β1) </pre>
 
 <P>There is an inverse reading of the same matrix math.
 Any boost in the X-Y plane can be decomposed into 2 mutually perpendicular 
 boosts (along X and Y), followed by a rotation. 
 This fact helps to derive the arbitrary-angle case.
*/
public final class ElbowBoostEquivalent {
  
  public ElbowBoostEquivalent(double β, double direction, double θw){
    this.β = β;
    this.direction = direction;
    this.θw = θw;
  }
  
  /** In K, the speed of the equivalent single boost, range 0..1. */
  public double β;
  
  /**
   In K, the direction of the equivalent single boost, with respect to the direction of the first boost. Radians.
   The sense of rotation is defined by a right-hand rule, from +X-axis to the +Y-axis.   
  */
  public double direction;
  
  /** 
   Kinematic (Wigner) rotation angle (radians) with respect to the direction of the first boost.
   
   This rotation is, in K, the angle of the <em>X''-axis of K''</em> with respect to the X-axis of K.
   For circular motion, the sense of this rotation is opposite (retrograde) to the sense of the circular motion. 
  */
  public double θw;

}
