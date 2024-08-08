package sr.core.history;

import sr.core.event.Event;
import sr.core.vector.AxisAngle;
import sr.core.vector.Velocity;

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
   @param Δct is the difference between ct and {@link DeltaBase#ΔbaseEvent()#ct(double)}.
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
   @param Δct is the difference between ct and {@link DeltaBase#ΔbaseEvent()#ct(double)}.
  */
  protected abstract double Δτ(double Δct);
  
  
  public DeltaBase deltaBase() { return deltaBase; }
  
  /** 
   Return an approximation to the velocity of the object at the given coordinate-time.
   <P>This method can fail for ultra-relativistic speeds, because the 
   approximate calculation returns a speed of 1.0.
   It will also fail at events where the velocity's derivative is not defined (for example, hard turning points).
   <P>If that's the case, you'll need to find other means to calculate the velocity,  
   perhaps by overriding this method.
  */
  public Velocity velocity(double ct) {
    Event a = event(ct);
    Event b = event(ct + 0.0001);
    Event Δ = b.minus(a);
    double Δt = Δ.ct();
    return Velocity.of(Δ.x()/Δt, Δ.y()/Δt, Δ.z()/Δt);
  }
  
  /**
   How the object has rotated because of Silberstein (Thomas-Wigner) rotation of the co-moving frame.
   <P>This quantity is similar to proper-time, in that the zero is arbitrary.
   <P>Returns a zero-vector if the motion is in a straight line.
   <P>There's no restriction on the magnitude of the returned axis-angle; for example, it's not restricted to the range 0..2pi range.
   This helps in knowing how many full rotations have taken place. 
  */
  public abstract AxisAngle rotation(double Δct);
  
  //PRIVATE
  
  private DeltaBase deltaBase;
  
}
