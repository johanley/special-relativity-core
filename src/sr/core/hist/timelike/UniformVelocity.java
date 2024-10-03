package sr.core.hist.timelike;

import sr.core.component.Event;
import sr.core.component.Position;
import sr.core.vec3.AxisAngle;
import sr.core.vec3.Velocity;
import sr.core.vec4.FourDelta;

/**
 History for a particle with mass moving uniformly at a given speed, and in a given direction.
 The general character of this history: 
 
  <pre>
            CT
            ^
            |       *
            |      *
            |     *
 ----------------*------------&gt; X
            |   *
            |  * 
            | *
 </pre>
 <p>If the speed is 0, then this history corresponds to a stationary object.
*/
public final class UniformVelocity extends TimelikeMoveableHistory {

  /**
   Factory method.
   The overall speed must be in the range [0,1).
   @param deltaBase of the history, relative to which this history acts.
   @param velocity of the object, possibly 0 (for a stationary object).
  */
  public static UniformVelocity of(TimelikeDeltaBase deltaBase, Velocity velocity) {
    return new UniformVelocity(deltaBase, velocity);
  }

  /**
   Factory method.
   The overall speed must in the range [0,1).
   @param initialPosition for the object at ct=0, with proper-time zero at that time as well.
   @param velocity of the object, possibly 0 (for a stationary object).
  */
  public static UniformVelocity of(Position initialPosition, Velocity velocity) {
    return new UniformVelocity(initialPosition, velocity);
  }
  
  /**
   Factory method for a stationary object.
   @param deltaBase of the history, relative to which this history acts.
  */
  public static UniformVelocity stationary(TimelikeDeltaBase deltaBase) {
    return new UniformVelocity(deltaBase, Velocity.zero());
  }
  
  /**
   Factory method for a stationary object.
   @param initialPosition for the object at ct=0, with proper time zero at that time as well.
  */
  public static UniformVelocity stationary(Position initialPosition) {
    return new UniformVelocity(TimelikeDeltaBase.of(initialPosition), Velocity.zero());
  }
  
  @Override protected FourDelta delta(double Δct) {
    Event b = Event.of(Δct, Δct*velocity.x(), Δct*velocity.y(), Δct*velocity.z());
    return FourDelta.withRespectToOrigin(b);
  }
  
  @Override protected double Δct(double Δτ) {
    return Δτ * velocity.Γ();
  }
  
  @Override protected double Δτ(double Δct) {
    return Δct / velocity.Γ();
  }
  
  @Override public AxisAngle rotation(double Δct) {
    return AxisAngle.zero();
  }
  
  @Override public String toString() {
    return "UniformVelocity " + timelikeDeltaBase() + " velocity:" + velocity;
  }
  
  private Velocity velocity;

  private UniformVelocity(Position initialPosition, Velocity velocity) {
    this(TimelikeDeltaBase.of(initialPosition), velocity);
  }
  
  private UniformVelocity(TimelikeDeltaBase deltaBase, Velocity velocity) {
    super(deltaBase);
    this.velocity = velocity;
  }
}