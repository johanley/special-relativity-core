package sr.core.vec4;

import static sr.core.Axis.CT;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.Util;
import sr.core.component.Components;
import sr.core.component.ops.Boost;
import sr.core.component.ops.Sense;
import sr.core.ops.LinearBoostOp;
import sr.core.ops.LinearOps;
import sr.core.vec3.AxisAngle;
import sr.core.vec3.Direction;
import sr.core.vec3.ThreeVector;
import sr.core.vec3.Velocity;

/** 
 Velocity as a time-like four-vector, defined as differential displacement divided by proper-time.
 
 <P>Applies only to objects having mass.
 <P>Note that {@link Velocity} allows for speed = 1, but this class does not.
*/
public final class FourVelocity extends FourVector implements LinearOps<FourVelocity>, LinearBoostOp<FourVelocity> {

  /** Factory method. */
  public static FourVelocity of(Velocity velocity) {
    return new FourVelocity(velocity);
  }
  
  /** Factory method. */
  public static FourVelocity of(double magnitude, Direction direction) {
    return new FourVelocity(Velocity.of(magnitude, direction));
  }
  
  /** Factory method. */
  public static FourVelocity of(double magnitude, Axis axis) {
    return new FourVelocity(Velocity.of(magnitude, axis));
  }
  
  public Velocity velocity() { return velocity; }
  
  /** Never negative. The magnitude of the velocity. */
  public double β() { return velocity.magnitude(); }
  public Direction direction() { return Direction.of(velocity); }
  /** The Lorentz factor, being the time component of the four-velocity (never negative). */
  public double Γ() {return on(CT);} 

  /** The time component is unchanged by this operation, but the spatial components switch sign. */
  @Override public FourVelocity reverseClocks() {
    return FourVelocity.of(velocity.reverseClocks());
  }
  
  @Override public FourVelocity reverseSpatialAxes() {
    return FourVelocity.of(velocity.reverseSpatialAxes());
  }
  
  @Override public FourVelocity rotate(AxisAngle axisAngle, Sense sense) {
    return FourVelocity.of(velocity.rotate(axisAngle, sense));
  }
  
  @Override public FourVelocity boost(Velocity v, Sense sense) {
    Boost boost = Boost.of(v, sense);
    Components comps = boost.applyTo(components);
    //"reverse-engineer" the comps into a velocity, then into a four-velocity
    double Γ = comps.ct();
    ThreeVector v_new = ThreeVector.of(comps.x(), comps.y(), comps.z()).divide(Γ);
    return FourVelocity.of(Velocity.of(v_new));
  }
  
  private Velocity velocity;

  private FourVelocity(Velocity velocity) {
    Util.mustHave(velocity.magnitude() < 1.0, "Velocity input to 4-velocity must be less than 1.0: " + velocity);
    this.velocity = velocity;
    double Γ = Physics.Γ(velocity.magnitude()); //always positive, even under clock-reversal
    this.components = Components.of(
      Γ, 
      Γ * velocity.x(), 
      Γ * velocity.y(), 
      Γ * velocity.z()
    );
  }
}
