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
   Return the displacement relative to the delta-base. 
   @param Δct is the difference between ct and {@link DeltaBase#ΔbaseEvent()#ct(double)}.
  */
  protected abstract NFourDelta delta(double Δct);
  
  public final NDeltaBase deltaBase() { return deltaBase; }
  
  /** 
   Return an approximation to the velocity of the object at the given coordinate-time.
   <P>Implementations can override this method, if they have a non-approximate way of calculating the velocity.
   
   <P>This method can fail for ultra-relativistic speeds, because the 
   approximate calculation returns a speed of 1.0.
   It will also fail at events where the velocity's derivative is not defined (for example, hard turning points).
   
   <P>If that's the case, you'll need to find other means to calculate the velocity,  
   perhaps by overriding this method.
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
