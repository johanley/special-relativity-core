package sr.core.hist.timelike;

import sr.core.component.NEvent;
import sr.core.hist.NDeltaBase;
import sr.core.hist.NMoveableHistory;
import sr.core.vec3.NAxisAngle;

/**
 Allow a {@link NTimelikeHistory} to have a configurable {@link NTimelikeDeltaBase} in space-time.
 Methods are implemented as differences with respect to the {@link NTimelikeDeltaBase}. 
*/
public abstract class NTimelikeMoveableHistory extends NMoveableHistory implements NTimelikeHistory {

  /** @param timelikeDeltaBase about which the history is constructed using differences. */
  protected NTimelikeMoveableHistory(NTimelikeDeltaBase timelikeDeltaBase) {
    super(NDeltaBase.of(timelikeDeltaBase.baseEvent()));
    this.timeLikeDeltaBase = timelikeDeltaBase;
  }

  /** Return the {@link NEvent} given a proper-time as the parameter into the {@link NTimelikeHistory}. */
  public final NEvent eventFromProperTime(double τ) {
    return event(ct(τ));
  }
  
  @Override public double ct(double τ) {
    double Δτ = τ - timeLikeDeltaBase.ΔbaseEvent_τ();
    return timeLikeDeltaBase.baseEvent().ct() + Δct(Δτ);
  }
  /**
   Return the change in coordinate-time relative to the delta-base, given the change in 
   proper-time relative to the delta-base.
   @param Δτ is the difference between τ and {@link NTimelikeDeltaBase#ΔbaseEvent_τ()}.
  */
  protected abstract double Δct(double Δτ);

  @Override public double τ(double ct) {
    double Δct = ct - timeLikeDeltaBase.baseEvent().ct();
    return timeLikeDeltaBase.ΔbaseEvent_τ() + Δτ(Δct);
  }
  /**
   Return the change in a proper-time relative to the delta-base, given the change in 
   coordinate-time relative to the delta-base.
   @param Δct is the difference between ct and {@link DeltaBase#ΔbaseEvent()#ct(double)}.
  */
  protected abstract double Δτ(double Δct);
  
  
  public NTimelikeDeltaBase timelikeDeltaBase() { return timeLikeDeltaBase; }
  
  /**
   How the object has rotated because of kinematic (Thomas-Wigner) rotation of the co-moving frame.
   <P>This quantity is similar to proper-time, in that the zero is arbitrary.
   <P>Returns a zero-vector if the motion is in a straight line.
   <P>There's no restriction on the magnitude of the returned axis-angle; for example, it's not restricted to the range 0..2pi range.
   This helps in knowing how many full rotations have taken place. 
  */
  public abstract NAxisAngle rotation(double Δct);
  
  //PRIVATE
  
  private NTimelikeDeltaBase timeLikeDeltaBase;
  
}
