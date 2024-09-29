package sr.core.vec3;

import sr.core.Axis;
import sr.core.Util;
import sr.core.component.NComponents;
import sr.core.component.ops.NReverseSpatialComponents;
import sr.core.component.ops.NRotate;
import sr.core.component.ops.NSense;
import sr.core.ops.NLinearOps;

/** 
 The spatial gradient of the phase of a wave (<b>k</b>, its wave vector).
 The magnitude of the gradient is always positive. 
*/
public final class NPhaseGradient extends NThreeVector implements NLinearOps<NPhaseGradient> {
  
  /** Factory method, taking the 3 components of the phase-gradient (wave vector) <em>k</em> along the XYZ axes, in that order. */
  public static NPhaseGradient of(double x, double y, double z) {
    return new NPhaseGradient(x, y, z);
  }
  
  /**
   Factory method. 
   @param k is positive. 
  */
  public static NPhaseGradient of(double k, NDirection direction) {
    return new NPhaseGradient(k, direction);
  }

  /** 
   Factory method. 
   The vector has 1 non-zero component, along the given spatial coordinate axis.
   @param k is positive. 
  */
  public static NPhaseGradient of(double k, Axis axis) {
    return new NPhaseGradient(k, axis);
  }
  
  /** No effect. */
  @Override public NPhaseGradient reverseClocks() {
    return new NPhaseGradient(components);
  }
  
  /** Reverse all components. */
  @Override public NPhaseGradient reverseSpatialAxes() {
    NComponents comps = new NReverseSpatialComponents().applyTo(components);
    return new NPhaseGradient(comps);
  }
  
  @Override public NPhaseGradient rotate(NAxisAngle axisAngle, NSense sense) {
    NComponents comps = NRotate.of(axisAngle, sense).applyTo(components);
    return new NPhaseGradient(comps);
  }
  
  private NPhaseGradient(double x, double y, double z) {
    super(x, y, z);
  }

  private NPhaseGradient(double k, NDirection direction) {
    super(k, direction);
    check(k);
  }

  private NPhaseGradient(double k, Axis axis) {
    super(k, axis);
    check(k);
  }
  
  private NPhaseGradient(NComponents comps) {
    super(comps);
  }
  
  private void check(double k) {
    Util.mustHave(k > 0, "k must be positive: " + k);
  }
}
