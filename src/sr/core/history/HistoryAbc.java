package sr.core.history;

import sr.core.Physics;
import sr.core.transform.FourVector;

/** Abstract Base Class for a {@link History} implementation. */
public abstract class HistoryAbc implements History {

  /** Applies a check on the limits of τ, then calls {@link #eventFor(double)}.  */
  public final FourVector event(double τ) {
    withinLimits(τ);
    return eventFor(τ);
  }
  protected abstract FourVector eventFor(double τ);
  
  @Override public final FourVector start() {
    return event(τmin());
  }
  
  @Override public final FourVector end() {
    return event(τmax());
  }

  /** Applies a check on the limits of τ, then calls {@link #fourVelocityFor(double)}.  */
  public final FourVector fourVelocity(double τ) {
    withinLimits(τ);
    return fourVelocityFor(τ);
  }
  /** 
   Overridable default implementation that calculates the derivative 'manually', by 
   calling {@link #event(double)}, and calculating the rate of change.
   <b>This default implementation assumes that the τ parameter is indeed the proper time.</b>
   
   <P>Many Implementations will override this default with more direct means of computing the 
   4-velocity. 
  */
  protected FourVector fourVelocityFor(double τ) {
    //TO DO? OR REMOVE THIS??
    return null;
  }
  
  /** 
   This default implementation uses the four-velocity.
   Many implementations will prefer to override this with a more direct computation of the speed. 
   <P>This default implementation has mediocre accuracy for low speeds. 
  */
  @Override public double β(double τ) {
    double Γ = fourVelocity(τ).ct();
    return Physics.β(Γ);
  }

  /** Throw a runtime exception if the τ parameter isn't within its min-max limits. */ 
  protected final void withinLimits(double τ) {
    if (τ < τmin() || τ > τmax()) {
      throw new IllegalArgumentException("Proper time " + τ + " is not in range " + τmin() + ".." + τmax());
    }
  }

  /** Return the difference between the τ parameter and its minimum. */
  protected final double Δτ(double τ) {
    withinLimits(τ);
    return τ - τmin();
  }
  
}
