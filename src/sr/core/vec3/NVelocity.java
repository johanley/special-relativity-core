package sr.core.vec3;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.Util;
import sr.core.component.NComponents;
import sr.core.component.ops.NReverseSpatialComponents;
import sr.core.component.ops.NRotate;
import sr.core.component.ops.NSense;
import sr.core.ops.NLinearOps;

/** 
 The 3-velocity of an object. 

 IMPORTANT TO NOTE: the most extreme relativistic speeds seen in nature (in high-energy cosmic rays, for example) 
 can't be represented with the {@link java.lang.Double} data type in the Java programming language, 
 because it has an insufficient number of decimal places. 
 Thus, this class cannot be used to represent such speeds.
 (An alternative implementation would might use {@link java.math.BigDecimal} instead of Double to represent speeds.)

 <P>Both the components of the velocity and the overall speed must be in the range (-1, +1).
 This is an open interval, excluding the boundaries.
*/
public final class NVelocity extends NThreeVector implements NLinearOps<NVelocity>{
  
  /** Factory method, taking the 3 components of the velocity along the XYZ axes, in that order.  */
  public static NVelocity of(double βx, double βy, double βz) {
    return new NVelocity(βx, βy, βz);
  }
  
  /** Factory method for the case in which the velocity is parallel to a spatial coordinate axis. */
  public static NVelocity of(double magnitude, Axis axis) {
    return new NVelocity(magnitude, axis);
  }
  
  /** Factory method. */
  public static NVelocity of(double magnitude, NDirection direction) {
    return new NVelocity(magnitude, direction);
  }

  /** Factory method for the case in which the data is already in a ThreeVector (as the result of a calculation). */
  public static NVelocity of(NThreeVector v) {
    return new NVelocity(v.x(), v.y(), v.z());
  }
  
  public static NVelocity zero() {
    return NVelocity.of(0.0, 0.0, 0.0);
  }

  /** Only massless objects can have a speed of 1.0. */
  public static NVelocity unity(NDirection direction) {
    return new NVelocity(direction);
  }
  
  /** Only massless objects can have a speed of 1.0. */
  public static NVelocity unity(Axis axis) {
    return new NVelocity(axis);
  }
  
  /** Some cases only make sense when the speed is non-zero. */
  public static NVelocity nonZero(double βx, double βy, double βz) {
    NVelocity result = NVelocity.of(βx, βy, βz);
    Util.mustHave(result.magnitude() > 0, "Velocity should have a non-zero magnitude.");
    return result;
  }
  
  /** The Lorentz factor (warp factor) related to this velocity. */
  public double Γ() {
    return Physics.Γ(magnitude());
  }
  
  /*
  NOT ADDING THIS METHOD. Sometimes β carries a sign! Depends on the context. 
  public double β() {
    return magnitude();
  }
  */
  
  /** Reverse the sign of all spatial components.*/
  @Override public NVelocity reverseClocks() {
    NComponents comps = new NReverseSpatialComponents().applyTo(components);
    return new NVelocity(comps);
  }
  
  /** Reverse the sign of all spatial components.*/
  @Override public NVelocity reverseSpatialAxes() {
    NComponents comps = new NReverseSpatialComponents().applyTo(components);
    return new NVelocity(comps);
  }
  
  @Override public NVelocity rotate(NAxisAngle axisAngle, NSense sense) {
    NComponents comps = NRotate.of(axisAngle, sense).applyTo(components);
    return new NVelocity(comps);
  }
  
  private NVelocity(double xComp, double yComp, double zComp) {
    super(xComp, yComp, zComp);
    check();
  }
  
  private NVelocity(double magnitude, Axis axis) {
    super(magnitude, axis);
    checkNonNegative(magnitude);
    check();
  }
  private NVelocity(Axis axis) {
    super(1.0, axis);
    //no checks! 
  }
  
  private NVelocity(double magnitude, NDirection direction) {
    super(magnitude, direction);
    checkNonNegative(magnitude);
    check();
  }
  private NVelocity(NDirection direction) {
    super(1.0, direction);
    //no checks! 
  }
  
  private NVelocity(NComponents comps) {
    super(comps);
    check();
  }
  
  /** Validations on incoming constructor data. */
  private void check() {
    checkRange(x(), y(), z());
    checkRange(magnitude());
  }
  
  private void checkNonNegative(double value) {
    Util.mustHave(value >= 0, "Magnitude must be non-negative: " + value);
  }
  
  private void checkRange(Double... βs) {
    for (Double βi : βs) {
      Util.mustHaveSpeedRange(βi);
    }
  }
}
