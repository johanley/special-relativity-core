package sr.core.vec3;

import sr.core.Axis;
import sr.core.Util;
import sr.core.component.Components;
import sr.core.component.ops.ReverseSpatialComponents;
import sr.core.component.ops.Rotate;
import sr.core.component.ops.Sense;
import sr.core.ops.LinearOps;

/** 
 The spatial gradient of the phase of a wave (<b>k</b>, its wave vector).
 The magnitude of the gradient is always positive. 
*/
public final class PhaseGradient extends ThreeVector implements LinearOps<PhaseGradient> {
  
  /** Factory method, taking the 3 components of the phase-gradient (wave vector) <em>k</em> along the XYZ axes, in that order. */
  public static PhaseGradient of(double x, double y, double z) {
    return new PhaseGradient(x, y, z);
  }
  
  /**
   Factory method. 
   @param k is positive. 
  */
  public static PhaseGradient of(double k, Direction direction) {
    return new PhaseGradient(k, direction);
  }

  /** 
   Factory method. 
   The vector has 1 non-zero component, along the given spatial coordinate axis.
   @param k is positive. 
  */
  public static PhaseGradient of(double k, Axis axis) {
    return new PhaseGradient(k, axis);
  }
  
  /** No effect. */
  @Override public PhaseGradient reverseClocks() {
    return new PhaseGradient(components);
  }
  
  /** Reverse all spatial components. */
  @Override public PhaseGradient reverseSpatialAxes() {
    Components comps = new ReverseSpatialComponents().applyTo(components);
    return new PhaseGradient(comps);
  }
  
  @Override public PhaseGradient rotate(AxisAngle axisAngle, Sense sense) {
    Components comps = Rotate.of(axisAngle, sense).applyTo(components);
    return new PhaseGradient(comps);
  }
  
  private PhaseGradient(double x, double y, double z) {
    super(x, y, z);
  }

  private PhaseGradient(double k, Direction direction) {
    super(k, direction);
    check(k);
  }

  private PhaseGradient(double k, Axis axis) {
    super(k, axis);
    check(k);
  }
  
  private PhaseGradient(Components comps) {
    super(comps);
  }
  
  private void check(double k) {
    Util.mustHave(k > 0, "k must be positive: " + k);
  }
}
