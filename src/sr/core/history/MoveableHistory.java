package sr.core.history;

import sr.core.vector3.Velocity;
import sr.core.vector4.Event;

/**
 Allow a {@link History} to have a configurable {@link DeltaBase} in space-time.
 Methods are implemented as differences with respect to the {@link DeltaBase}. 
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
  
  public final DeltaBase deltaBase() { return deltaBase; }
  
  /** 
   Return an approximation to the velocity of the object at the given coordinate-time.
   <P>Implementations can override this method, if they have a non-approximate way of calculating the velocity.
   
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
  
  //PRIVATE
  
  private DeltaBase deltaBase;
  
}
