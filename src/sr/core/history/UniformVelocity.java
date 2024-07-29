package sr.core.history;

import sr.core.Physics;
import sr.core.event.Event;
import sr.core.vector.Position;
import sr.core.vector.Velocity;

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
public final class UniformVelocity extends MoveableHistory {

  /**
   Factory method.
   The overall speed must be in the range [0,1).
   @param deltaBase of the history, relative to which this history acts.
   @param velocity of the object, possibly 0 (for a stationary object).
  */
  public static UniformVelocity of(DeltaBase deltaBase, Velocity velocity) {
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
  public static UniformVelocity stationary(DeltaBase deltaBase) {
    return new UniformVelocity(deltaBase, Velocity.zero());
  }
  
  /**
   Factory method for a stationary object.
   @param initialPosition for the object at ct=0, with proper time zero at that time as well.
  */
  public static UniformVelocity stationary(Position initialPosition) {
    return new UniformVelocity(DeltaBase.of(initialPosition), Velocity.zero());
  }
  
  @Override protected Event Δevent(double Δct) {
    Event displacement = Event.of(Δct, Δct*velocity.x(), Δct*velocity.y(), Δct*velocity.z());
    return displacement;
  }
  
  @Override protected double Δct(double Δτ) {
    return Δτ * Physics.Γ(velocity.magnitude());
  }
  
  @Override protected double Δτ(double Δct) {
    return Δct / Physics.Γ(velocity.magnitude());
  }
  
  @Override public String toString() {
    return "UniformVelocity " + deltaBase() + " velocity:" + velocity;
  }
  
  private Velocity velocity;

  private UniformVelocity(Position initialPosition, Velocity velocity) {
    this(DeltaBase.of(initialPosition), velocity);
  }
  
  private UniformVelocity(DeltaBase deltaBase, Velocity velocity) {
    super(deltaBase);
    this.velocity = velocity;
  }
}