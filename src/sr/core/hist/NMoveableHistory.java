package sr.core.hist;

import sr.core.component.NEvent;
import sr.core.vec3.NVelocity;
import sr.core.vec4.NFourDelta;

/**
 Allow a {@link NHistory} to have a configurable {@link NDeltaBase} in space-time.
 Methods are implemented as differences with respect to the {@link NDeltaBase}. 
*/
public abstract class NMoveableHistory implements NHistory {

  /** @param deltaBase about which the history is constructed using differences. */
  protected NMoveableHistory(NDeltaBase deltaBase) {
    this.deltaBase = deltaBase;
  }

  /** 
   Return the event for the given coordinate-time.
   Adds the value returned by {@link #delta(double)} to the {@link #deltaBase()}. 
  */
  @Override public final NEvent event(double ct) {
    double Δct = ct - deltaBase.baseEvent().ct();
    NFourDelta delta = delta(Δct);
    return NEvent.of(
      deltaBase.baseEvent().ct() + delta.ct(), 
      deltaBase.baseEvent().x() + delta.x(), 
      deltaBase.baseEvent().y() + delta.y(), 
      deltaBase.baseEvent().z() + delta.z() 
    );
  }
  /**
   Return the displacement to be added to the delta-base. 
   @param Δct is the difference between <em>ct</em> and the coordinate time attached to {@link NDeltaBase#ΔbaseEvent()}.
  */
  protected abstract NFourDelta delta(double Δct);
  
  public final NDeltaBase deltaBase() { return deltaBase; }
  
  /** 
   Return an approximation to the velocity of the object at the given coordinate-time.
   <P>Implementations can override this method, if they have a more direct way of calculating the velocity.
   
   <P>This method can fail for ultra-relativistic speeds, because the approximate calculation returns a speed of 1.0.
   It will also fail at events where the velocity's derivative is not defined (for example, hard turning points).
   
   <P>If that's the case, you'll need to find other means to calculate the velocity, usually by overriding this method
   with a more appropriate algorithm.
  */
  public NVelocity velocity(double ct) {
    NEvent a = event(ct);
    NEvent b = event(ct + 0.0001);
    NFourDelta Δ = NFourDelta.of(a, b);
    double Δt = Δ.ct();
    return NVelocity.of(Δ.x()/Δt, Δ.y()/Δt, Δ.z()/Δt);
  }
  
  //PRIVATE
  
  private NDeltaBase deltaBase;
  
}
