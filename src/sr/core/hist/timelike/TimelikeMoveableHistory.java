package sr.core.hist.timelike;

import sr.core.component.Event;
import sr.core.hist.DeltaBase;
import sr.core.hist.MoveableHistory;
import sr.core.vec3.AxisAngle;

/**
 Allow a {@link TimelikeHistory} to have a configurable {@link TimelikeDeltaBase} in space-time.
 Methods are implemented as differences with respect to the {@link TimelikeDeltaBase}. 
*/
public abstract class TimelikeMoveableHistory extends MoveableHistory implements TimelikeHistory {

  /** @param timelikeDeltaBase about which the history is constructed using differences. */
  protected TimelikeMoveableHistory(TimelikeDeltaBase timelikeDeltaBase) {
    super(DeltaBase.of(timelikeDeltaBase.baseEvent()));
    this.timeLikeDeltaBase = timelikeDeltaBase;
  }

  /** Return the {@link Event} given a proper-time as the parameter into the {@link TimelikeHistory}. */
  public final Event eventFromProperTime(double τ) {
    return event(ct(τ));
  }
  
  @Override public double ct(double τ) {
    double Δτ = τ - timeLikeDeltaBase.ΔbaseEvent_τ();
    return timeLikeDeltaBase.baseEvent().ct() + Δct(Δτ);
  }
  /**
   Return the change in coordinate-time relative to the delta-base, given the change in 
   proper-time relative to the delta-base.
   @param Δτ is the difference between τ and {@link TimelikeDeltaBase#ΔbaseEvent_τ()}.
  */
  protected abstract double Δct(double Δτ);

  @Override public double τ(double ct) {
    double Δct = ct - timeLikeDeltaBase.baseEvent().ct();
    return timeLikeDeltaBase.ΔbaseEvent_τ() + Δτ(Δct);
  }
  /**
   Return the change in a proper-time relative to the delta-base, given the change in 
   coordinate-time relative to the delta-base.
   @param Δct is the difference between <em>ct</em> and {@link DeltaBase#baseEvent()}<em>.ct()</em>.
  */
  protected abstract double Δτ(double Δct);
  
  
  public TimelikeDeltaBase timelikeDeltaBase() { return timeLikeDeltaBase; }
  
  /**
   How the object has rotated because of kinematic (Thomas-Wigner) rotation of the co-moving frame.
   <P>This quantity is similar to proper-time, in that the zero is arbitrary.
   <P>Returns a zero-vector if the motion is in a straight line.
   <P>There's no restriction on the magnitude of the returned axis-angle; for example, it's not restricted to the range 0..2pi range.
   This helps in knowing how many full rotations have taken place. 
  */
  public abstract AxisAngle rotation(double Δct);
  
  //PRIVATE
  
  private TimelikeDeltaBase timeLikeDeltaBase;
  
}
