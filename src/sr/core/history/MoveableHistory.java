package sr.core.history;

import sr.core.event.Event;

/**
 Allow a {@link History} to have a configurable {@link DeltaBase} in space-time.
 Methods are implemented as differences with respect to the {@link DeltaBase}. 
 This is used by {@link StitchedHistoryBuilder} in stitching together multiple histories into a single history.
*/
public abstract class MoveableHistory implements History {

  /** @param deltaBase about which the history is constructed using differences. */
  protected MoveableHistory(DeltaBase deltaBase) {
    this.deltaBase = deltaBase;
  }

  @Override public final Event event(double ct) {
    double Δct = ct - deltaBase.ΔbaseEvent().ct();
    Event displacement = Δevent(Δct);
    return deltaBase.ΔbaseEvent().plus(displacement);
  }
  /**
   Return the displacement relative to the delta-base. 
   @param Δct is the difference between ct and {@link DeltaBase#event()#ct(double)}.
  */
  protected abstract Event Δevent(double Δct);
  
  /** Return the {@link Event} given a proper-time as the parameter into the {@link History}. */
  public final Event eventFromProperTime(double τ) {
    return event(ct(τ));
  }
  
  
  @Override public double ct(double τ) {
    double Δτ = τ - deltaBase.ΔbaseEvent_τ();
    return deltaBase.ΔbaseEvent().ct() + Δct(Δτ);
  }
  /**
   Return the change in coordinate-time relative to the delta-base, given the change in 
   proper-time relative to the delta-base.
   @param Δτ is the difference between τ and {@link DeltaBase#ΔbaseEvent_τ()}.
  */
  protected abstract double Δct(double Δτ);

  
  @Override public double τ(double ct) {
    double Δct = ct - deltaBase.ΔbaseEvent().ct();
    return deltaBase.ΔbaseEvent_τ() + Δτ(Δct);
  }
  /**
   Return the change in a proper-time relative to the delta-base, given the change in 
   coordinate-time relative to the delta-base.
   @param Δct is the difference between ct and {@link DeltaBase#event()#ct()}.
  */
  protected abstract double Δτ(double Δct);
  
  
  public DeltaBase deltaBase() { return deltaBase; }
  
  //PRIVATE
  
  private DeltaBase deltaBase;
  
}
