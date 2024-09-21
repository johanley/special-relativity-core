package sr.core.vector3;

import sr.core.Axis;
import sr.core.Util;

/** 
 The spatial gradient of the phase of a wave (<em>k</em>, its wave vector).
 The magnitude of the gradient must be non-zero. 
*/
public final class PhaseGradient extends ThreeVectorImpl {

  /** Factory method, taking the 3 components of the phase-gradient along the XYZ axes, in that order. */
  public static PhaseGradient of(double x, double y, double z) {
    return new PhaseGradient(x, y, z);
  }
  
  /**
   Factory method. 
   @param k is non-zero; if negative, then the direction is reversed. 
  */
  public static PhaseGradient of(double k, Direction direction) {
    return new PhaseGradient(k, direction);
  }

  /** 
   Factory method. 
   The vector has 1 non-zero component, along the given spatial coordinate axis.
    @param k is non-zero; if negative, then the direction is reversed. 
  */
  public static PhaseGradient of(Axis axis, double k) {
    return new PhaseGradient(axis, k);
  }
  
  private PhaseGradient(double x, double y, double z) {
    super(x, y, z);
  }

  private PhaseGradient(double k, Direction direction) {
    super(k, direction);
    check(k);
  }

  private PhaseGradient(Axis axis, double k) {
    super(axis, k);
    check(k);
  }
  
  private void check(double k) {
    Util.mustHave(k != 0, "k cannot be zero: " + k);
  }

}