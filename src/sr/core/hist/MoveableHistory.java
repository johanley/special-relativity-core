package sr.core.hist;

import sr.core.component.Event;
import sr.core.vec3.Velocity;
import sr.core.vec4.FourDelta;

/**
 Allow a {@link History} to have a configurable {@link DeltaBase} in space-time.
 Methods are implemented as differences with respect to the {@link DeltaBase}. 
*/
public abstract class MoveableHistory implements History {

  /** @param deltaBase about which the history is constructed using differences. */
  protected MoveableHistory(DeltaBase deltaBase) {
    this.deltaBase = deltaBase;
  }

  /** 
   Return the event for the given coordinate-time.
   Adds the value returned by {@link #delta(double)} to the {@link #deltaBase()}. 
  */
  @Override public final Event event(double ct) {
    double Δct = ct - deltaBase.baseEvent().ct();
    FourDelta delta = delta(Δct);
    return Event.of(
      deltaBase.baseEvent().ct() + delta.ct(), 
      deltaBase.baseEvent().x() + delta.x(), 
      deltaBase.baseEvent().y() + delta.y(), 
      deltaBase.baseEvent().z() + delta.z() 
    );
  }
  /**
   Return the displacement to be added to the delta-base. 
   @param Δct is the difference between <em>ct</em> and the coordinate time attached to {@link DeltaBase#baseEvent()}.
  */
  protected abstract FourDelta delta(double Δct);
  
  public final DeltaBase deltaBase() { return deltaBase; }
  
  /** 
   Return an approximation to the velocity of the object at the given coordinate-time.
   <P>Implementations can override this method, if they have a more direct way of calculating the velocity.
   
   <P>This method can fail for ultra-relativistic speeds, because the approximate calculation returns a speed of 1.0.
   It will also fail at events where the velocity's derivative is not defined (for example, hard turning points).
   
   <P>If that's the case, you'll need to find other means to calculate the velocity, usually by overriding this method
   with a more appropriate algorithm.
  */
  public Velocity velocity(double ct) {
    Event a = event(ct);
    Event b = event(ct + 0.0001);
    FourDelta Δ = FourDelta.of(a, b);
    double Δt = Δ.ct();
    return Velocity.of(Δ.x()/Δt, Δ.y()/Δt, Δ.z()/Δt);
  }
  
  //PRIVATE
  
  private DeltaBase deltaBase;
  
}
