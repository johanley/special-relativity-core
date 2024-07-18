package sr.core.particlehistory;

import sr.core.Position;
import sr.core.Util;
import sr.core.Velocity;
import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.FourVector;

/** 
 History for a particle with mass that doesn't move from a given position. 
 This is the simplest possible history.
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

  /** For an object having unit mass. */
  public ParticleStationary(Position position) {
    this(1.0, position);
  }
  
  /** @param ct is the coordinate-time. In this case, it is also a proper-time. */
  @Override public FourVector event(double ct) {
    FourVector displaceInTimeOnly = FourVector.from(ct, 0.0, 0.0, 0.0, ApplyDisplaceOp.YES);
    return initialEvent.plus(displaceInTimeOnly);
  }

  /** 
   Constant 4-momentum, with the spatial components = 0, and the time component = mass * c.
   @param ct is the coordinate-time; it's not used by this method, since the 4-momentum is constant. 
  */
  @Override public FourVector fourMomentum(double ct) {
    return fourMomentum;
  }
  
  private FourVector initialEvent;
  private FourVector fourMomentum;
}
