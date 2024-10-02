package sr.core.component;

import static sr.core.Axis.CT;
import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;
import static sr.core.Util.mustHave;
import static sr.core.Util.round;

import sr.core.Axis;
import sr.core.component.ops.NBoost;
import sr.core.component.ops.NMoveZeroPointBy;
import sr.core.component.ops.NReverseSpatialComponents;
import sr.core.component.ops.NReverseTimeComponent;
import sr.core.component.ops.NRotate;
import sr.core.component.ops.NSense;
import sr.core.ops.NAffineOp;
import sr.core.ops.NLinearBoostOp;
import sr.core.ops.NLinearOps;
import sr.core.vec3.NAxisAngle;
import sr.core.vec3.NVelocity;
import sr.core.vec4.NFourDelta;

/** 
 An event in Minkowski space-time.
 <P>Note that an event is not a 4-vector.
*/
public final class NEvent implements NAffineOp<NEvent>, NLinearOps<NEvent>, NLinearBoostOp<NEvent> {
  
  /** Factory method. */
  public static final NEvent of(NComponents components) {
    mustHave(components.hasSpaceAndTime(), "Expecting 4 components for event: " + components.size());
    return new NEvent(components);
  }
  
  /** Factory method. */
  public static final NEvent of(double ct, double x, double y, double z) {
    return new NEvent(ct, x, y, z);
  }

  /** Factory method. */
  public static final NEvent of(Double ct, NPosition position) {
    return new NEvent(ct, position.on(X), position.on(Y), position.on(Z));
  }
  
  /** The event at the origin of the coordinate system. */
  public static final NEvent origin() {
    return NEvent.of(0.0, NPosition.origin());
  }
  
  public double on(Axis axis) { return components.on(axis); }
  public double ct() { return on(CT); }
  public double x() { return on(X); }
  public double y() { return on(Y); }
  public double z() { return on(Z); }

  /** Return the position of the event. */
  public NPosition position(){
    return NPosition.of(on(X), on(Y), on(Z));
  }
  
  @Override public NEvent reverseClocks() {
    NComponents comps = new NReverseTimeComponent().applyTo(components);
    return NEvent.of(comps);
  }
  
  @Override public NEvent reverseSpatialAxes() {
    NComponents comps = new NReverseSpatialComponents().applyTo(components);
    return NEvent.of(comps);
  }
  
  @Override public NEvent rotate(NAxisAngle axisAngle, NSense sense) {
    NComponents comps3 = NRotate.of(axisAngle, sense).applyTo(components);
    return NEvent.of(this.components.ct(), comps3.x(), comps3.y(), comps3.z());
  }
  
  @Override public NEvent boost(NVelocity v, NSense sense) {
    NComponents comps = NBoost.of(v, sense).applyTo(components);
    return NEvent.of(comps);
  }
  
  @Override public NEvent moveZeroPointBy(NFourDelta displacement, NSense sense) {
    NComponents comps = NMoveZeroPointBy.of(displacement, sense).applyTo(components);
    return NEvent.of(comps);
  }
  
  /** This implementation applies rounding. */
  @Override public String toString() {
    String sep = ",";
    String result = "[";
    for(Axis axis : Axis.values()) {
      result = result + roundIt(on(axis)) + sep + " ";
    }
    //chop off the final separator+space characters 
    result = result.substring(0, result.length() - 2);
    return result + "]";
  }
  
  // PRIVATE

  private NComponents components;

  private NEvent(NComponents components) {
    this.components = components;
  }

  private NEvent(Double ct, Double x, Double y, Double z) {
    components = NComponents.of(ct, x, y, z);
  }
  
  private double roundIt(Double val) {
    return round(val, 5);
  }
}
