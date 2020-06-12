package sr.core.history;

import sr.core.Physics;
import sr.core.transform.FourVector;

/** 
 Abstract Base Class for a {@link History} implementation.
 
 The meaning of the history-parameter τ is not defined here.
 That is left up to concrete subclasses.
 It may be the proper-time of the object, coordinate-time, or any other 
 appropriate identifier for events in the object's history.  
*/
public abstract class HistoryAbc implements History {

  /** Applies a check on the limits of τ, then calls {@link #eventFor(double)}. */
  public final FourVector event(double τ) {
    withinLimits(τ);
    return eventFor(τ);
  }
  protected abstract FourVector eventFor(double τ);
  
  /** Applies a check on the limits of τ, then calls {@link #fourVelocityFor(double)}.  */
  public final FourVector fourVelocity(double τ) {
    withinLimits(τ);
    return fourVelocityFor(τ);
  }
  protected abstract FourVector fourVelocityFor(double τ);
  
  /** 
   This default implementation uses the four-velocity.
   Many implementations will prefer to override this with a more direct computation of the speed. 
   <P>This default implementation has mediocre accuracy for low speeds. 
  */
  @Override public double β(double τ) {
    //the check on the limits of τ is done on the next line
    double Γ = fourVelocity(τ).ct();
    return Physics.β(Γ);
  }

  /** The event corresponding to τmin. */
  @Override public final FourVector start() {
    return event(τmin());
  }
  
  /** The event corresponding to τmax. */
  @Override public final FourVector end() {
    return event(τmax());
  }

  /** 
   Throw a runtime exception if the τ parameter isn't within its min-max limits.
   Calling this method more than once does no harm.
  */ 
  protected final void withinLimits(double τ) {
    if (τ < τmin() || τ > τmax()) {
      throw new IllegalArgumentException("τ-parameter " + τ + " is not in range " + τmin() + ".." + τmax());
    }
  }

  /** Return the difference between the τ parameter and τmin. */
  protected final double Δτ(double τ) {
    withinLimits(τ);
    return τ - τmin();
  }
  
}
