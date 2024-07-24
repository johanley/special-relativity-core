package sr.core.history;

import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;

import sr.core.Physics;
import sr.core.Util;
import sr.core.event.Event;
import sr.core.vector.Position;
import sr.core.vector.Velocity;

/**
 History for a particle with mass moving uniformly at a given speed, and in a given direction.
 
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
 
*/
public final class UniformVelocity implements History {

  /**
   Factory method.
    
   The overall speed must be a non-zero value in the range (0,1).
   @param initialPosition for the object at cτ=0
   @param velocity non-zero velocity
  */
  public static UniformVelocity of(Position initialPosition, Velocity velocity) {
    return new UniformVelocity(initialPosition, velocity);
  }
  
  /** @param ct is the coordinate-time. */
  @Override public Event event(double ct) {
    Event displacement = Event.of(ct, ct*velocity.on(X), ct*velocity.on(Y), ct*velocity.on(Z));
    return initialEvent.plus(displacement);
  }
  
  /** 
   Convert coordinate-time to proper-time.
   The zero of proper-time is taken as the event with ct = 0.
   @param ct is the coordinate-time.
  */
  @Override public double convert(double ct) {
    return ct / Physics.Γ(velocity.magnitude());
  }
  
  @Override public String toString() {
    return "UniformVelocity initial-event:" + initialEvent + " velocity:" + velocity;
  }
  
  private Event initialEvent;
  private Velocity velocity;
  
  private UniformVelocity(Position initialPosition, Velocity velocity) {
    Util.mustHave(velocity.magnitude() > 0, "Speed must be greater than zero.");
    this.velocity = velocity;
    this.initialEvent = Event.of(0.0, initialPosition);
  }
  
}