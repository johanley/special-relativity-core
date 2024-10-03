package sr.core.vec3;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.Util;
import sr.core.component.Components;
import sr.core.component.ops.ReverseSpatialComponents;
import sr.core.component.ops.Rotate;
import sr.core.component.ops.Sense;
import sr.core.ops.LinearOps;

/** 
 The 3-velocity of an object. 

 IMPORTANT TO NOTE: the most extreme relativistic speeds seen in nature (in high-energy cosmic rays, for example) 
 can't be represented with the {@link java.lang.Double} data type in the Java programming language, 
 because it has an insufficient number of decimal places. 
 Thus, this class cannot be used to represent such speeds.
 (An alternative implementation would might use {@link java.math.BigDecimal} instead of Double to represent speeds.)

 <P>Most objects created by this class have both the components of the velocity and the overall speed in the range (-1, +1).
 This is an open interval, excluding the boundaries.
 The exceptions are the <em>unity</em> methods, where the speed is always 1.
*/
public final class Velocity extends ThreeVector implements LinearOps<Velocity>{
  
  /** Factory method, taking the 3 components of the velocity along the XYZ axes, in that order.  */
  public static Velocity of(double βx, double βy, double βz) {
    return new Velocity(βx, βy, βz);
  }
  
  /** 
   Factory method for the case in which the velocity is parallel or anti-parallel to a spatial coordinate axis.
   @param speed can be either sign. 
  */
  public static Velocity of(double speed, Axis axis) {
    return new Velocity(speed, axis);
  }
  
  /** 
   Factory method.
   @param magnitude must be non-negative. 
  */
  public static Velocity of(double magnitude, Direction direction) {
    return new Velocity(magnitude, direction);
  }

  /** Factory method for the case in which the data is already in a ThreeVector (as the result of a calculation). */
  public static Velocity of(ThreeVector v) {
    return new Velocity(v.x(), v.y(), v.z());
  }
  
  public static Velocity zero() {
    return Velocity.of(0.0, 0.0, 0.0);
  }

  /** Only massless objects can have a speed of 1.0. */
  public static Velocity unity(Direction direction) {
    return new Velocity(direction);
  }
  
  /** Only massless objects can have a speed of 1.0. */
  public static Velocity unity(Axis axis, Sense sense) {
    return new Velocity(axis, sense);
  }
  
  /** Some cases only make sense when the speed is non-zero. */
  public static Velocity nonZero(double βx, double βy, double βz) {
    Velocity result = Velocity.of(βx, βy, βz);
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
  @Override public Velocity reverseClocks() {
    Components comps = new ReverseSpatialComponents().applyTo(components);
    return new Velocity(comps);
  }
  
  /** Reverse the sign of all spatial components.*/
  @Override public Velocity reverseSpatialAxes() {
    Components comps = new ReverseSpatialComponents().applyTo(components);
    return new Velocity(comps);
  }
  
  @Override public Velocity rotate(AxisAngle axisAngle, Sense sense) {
    Components comps = Rotate.of(axisAngle, sense).applyTo(components);
    return new Velocity(comps);
  }
  
  private Velocity(double xComp, double yComp, double zComp) {
    super(xComp, yComp, zComp);
    check();
  }
  
  private Velocity(double speed, Axis axis) {
    super(speed, axis);
    check();
  }
  private Velocity(Axis axis, Sense sense) {
    super(sense.sign(), axis);
    //no checks! 
  }
  
  private Velocity(double magnitude, Direction direction) {
    super(magnitude, direction);
    checkNonNegative(magnitude);
    check();
  }
  private Velocity(Direction direction) {
    super(1.0, direction);
    //no checks! 
  }
  
  private Velocity(Components comps) {
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
