package sr.core;

import sr.core.vector.ThreeVector;
import sr.core.vector.Velocity;

/** 
 Relativistic transformation of velocities.
 
<P>Reference: <a href='https://en.wikipedia.org/wiki/Velocity-addition_formula'>Wikipedia</a>.

<P>Apart from the style of the above reference, one can picture the items involved in the following way.
(This follows the style of Fock's book <em>The Theory of Space, Time and Gravitation</em>.)
In a frame K, there are two objects.
Each has a velocity with respect to K.
One can select one of the objects as representing a second frame K'.
Then you find the velocity of the other object relative to K'.
*/ 
public final class VelocityTransformation {

  /**
   You need to be careful with the exact meaning of the parameters.
   The order matters! Non-commutative.
    
   @param boost_v velocity of K' in K (the boost velocity)
   @param object_v velocity of object in K
   @return velocity of object in K' (u')
  */
  public static Velocity primedVelocity(Velocity boost_v, Velocity object_v) {
    return transform(boost_v, object_v, -1);
  }
  
  /**
   You need to be careful with the exact meaning of the parameters.
   The order matters! Non-commutative.
    
   @param boost_v velocity of K' in K (the boost velocity)
   @param u' velocity of object in K'
   @return velocity of object in K (u)
  */
  public static Velocity unprimedVelocity(Velocity boost_v, Velocity object_v_prime) {
    return transform(boost_v, object_v_prime, +1);
  }
  
  /**
   Do the transform.
   @param v boost velocity
   @param u object velocity
   @param sign +1 for returning unprimed-u, -1 for returning primed-u.
  */
  private static Velocity transform(Velocity v, Velocity u, int sign) {
    double a = 1.0 / (1 + sign*u.dot(v));
    double Γ_v = Physics.Γ(v.magnitude());
    double b = Γ_v / (1 + Γ_v);
    
    ThreeVector c = v.cross(v.cross(u)).times(b);
    ThreeVector d = sign == 1 ? u.plus(v).plus(c) : u.minus(v).plus(c); 
    ThreeVector e = d.times(a);
    
    return Velocity.of(e.x(), e.y(), e.z());
  }
}
