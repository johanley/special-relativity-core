package sr.core;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.FourVector;

/** 
 The velocity of an object. (This is not a 4-vector.)
 
 IMPORTANT TO NOTE: the most extreme relativistic speeds seen in nature (in high-energy cosmic rays, for example) 
 can't be represented with the {@link java.lang.Double} data type in the Java programming language, 
 because it has an insufficient number of decimal places. 
 Thus, this class cannot be used to represent such speeds.
 (An alternative implementation would might use {@link java.math.BigDecimal} instead of Double to represent speeds.)
*/
public final class Velocity {

  /** Some cases only make sense when the speed is non-zero. */
  public static Velocity speedNotZero(double βx, double βy, double βz) {
    Velocity result = new Velocity(βx, βy, βz);
    Util.mustHave(result.β > 0, "Overall speed must be greater than 0.");
    return result;
  }

  /** Factory method. */
  public static Velocity from(double βx, double βy, double βz) {
    return new Velocity(βx, βy, βz);
  }
  
  /**
   Factory method for the case in which the full velocity is parallel to an axis.
    @param axis the spatial axis parallel to the velocity
    @param β the full speed of the object (either sign).
  */
  public static Velocity from(Axis axis, double β) {
    Velocity result = zero();
    return result.put(axis, β);
  }
  
  public static Velocity zero() {
    return new Velocity(0.0, 0.0, 0.0);
  }
  
  /** The 4-velocity corresponding to this object. */
  public FourVector fourVelocity() {
    return FourVector.from(Γ, Γ*βx(), Γ*βy(), Γ*βz(), ApplyDisplaceOp.NO);
  }

  /**
   A four-momentum having this velocity.  
   @param mass must be greater than 0.
  */
  public FourVector fourMomentumFor(double mass) {
    Util.mustHave(mass > 0, "Mass " + mass + " must be greater than 0.");
    return fourVelocity().multiply(mass); //c=1 in this project
  }
  
  /** Replace one component of this vector, and build a new object in doing so. */
  public Velocity put(Axis axis, Double value) {
    Util.mustBeSpatial(axis);
    //copy this object's speeds to start with
    Map<Axis, Double> sp = new LinkedHashMap<>();
    for(Axis a: Axis.values()) {
      sp.put(a, speeds.get(a));
    }
    sp.put(axis, value); //then override one value
    return new Velocity(sp.get(Axis.X), sp.get(Axis.Y), sp.get(Axis.Z));
  }

  /** The component of the 3-velocity along the X-axis. In range [0,1). */
  public double βx() {
    return speeds.get(Axis.X);
  }
  
  /** The component of the 3-velocity along the Y-axis. In range [0,1). */
  public double βy() {
    return speeds.get(Axis.Y);
  }

  /** The component of the 3-velocity along the Z-axis. In range [0,1). */
  public double βz() {
    return speeds.get(Axis.Z);
  }

  /** The overall speed. Always positive, in the range [0,1). */
  public double β() {
    return β;
  };
  
  /** The Lorentz factor, always greater than or equal to 1. */
  public double Γ() {
    return Γ;
  }
  
  // PRIVATE
  private Map</*spatial*/Axis, Double> speeds = new LinkedHashMap<>();
  private double β;
  private double Γ; 
  //should I add the rapidity α?

  private Velocity(double βx, double βy, double βz) {
    checkSpeeds(βx, βy, βz);
    this.speeds.put(Axis.X, βx);
    this.speeds.put(Axis.Y, βy);
    this.speeds.put(Axis.Z, βz);
    
    this.β = Util.sqroot(
      Util.sq(βx) + Util.sq(βy) + Util.sq(βz)
    );
    checkSpeeds(β);
    
    this.Γ = Physics.Γ(β);
  }
  
  private void checkSpeeds(Double... βs) {
    for (Double βi : βs) {
      Util.mustHaveSpeedRange(βi);
    }
  }
}
