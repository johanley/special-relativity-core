package sr.core.particlehistory;

import sr.core.Position;
import sr.core.Util;
import sr.core.Velocity;
import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.FourVector;

/**
 History for a particle with mass moving uniformly at a given speed, and in a given direction.
*/
public final class ParticleMovingUniformly implements ParticleHistory {

  /**
   Constructor.
    
   The overall speed must be a non-zero value in the range (0,1).
   @param mass must be positive
   @param initialPosition for the object at cτ=0
   @param velocity non-zero velocity
  */
  public ParticleMovingUniformly(double mass, Position initialPosition, Velocity velocity) {
    Util.mustHave(velocity.β() > 0, "Speed must be greater than zero.");
    this.velocity = velocity;
    this.initialEvent = initialPosition.eventForTime(0.0);
    this.fourMomentum = velocity.fourMomentumFor(mass);
  }
  
  /** For a particle having unit mass, and parameterized with the coordinate-time. */
  public ParticleMovingUniformly(Position initialPosition, Velocity velocity) {
    this(1.0, initialPosition, velocity);
  }

  /** @param ct is the coordinate-time. */
  @Override public FourVector event(double ct) {
    FourVector displacement = FourVector.from(ct, ct*velocity.βx(), ct*velocity.βy(), ct*velocity.βz(), ApplyDisplaceOp.NO);
    return initialEvent.plus(displacement);
  }
  
  /** 
   The 4-momentum has a fixed value.
   @param ct is the coordinate-time; it's not used in the implementation of this method.
  */
  @Override public FourVector fourMomentum(double ct) {
    return fourMomentum;
  }
  
  private FourVector initialEvent;
  private FourVector fourMomentum;
  private Velocity velocity;
}