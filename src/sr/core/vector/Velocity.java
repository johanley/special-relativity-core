package sr.core.vector;

import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;

import sr.core.Axis;
import sr.core.Util;

/** 
The 3-velocity of an object. (This is not a 4-vector.)

IMPORTANT TO NOTE: the most extreme relativistic speeds seen in nature (in high-energy cosmic rays, for example) 
can't be represented with the {@link java.lang.Double} data type in the Java programming language, 
because it has an insufficient number of decimal places. 
Thus, this class cannot be used to represent such speeds.
(An alternative implementation would might use {@link java.math.BigDecimal} instead of Double to represent speeds.)

<P>Both the components of the velocity and the overall speed must be in the range (-1, +1).
This is an open interval, excluding the boundaries.
*/
public final class Velocity extends ThreeVectorImpl {
  
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
  
  //PRIVATE 
  
  private Velocity(double xComp, double yComp, double zComp) {
    super(xComp, yComp, zComp);
    check();
  }
  
  private Velocity(Axis axis, double value) {
    super(axis, value);
    check();
  }
  
  /** Validations on incoming constructor data. */
  private void check() {
    checkRange(on(X), on(Y), on(Z));
    checkRange(magnitude());
  }
  
  private void checkRange(Double... βs) {
    for (Double βi : βs) {
      Util.mustHaveSpeedRange(βi);
    }
  }
}