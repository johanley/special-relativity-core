package sr.core;

import sr.core.component.ops.Sense;
import sr.core.vec3.NDirection;
import sr.core.vec3.NPhaseGradient;
import sr.core.vec3.NVelocity;
import sr.core.vec4.NFourPhaseGradient;
import sr.core.vec4.NFourVelocity;

/** 
 Relativistic transformation of velocities.
 This implementation converts to four-velocities, and then uses a Lorentz Transformation.
*/ 
public final class VelocityTransformation {

  /**
   You need to be careful with the exact meaning of the parameters.
   The order matters! Non-commutative.
    
   @param boost_v velocity of K' in K (the boost velocity). Speed less than 1.0.
   @param object_v velocity of object in K
   @return velocity of object in K' (u')
  */
  public static NVelocity primedVelocity(NVelocity boost_v, NVelocity object_v) {
    return transform(boost_v, object_v, Sense.Primed);
  }
  
  /**
   You need to be careful with the exact meaning of the parameters.
   The order matters! Non-commutative.
    
   @param boost_v velocity of K' in K (the boost velocity). Speed less than 1.0.
   @param object_v_prime velocity of object in K' (v')
   @return velocity of object in K (u)
  */
  public static NVelocity unprimedVelocity(NVelocity boost_v, NVelocity object_v_prime) {
    return transform(boost_v, object_v_prime, Sense.Unprimed);
  }
  
  /**
   Do the transform.
   Because of the behaviour of Γ, this isn't very precise for the lowest speeds. 
   But that's okay in the context of this project.
    
   @param boost_v boost velocity. Speed less than 1.0.
   @param u object velocity
   @param sense +1 for returning primed-u, -1 for returning unprimed-u.
  */
  private static NVelocity transform(NVelocity boost_v, NVelocity u, Sense sense) {
    check(boost_v);
    NVelocity result = null;
    if (u.magnitude() < 1.0) {
      NFourVelocity u4 = NFourVelocity.of(u);
      result = u4.boost(boost_v, sense).velocity();
    }
    else {
      //massless object travelling at c !
      //the task is to find its direction of travel, because its speed must be c
      //aberration of a phase-gradient k can be used to find that direction
      NFourPhaseGradient k_K = NFourPhaseGradient.of(NPhaseGradient.of(1.0, NDirection.of(u.x(), u.y(), u.z())));
      NPhaseGradient k_Kp_3 = k_K.boost(boost_v, sense).k();
      result = NVelocity.unity(NDirection.of(k_Kp_3.x(), k_Kp_3.y(), k_Kp_3.z()));
    }
    return result;
  }
  
  private static void check(NVelocity boost_v) {
     if (boost_v.magnitude() >= 1) {
       throw new IllegalArgumentException("Boost velocity must have magnitude less than 1: " + boost_v.magnitude());
     }
  }
}
