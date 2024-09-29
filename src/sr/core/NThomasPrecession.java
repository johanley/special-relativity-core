package sr.core;

import static sr.core.Util.sq;

import sr.core.vec3.NAcceleration;
import sr.core.vec3.NAxisAngle;
import sr.core.vec3.NThreeVector;
import sr.core.vec3.NVelocity;

/**
 Thomas precession for a given acceleration.
 
 <P>A precession is a rate of rotation about an axis.
 Here, the rotation is between the spatial axes of two frames:
 <ul>
  <li>frame K, in which an object moves
  <li>frame K', which is instantaneously co-moving with the object
 </ul> 
 The rotation transforms the spatial axes of K into the spatial axes of K'.
 
 <P>This can be applied generally, not just to circular motion.
 In the general case the overall rotation of the co-moving frame can be integrated along the history.
 
 <P>In the case of circular motion in the XY plane and with the angular velocity in the +Z-direction (right-hand rule),
 the axis-angle of the Thomas precession is always in the -Z-direction, and has a constant magnitude.
 This means that the rotation is opposite to the sense of the circular motion.
*/
public final class NThomasPrecession {

  /**
   Instantaneous rate of precession for a given instantaneous acceleration.
   The result is 0 if the motion has no acceleration, or if the motion is accelerated but all in the same line.
   
   @param a in K, the instantaneous acceleration of an object at time ct
   @param v in K, the instantaneous velocity of an object at time ct
   @return rate of rotation of the co-moving frame K' with respect to K (right-hand rule); the rate uses ct.
  */
  public static NAxisAngle ofKprime(NAcceleration a, NVelocity v) {
    return transform(a, v, +1);
  }
  
  /**
   The inverse of {@link #primed(Acceleration, Velocity)}.
    
   @return rate of rotation of K with respect to the co-moving frame K' (right-hand rule); the rate uses ct' (not the proper-time of the object).
  */
  public static NAxisAngle ofK(NAcceleration a, NVelocity v) {
    //is this correct? is it really a rate with respect to ct'?
    return transform(a, v, -1);
  }
  
  private static NAxisAngle transform(NAcceleration a, NVelocity v, int sign) {
    //https://en.wikipedia.org/wiki/Thomas_precession#Statement
    double Γ = v.Γ();
    double b = sq(Γ) / (Γ+1);
    NThreeVector t = a.cross(v).times(b).times(sign);
    return NAxisAngle.of(t.x(), t.y(), t.z());
  }
}
