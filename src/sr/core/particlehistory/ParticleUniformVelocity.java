package sr.core.particlehistory;

import sr.core.Position;
import sr.core.Util;
import sr.core.Velocity;
import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.FourVector;
import static sr.core.Axis.*;

/**
 History for a particle with mass moving uniformly at a given speed, and in a given direction.
*/
public final class ParticleUniformVelocity implements ParticleHistory {

  /**
   Constructor.
    
   The overall speed must be a non-zero value in the range (0,1).
   @param mass must be positive
   @param initialPosition for the object at cτ=0
   @param velocity non-zero velocity
  */
  public ParticleUniformVelocity(double mass, Position initialPosition, Velocity velocity) {
    Util.mustHave(velocity.magnitude() > 0, "Speed must be greater than zero.");
    this.velocity = velocity;
    this.initialEvent = initialPosition.eventForTime(0.0);
    this.fourMomentum = velocity.fourMomentumFor(mass);
  }
  
  /** For a particle having unit mass, and parameterized with the coordinate-time. */
  public ParticleUniformVelocity(Position initialPosition, Velocity velocity) {
    this(1.0, initialPosition, velocity);
  }

  /** @param ct is the coordinate-time. */
  @Override public FourVector event(double ct) {
    FourVector displacement = FourVector.from(ct, ct*velocity.on(X), ct*velocity.on(Y), ct*velocity.on(Z), ApplyDisplaceOp.NO);
    return initialEvent.plus(displacement);
  }
  
  /** 
   The 4-momentum has a fixed value.
   @param ct is the coordinate-time; it's not used in the implementation of this method.
  */
  @Override public FourVector fourMomentum(double ct) {
    return fourMomentum;
  }
  
  /** 
   The zero of proper-time is taken as the event with ct = 0.
   @param ct is the coordinate-time.
  */
  @Override public double τ(double ct) {
    return ct / velocity.Γ();
  }
  
  private FourVector initialEvent;
  private FourVector fourMomentum;
  private Velocity velocity;
}