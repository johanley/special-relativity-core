package sr.core;

import static sr.core.Util.*;
import static java.lang.Math.*;

/** 
 Commonly needed core items.
 Not all physics is present in this class. 
 In particular, the Lorentz transformations are in {@link Transformer}. 
*/
public final class Physics {

  /** 
   The squared-interval between two events. The metric. 
   The unit of the returned value is distance-squared.
   Uses (+,-,-,-,) as the signature (the same as The Classical Theory of Fields, by Landau and Lifschitz).
   Can be positive or negative! 
   All time-like intervals are real, not imaginary.  
  */
  public static double sqInterval(Event a, Event b) {
    return sq(a.ct()-b.ct()) - sq(a.x()-b.x()) - sq(a.y()-b.y()) - sq(a.z()-b.z());
  }
  
  /** 
   Speed as a fraction of the speed limit. Dimensionless.
   The numeric value of the speed limit is defined by {@link Config}.
  */
  public static final Double β(Double speed) {
    return speed / Config.c();
  }
  
  /** 
   The warp factor (Lorentz factor). Dimensionless.
   (Upper case gamma is used simply because lower case gamma doesn't render 
   well in some common fonts.)
  */
  public static final Double Γ(Double β) {
    return 1.0/sqroot(1 - sq(β));
  }
  
  /** 
   The Doppler factor. Dimensionless.
   
   @param θ is the angle between the line-of-sight and the line-of-motion, in radians [0..pi].
   @param speed is the speed, not β   
  */
  public static final Double D(Double β, Double θ) {
    double Γ = Γ(β);
    return 1.0/Γ*(1 - β*cos(θ));
  }
}
