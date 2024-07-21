package sr.core.vector;

import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;

import java.util.Optional;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.Rotation;
import sr.core.Util;
import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.FourVector;

/** 
The 3-velocity of an object. (This is not a 4-vector.)

IMPORTANT TO NOTE: the most extreme relativistic speeds seen in nature (in high-energy cosmic rays, for example) 
can't be represented with the {@link java.lang.Double} data type in the Java programming language, 
because it has an insufficient number of decimal places. 
Thus, this class cannot be used to represent such speeds.
(An alternative implementation would might use {@link java.math.BigDecimal} instead of Double to represent speeds.)
*/
public final class Velocity implements ThreeVector {
  
  /** Factory method, taking the 3 components of the velocity along the XYZ axes, in that order.  */
  public static Velocity of(double βx, double βy, double βz) {
    return new Velocity(βx, βy, βz);
  }
  
  /** Factory method for the case in which the velocity is parallel to a spatial coordinate axis. */
  public static Velocity of(Axis axis, double βi) {
    return new Velocity(axis, βi);
  }
  
  public static Velocity zero() {
    return Velocity.of(0.0, 0.0, 0.0);
  }
  
  /** Some cases only make sense when the speed is non-zero. */
  public static Velocity nonZero(double βx, double βy, double βz) {
    Velocity result = Velocity.of(βx, βy, βz);
    Util.mustHave(result.magnitude() > 0, "Vector should have a non-zero magnitude.");
    return result;
  }
  
  /** 
   The 4-velocity corresponding to this object.
   Always time-like. Never zero (because the time component is always greater than or equal to 1.) 
  */
  public FourVector fourVelocity() {
    double Γ = Γ();
    return FourVector.from(Γ, Γ*on(X), Γ*on(Y), Γ*on(Z), ApplyDisplaceOp.NO);
  }

  /**  A four-momentum having this velocity.  @param mass must be greater than 0. */
  public FourVector fourMomentumFor(double mass) {
    Util.mustHave(mass > 0, "Mass " + mass + " must be greater than 0.");
    return fourVelocity().multiply(mass); //c=1 in this project
  }
  
  /** The Lorentz factor, always greater than or equal to 1. */
  public double Γ() {
    return Physics.Γ(magnitude());
  }

  @Override public double on(Axis axis) {
    return vec.on(axis);
  }

  @Override public Optional<Axis> axis(){
    return vec.axis();
  }
  
  @Override public double dot(ThreeVector that) {
    return vec.dot(that);
  }
  
  @Override public ThreeVector cross(ThreeVector that) {
    return vec.cross(that);
  }
  
  @Override public double angle(ThreeVector that) {
    return vec.angle(that);
  }

  @Override public double square() {
    return vec.square();
  }

  @Override public double magnitude() {
    return vec.magnitude();  
  }

  @Override public ThreeVector plus(ThreeVector that) {
    return vec.plus(that);
  }
  
  @Override public ThreeVector minus(ThreeVector that) {
    return vec.minus(that);
  }

  @Override public ThreeVector multiply(double scalar) {
    return vec.multiply(scalar);
  }
  
  @Override public ThreeVector divide(double scalar) {
    return vec.divide(scalar);
  }
  
  @Override public  ThreeVector rotation(Rotation rotation) {
    return vec.rotation(rotation);
  }
  
  @Override public ThreeVector reflection() {
    return vec.reflection();
  }
  
  @Override public ThreeVector reflection(Axis axis) {
    return vec.reflection(axis);
  }
  
  //PRIVATE 
  
  private ThreeVectorImpl vec;
  
  private Velocity(double xComp, double yComp, double zComp) {
    vec = ThreeVectorImpl.of(xComp, yComp, zComp);
    check();
  }
  
  private Velocity(Axis axis, double value) {
    vec = ThreeVectorImpl.of(axis, value);
    check();
  }
  
  /** Validations on incoming constructor data. */
  private void check() {
    checkRange(vec.on(X), vec.on(Y), vec.on(Z));
    checkRange(magnitude());
  }
  
  private void checkRange(Double... βs) {
    for (Double βi : βs) {
      Util.mustHaveSpeedRange(βi);
    }
  }
}