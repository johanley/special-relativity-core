package sr.core;

import static java.lang.Math.cos;
import static sr.core.Util.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import sr.core.transform.FourVector;

/** 
 Commonly needed core items.
 Not all physics is present in this class. 
 
 <P>WARNING: the are some extreme cases in which the value of β is so close to 1 
 that it needs special handling. A BigDecimal needs to be used instead of a Double.
 BigDecimal objects can represent floating point numbers to any number of decimals.
*/
public final class Physics {
  
  public static final double JOULES_PER_EV = 1.602177e-19;
  
  public static final double PROTON_MASS_KG = 1.672622e-27;
  
  /** The nominal limiting visual magnitude of a star (6.0), as seen by an average human eye. */
  public static final Double LIMITING_MAG_HUMAN_EYE = 6.0;
  
  /** Used for extreme values of β that are extremely close to 1, and need to be represented by BigDecimal, not Double. */
  public static int NUM_DECIMALS = 40;
  
  /** Used for extreme values of β that are extremely close to 1, and need to be represented by BigDecimal, not Double. */
  public static final MathContext LARGE_NUM_DECIMALS = new MathContext(NUM_DECIMALS, RoundingMode.HALF_EVEN);
  
  /** 
   Nature's speed limit for all signals (meters per second), in case you need it for converting your 
   data to units in which c=1. This library always assumes c=1! 
   Value: {@value} 
  */
  public static final double C = 299792458.0D;
  
  /**
    Note that this method uses very specific units!
    @param joules the energy of the object in Joules
    @param kg the mass of the object in kilograms 
  */
  public static final double ΓfromEnergy(double joules, double kg) {
    double numer = joules;
    double denom = kg * sq(C);
    return numer / denom;
  }
  
  /**
   β for an object of the given energy and mass.
   
   The return value is a BigDecimal, not a Double. 
   This is because for extreme energies, β is so close to 1 that double lacks sufficient precision.
   The caller will need to be careful when converting the return value into a double.  
  */
  public static final BigDecimal βfromEnergy(double joules, double kg) {
    BigDecimal result = null;
    double gamma = ΓfromEnergy(joules, kg);
    BigDecimal gammaSquared = BigDecimal.valueOf(gamma*gamma); 
    BigDecimal betaSquared = BigDecimal.ONE.subtract(BigDecimal.ONE.divide(gammaSquared, NUM_DECIMALS, RoundingMode.HALF_EVEN));
    result = betaSquared.sqrt(LARGE_NUM_DECIMALS);
    return result;
  }
  
  /** 
   The warp factor (Lorentz factor). Dimensionless.
   WARNING: don't use when β is extremely close to 1!

   <P>(Upper case gamma is used simply because lower case gamma doesn't render 
   well in some common fonts.)
  */
  public static final Double Γ(Double β) {
    return 1.0/sqroot(1 - sq(β));
  }

  /** 
   The speed from the Lorentz factor. Dimensionless.
  */
  public static final Double β(Double Γ) {
    //not a great result when Γ is low, because it changes so slowly!
    double betaSq = 1 - 1.0/sq(Γ);
    return sqroot(betaSq);
  }
  
  /** 
   The warp factor (Lorentz factor). Dimensionless.
   WARNING: This method is necessary when β is extremely close to 1, and Double has insufficient precision to 
   represent it. 
  */
  public static final BigDecimal Γ(BigDecimal β) {
    BigDecimal numer = new BigDecimal("1.0");
    BigDecimal a = BigDecimal.ONE.subtract(β.pow(2, LARGE_NUM_DECIMALS));
    BigDecimal denom = a.sqrt(LARGE_NUM_DECIMALS);
    BigDecimal result = numer.divide(denom, LARGE_NUM_DECIMALS);
    return result;
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

  /** Build a 4-velocity vector, where the 3-velocity has a component only on the given axis. */
  public static FourVector fourVelocity(double β, Axis spatialAxis) {
    mustBeSpatial(spatialAxis);
    FourVector result = FourVector.ZERO_LINEAR;
    double Γ = Γ(β);
    result = result.put(Axis.CT, Γ);
    result = result.put(spatialAxis, Γ * β);
    return result;
  }
  
  /** 
   Converts to units of light-yr per year^2.
   @param mks_units the acceleration in meters per sec^2. 
  */
  public static final Double acceleration(double mks_units) {
    return mks_units * (SECONDS_PER_YEAR * SECONDS_PER_YEAR) / METERS_PER_LIGHT_YEAR;
  }
  
  /** 
   Converts to units of light-yr per year^2.
   @param gees multiple of Earth's standard gravitational acceleration. 
  */
  public static final Double gAcceleration(double gees) {
    return acceleration(gees * ONE_GEE_MKS);
  }
  
  /**
   Transform of velocity, in the case where all motions are parallel to an axis (colinear).
   
   <P>An object moves along an axis in the K frame, with velocity v.
   K' is a second frame, boosted along the same axis (with respect to the K frame). 
    
    @param v the velocity of the object along the axis in the K frame
    @param boost velocity of K' with respect to K, along the axis
    @return the velocity along the axis in the boosted frame K'
  */
  public static final Double transformVelocityColinear(double v, double boost) {
    double c = 1.0; //to compare with books
    double result = (v + boost) / (1 + v*boost/(c*c));
    return result;
  }

  /**
   New angle for a boosted stick.
   
   A stick is boosted. 
   In K, the stick is at rest, at an angle θ (not 0 or 90 degrees) with respect to the boost.
   In K', the stick gets flattened in the direction of the boost, in the usual way (length contraction).
   In K', the angle θ changes from the given value to the returned value.
   
   <P>Problem Book in Relativity and Gravitation, Lightman and others, problem 1.7.
   @return in K'', the angle of the stick with respect to the direction of the boost, range -pi/2..+pi/2  
   @param θ in K, the angle of the stick with respect to the direction of the boost
   @param β boost speed
  */
  public static final double stickAngleAfterBoost(double θ, double β) {
    double tanθ = Math.tan(θ);
    double result = Math.atan(tanθ * Γ(β)); //-pi/2..+pi/2
    return result;
  }
  
  public static void main(String... args) {
    /*
     Rotation of the stick from time-slice of histories in K: -38.6598 degrees
     Thomas-Wigner angle calculated directly from a formula: -18.92464 degrees
    */
    double βdirection = Util.degsToRads(24.227745317954163);
    double θw = Util.degsToRads(18.924644416051237);
    double β = 0.8772684879784525;
    
    double a1 = stickAngleAfterBoost(βdirection, β);
    Util.log("βdirection start: " + Util.radsToDegs(βdirection));
    Util.log("βdirection flattened: " + Util.radsToDegs(a1));
    Util.log("βdirection chg  : " + Util.radsToDegs(a1 - βdirection));
    Util.log("θw calc:          " + Util.radsToDegs(θw));
    a1 = stickAngleAfterBoost(βdirection+θw, β);
    Util.log("βdirection+θw flattened: " + Util.radsToDegs(a1));
    
  }
  
  // PRIVATE 
  private static final double SECONDS_PER_YEAR = 86400.0*365.242189;
  private static final double METERS_PER_LIGHT_YEAR = 9.460730e15;
  private static final double ONE_GEE_MKS = 9.80665; //m per s^2
}
