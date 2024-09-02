package sr.core.history.timelike;

import sr.core.history.DeltaBase;
import sr.core.history.MoveableHistory;
import sr.core.vector3.AxisAngle;
import sr.core.vector4.Event;

/**
 Allow a {@link TimelikeHistory} to have a configurable {@link TimelikeDeltaBase} in space-time.
 Methods are implemented as differences with respect to the {@link TimelikeDeltaBase}. 
*/
public abstract class TimelikeMoveableHistory extends MoveableHistory implements TimelikeHistory {

  /** @param timelikeDeltaBase about which the history is constructed using differences. */
  protected TimelikeMoveableHistory(TimelikeDeltaBase timelikeDeltaBase) {
    super(DeltaBase.of(timelikeDeltaBase.ΔbaseEvent()));
    this.timeLikeDeltaBase = timelikeDeltaBase;
  }

  /** Return the {@link Event} given a proper-time as the parameter into the {@link TimelikeHistory}. */
  public final Event eventFromProperTime(double τ) {
    return event(ct(τ));
  }
  
  @Override public double ct(double τ) {
    double Δτ = τ - timeLikeDeltaBase.ΔbaseEvent_τ();
    return timeLikeDeltaBase.ΔbaseEvent().ct() + Δct(Δτ);
  }
  /**
   Return the change in coordinate-time relative to the delta-base, given the change in 
   proper-time relative to the delta-base.
   @param Δτ is the difference between τ and {@link TimelikeDeltaBase#ΔbaseEvent_τ()}.
  */
  protected abstract double Δct(double Δτ);

  @Override public double τ(double ct) {
    double Δct = ct - timeLikeDeltaBase.ΔbaseEvent().ct();
    return timeLikeDeltaBase.ΔbaseEvent_τ() + Δτ(Δct);
  }
  /**
   Return the change in a proper-time relative to the delta-base, given the change in 
   coordinate-time relative to the delta-base.
   @param Δct is the difference between ct and {@link DeltaBase#ΔbaseEvent()#ct(double)}.
  */
  protected abstract double Δτ(double Δct);
  
  
  public TimelikeDeltaBase timelikeDeltaBase() { return timeLikeDeltaBase; }
  
  /**
   How the object has rotated because of Silberstein (Thomas-Wigner) rotation of the co-moving frame.
   <P>This quantity is similar to proper-time, in that the zero is arbitrary.
   <P>Returns a zero-vector if the motion is in a straight line.
   <P>There's no restriction on the magnitude of the returned axis-angle; for example, it's not restricted to the range 0..2pi range.
   This helps in knowing how many full rotations have taken place. 
  */
  public abstract AxisAngle rotation(double Δct);
  
  //PRIVATE
  
  private TimelikeDeltaBase timeLikeDeltaBase;
  
}
