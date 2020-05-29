package sr.core;

import static java.lang.Math.cos;
import static sr.core.Util.sq;
import static sr.core.Util.sqroot;

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
   
   @param θ is the angle between the detected line-of-sight and the line-of-motion, in radians [0..pi].
  */
  public static final Double D(Double β, Double θ) {
    double Γ = Γ(β);
    double denom = Γ*(1 - β*cos(θ)); 
    return 1.0/denom;
  }
  
  /**
   The Doppler-shift applied to the effective temperature of a black-body spectrum.
   <P>Reference: 
   <pre>
   In search of the "starbow": The appearance of the starfield from a relativistic spaceship
   John M. McKinley, Paul Doherty 
   Am. J. Phys. vol 47 No. 4 April 1979 p309
   </pre>
  
  @param T0 in kelvins, the absolute temperature of the object in its rest frame
  */
  public static final Double T(Double D, Double T0) { 
    return  D*T0;
  }
  
  /**
  The change in apparent visual magnitude of a star because of Doppler effects, modeled as having a black-body spectrum.
  (Note that real stellar spectra only roughly approximate to that of a black-body.
  <P>Reference: 
  <pre>
  In search of the "starbow": The appearance of the starfield from a relativistic spaceship
  John M. McKinley, Paul Doherty 
  Am. J. Phys. vol 47 No. 4 April 1979 p309
  </pre>
  
  @param T0 in kelvins, the absolute temperature of the object in its rest frame
 */
 public static final Double Δmag(Double D, Double T0) { 
   return 2.5*Math.log10(D) - 26000*(1.0/T0 - 1.0/(D*T0));
 }

 /**
  Aberration formula for the detector-direction.
  Returns the angle (radians [0,pi]) between the detector-direction and the boost-direction.
  The detector-direction gets deflected towards the boost-direction.
  <P>References: 
   <ul>
    <li>The Classical Theory of Fields, Landau and Lifshitz
    <li><a href='http://specialrelativity.net/part2.html#Aberration'>specialrelativity.net</a>  
   </ul>
  
   @param θ the 'geometrical' angle radians [0,pi] between the detector-direction and the boost-direction 
   @param β the boost   
  */
  public static final Double aberrationForDetectorDirection(Double θ, Double β) {
   double num = Math.cos(θ) + β;
   double denom = 1 + β * Math.cos(θ);
   double thetaPrime = Math.acos(num / denom); //0..pi
   return thetaPrime;
  }
 
  /**
  Aberration formula for the photon-direction.
  Returns the angle (radians [0,pi]) between the photon-direction and the boost-direction.
  The photon-direction gets deflected away from the boost-direction.
  <P>References: 
   <ul>
    <li>The Classical Theory of Fields, Landau and Lifshitz
    <li><a href='http://specialrelativity.net/part2.html#Aberration'>specialrelativity.net</a>  
   </ul>
  
   @param θ the 'geometrical' angle radians [0,pi] between the photon-direction and the boost-direction 
   @param β the boost   
  */
  public static final Double aberrationForPhotonDirection(Double θ, Double β) {
   double num = Math.cos(θ) - β;
   double denom = 1 - β * Math.cos(θ);
   double thetaPrime = Math.acos(num / denom); //0..pi
   return thetaPrime;
  }
  
  /*** 
   No correction for extinction is applied. 
   WARNING: this method requires light-years as the distance unit.
   
   @param M absolute magnitude 
   @param lightYears how far away the star is from the detector (light-years only!).
  */
  public static final Double apparentVisualMagnitude(Double M, double lightYears) {
    Double parsecs = lightYears / 3.261564; //RASC Observer's Handbook 2019
    Double m = M + 5*Math.log10(parsecs) - 5;
    return m;
  }
}
