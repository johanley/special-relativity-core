package sr.core.vec4;

import static sr.core.Axis.CT;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.Util;
import sr.core.component.NComponents;
import sr.core.component.ops.NBoost;
import sr.core.component.ops.NSense;
import sr.core.ops.NLinearBoostOp;
import sr.core.ops.NLinearOps;
import sr.core.vec3.NAxisAngle;
import sr.core.vec3.NDirection;
import sr.core.vec3.NThreeVector;
import sr.core.vec3.NVelocity;

/** 
 Velocity as a time-like four-vector, defined as differential displacement divided by proper-time.
 
 <P>Applies only to objects having mass.
 <P>Note that {@link NVelocity} allows for speed = 1, but this class does not.
*/
public final class NFourVelocity extends NFourVector implements NLinearOps<NFourVelocity>, NLinearBoostOp<NFourVelocity> {

  /** Factory method. */
  public static NFourVelocity of(NVelocity velocity) {
    return new NFourVelocity(velocity);
  }
  
  /** Factory method. */
  public static NFourVelocity of(double magnitude, NDirection direction) {
    return new NFourVelocity(NVelocity.of(magnitude, direction));
  }
  
  /** Factory method. */
  public static NFourVelocity of(double magnitude, Axis axis) {
    return new NFourVelocity(NVelocity.of(magnitude, axis));
  }
  
  public NVelocity velocity() { return velocity; }
  
  /** Never negative. The magnitude of the velocity. */
  public double β() { return velocity.magnitude(); }
  public NDirection direction() { return NDirection.of(velocity); }
  /** The Lorentz factor, being the time component of the four-velocity (never negative). */
  public double Γ() {return on(CT);} 

  /** The time component is unchanged by this operation, but the spatial components switch sign. */
  @Override public NFourVelocity reverseClocks() {
    return NFourVelocity.of(velocity.reverseClocks());
  }
  
  @Override public NFourVelocity reverseSpatialAxes() {
    return NFourVelocity.of(velocity.reverseSpatialAxes());
  }
  
  @Override public NFourVelocity rotate(NAxisAngle axisAngle, NSense sense) {
    return NFourVelocity.of(velocity.rotate(axisAngle, sense));
  }
  
  @Override public NFourVelocity boost(NVelocity v, NSense sense) {
    NBoost boost = NBoost.of(v, sense);
    NComponents comps = boost.applyTo(components);
    //"reverse-engineer" the comps into a velocity, then into a four-velocity
    double Γ = comps.ct();
    NThreeVector v_new = NThreeVector.of(comps.x(), comps.y(), comps.z()).divide(Γ);
    return NFourVelocity.of(NVelocity.of(v_new));
  }
  
  private NVelocity velocity;

  private NFourVelocity(NVelocity velocity) {
    Util.mustHave(velocity.magnitude() < 1.0, "Velocity input to 4-velocity must be less than 1.0: " + velocity);
    this.velocity = velocity;
    double Γ = Physics.Γ(velocity.magnitude()); //always positive, even under clock-reversal
    this.components = NComponents.of(
      Γ, 
      Γ * velocity.x(), 
      Γ * velocity.y(), 
      Γ * velocity.z()
    );
  }
}
