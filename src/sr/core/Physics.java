package sr.core;

import static sr.core.Util.sq;
import static sr.core.Util.sqroot;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

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

  /** The numeric value of 1g, expressed using light-years as the distance unit and year as the time-unit. {@value}. */
  public static final double ONE_GEE = 1.03; //light-years, year as the unit!

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
   New angle for a boosted stick.
   
   A stick is boosted. 
   In K, the stick is at rest, at an angle θ (not 0 or 90 degrees) with respect to the boost.
   In K', the stick gets flattened in the direction of the boost, in the usual way (length contraction).
   In K', the angle θ changes from the given value to the returned value.
   
   <P>Problem Book in Relativity and Gravitation, Lightman and others, problem 1.7.
   @return in K', the angle of the stick with respect to the direction of the boost, range -pi/2..+pi/2  
   @param θ in K, the angle of the stick with respect to the direction of the boost
   @param β boost speed
  */
  public static final double stickAngleAfterBoost(double θ, double β) {
    double tanθ = Math.tan(θ);
    double result = Math.atan(tanθ * Γ(β)); //-pi/2..+pi/2
    return result;
  }
  
  // PRIVATE 
  private static final double SECONDS_PER_YEAR = 86400.0*365.242189;
  private static final double METERS_PER_LIGHT_YEAR = 9.460730e15;
  private static final double ONE_GEE_MKS = 9.80665; //m per s^2
}
