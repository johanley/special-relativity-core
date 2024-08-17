package sr.core;

import static sr.core.TransformInto.PRIMED;
import static sr.core.TransformInto.UNPRIMED;

import sr.core.event.FourVelocity;
import sr.core.vector.ThreeVector;
import sr.core.vector.Velocity;

/** 
 Relativistic transformation of velocities.
 This implementation uses the {@link LorentzTransformation} directly. 
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
    return transform(boost_v, object_v, PRIMED);
  }
  
  /**
   You need to be careful with the exact meaning of the parameters.
   The order matters! Non-commutative.
    
   @param boost_v velocity of K' in K (the boost velocity)
   @param u' velocity of object in K'
   @return velocity of object in K (u)
  */
  public static Velocity unprimedVelocity(Velocity boost_v, Velocity object_v_prime) {
    return transform(boost_v, object_v_prime, UNPRIMED);
  }
  
  /**
   Do the transform.
   Because of the behaviour of Γ, this isn't very precise for the lowest speeds. 
   But that's okay in the context of this project.
    
   @param v boost velocity
   @param u object velocity
   @param sign +1 for returning primed-u, -1 for returning unprimed-u.
  */
  private static Velocity transform(Velocity v, Velocity u, TransformInto direction) {
    LorentzTransformation3 lt = LorentzTransformation3.of(v);
    FourVelocity input = FourVelocity.of(u);
    FourVelocity output = lt.transformVector(input, direction);
    return output.velocity();
  }

  /**
   Do the transform with 3-vectors. (For testing only.)
   @param v boost velocity
   @param u object velocity
   @param sign +1 for returning unprimed-u, -1 for returning primed-u.
  */
  private static Velocity transform(Velocity v, Velocity u, int sign) {
    //Reference: https://en.wikipedia.org/wiki/Velocity-addition_formula
    double a = 1.0 / (1 + sign*u.dot(v));
    double Γ_v = Physics.Γ(v.magnitude());
    double b = Γ_v / (1 + Γ_v);
    
    ThreeVector c = v.cross(v.cross(u)).times(b);
    ThreeVector d = sign == 1 ? u.plus(v).plus(c) : u.minus(v).plus(c); 
    ThreeVector e = d.times(a);
   
    return Velocity.of(e);
  }
}
