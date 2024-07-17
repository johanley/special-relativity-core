package sr.core.particlehistory;

import sr.core.Position;
import sr.core.Util;
import sr.core.Velocity;
import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.FourVector;

/**
 History for a particle with mass moving uniformly at a given speed, and in a given direction.
 
 <P>In this case, the parameter for the history is identified with the proper time cτ.
 The zero of proper time is taken as ct=0.
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
    this.v = velocity;
    this.initialEvent = initialPosition.eventForTime(0.0);
    this.fourMomentum = velocity.fourMomentumFor(mass);
  }
 
  /** @param cτ is the proper time along the history. */
  @Override public FourVector event(double cτ) {
    //change from proper time to coordinate time
    double ct = ct(cτ);
    FourVector displacement = FourVector.from(ct, ct*v.βx(), ct*v.βy(), ct*v.βz(), ApplyDisplaceOp.NO);
    return initialEvent.plus(displacement);
  }
  
  /** 
   The four-momentum has a fixed value.
   @param cτ is the proper time along the history; it's not used in the implementation of this method.
  */
  @Override public FourVector fourMomentum(double cτ) {
    return fourMomentum;
  }
  
  private FourVector initialEvent;
  private FourVector fourMomentum;
  private Velocity v;
  
  /** Time dilation. Convert from proper time to coordinate time. */
  private double ct(double cτ) {
    return v.Γ() * cτ;
  }
}