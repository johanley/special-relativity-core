package sr.explore.dogleg;

/**
 Two successive boosts, the second at a some angle to the first, are 
 equivalent to a single boost in an intermediate direction, followed by a rotation.
 
 The first boost defines the X-axis.
 The second boost defines the X-Y plane. 
 The second boost is split into 2 parts, parallel and perpendicular to the X-axis (the first boost). 
 
 Example geometry: Z is the pole, doesn't change. 
 First a boost in X from K to K', then a boost in Y from K' to K''.
 This example has a 90 degree angle between the boost directions (in K').

 <P>With matrix algebra,
 <pre>
  By(β2) * Bx(β1) = R(θw) * B
  or the inverse:
  B = R-inv(θw) * By(β2) * Bx(β1)
 </pre>
 
 <P>There is an inverse reading of the same matrix math.
 Any boost in the x-y plane can be decomposed into 2 mutually perpendicular 
 boosts (along X and Y), followed by a rotation. This fact helps to derive the arbitrary-angle case.
*/
final class DoglegBoostEquivalent {
  
  DoglegBoostEquivalent(double β, double βdirection, double θw){
    this.β = β;
    this.βdirection = βdirection;
    this.θw = θw;
  }
  
  /** In K, the speed of the equivalent single boost. */
  double β;
  
  /**
   In K, the direction of the equivalent single boost, with respect to the direction of the first boost.
   Range -pi..+pi. 
  */
  double βdirection;
  
  /** 
   Thomas-Wigner rotation angle with respect to the X-axis.
   It's never more than 90 degrees.
   
   <P>This rotation is, in K, the angle of the <em>X''-axis of K''</em> with respect to the X-axis of K. 
   Range -pi/2..0 rads.
  */
  double θw;

}
