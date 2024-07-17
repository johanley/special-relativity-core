package sr.core.particlehistory;

import sr.core.Position;
import sr.core.Util;
import sr.core.Velocity;
import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.FourVector;

/** 
 History for a particle with mass that doesn't move from a given position. 
 This is the simplest possible history.
 
 <P>In this case, the parameter for the history is identified both with with the ct-coordinate and with 
 the proper time cτ.
 The zero of proper time is taken as ct=0.
*/
public final class ParticleStationary implements ParticleHistory {

  /**
   Constructor. 
    
   @param mass must be positive
   @param position initial position for <em>ct=0</em>.
  */
  public ParticleStationary(double mass, Position position) {
    Util.mustHave(mass > 0, "Mass must be positive.");
    initialEvent = position.eventForTime(0.0);
    fourMomentum = Velocity.zero().fourMomentumFor(mass);
  }
  
  @Override public FourVector event(double cτ) {
    FourVector displaceInTimeOnly = FourVector.from(cτ, 0.0, 0.0, 0.0, ApplyDisplaceOp.YES);
    return initialEvent.plus(displaceInTimeOnly);
  }

  /** Constant 4-momentum, with the spatial components = 0, and the time component = mass * c. */
  @Override public FourVector fourMomentum(double cτ) {
    return fourMomentum;
  }
  
  private FourVector initialEvent;
  private FourVector fourMomentum;
}
