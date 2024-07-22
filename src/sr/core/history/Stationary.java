package sr.core.history;

import sr.core.Util;
import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.FourVector;
import sr.core.vector.Position;
import sr.core.vector.Velocity;

/** 
 History for a particle with mass that doesn't move from a given position. 
 This is the simplest possible history.
*/
public final class Stationary implements History {

  /**
   Constructor. 
    
   @param mass must be positive
   @param position initial position for <em>ct=0</em>.
  */
  public Stationary(double mass, Position position) {
    Util.mustHave(mass > 0, "Mass must be positive.");
    initialEvent = position.eventForTime(0.0);
    fourMomentum = Velocity.zero().fourMomentumFor(mass);
  }

  /** For an object having unit mass. */
  public Stationary(Position position) {
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
 
  /**
   In this case, the proper-time and the coordinate-time are the same value.
   @param ct is the coordinate-time.
  */ 
  @Override public double τ(double ct) {
    return ct;
  }
  
  private FourVector initialEvent;
  private FourVector fourMomentum;
}
