package sr.core.hist.timelike;

import sr.core.component.Event;
import sr.core.component.Position;
import sr.core.hist.DeltaBase;

/** 
 The base event (and corresponding proper-time) for a history defined in terms of <em>displacements</em> from a particular event.

 <em>The delta-base will often exploit a symmetry.</em> For example,
 <ul> 
   <li>for circular motion, it could be related to the center of the circle. 
   In this case, it's not even part of the history of the object.
   <li>for constant proper-acceleration, it could be the turn-around point. 
 </ul>
*/
public final class TimelikeDeltaBase extends DeltaBase {

  /** 
   Factory method.
   @param ΔbaseEvent from which a history is generated by "deltas".
   @param ΔbaseEvent_τ is the value of the proper time for the <code>ΔBaseEvent</code>.  
  */
  public static TimelikeDeltaBase of(Event ΔbaseEvent, double ΔbaseEvent_τ) {
    return new TimelikeDeltaBase(ΔbaseEvent, ΔbaseEvent_τ);  
  }

  /** Factory method for the delta-base at the given position, and with <code>ct=0</code> and proper time = 0. */
  public static TimelikeDeltaBase of(Position position) {
    return new TimelikeDeltaBase(Event.of(0.0, position), 0.0);  
  }

  /** Factory method to derive a delta-base from an event on a history. */
  public static TimelikeDeltaBase of(TimelikeHistory history, double ct) {
    return new TimelikeDeltaBase(history.event(ct), history.τ(ct));  
  }

  /** Factory method using (0,0,0,0) as the delta-base-event, with proper time = 0 as well. */
  public static TimelikeDeltaBase origin() {
    return new TimelikeDeltaBase(Event.of(0.0, Position.of(0.0, 0.0, 0.0)), 0.0);  
  }
  
  public double ΔbaseEvent_τ() {return ΔbaseEvent_τ;}
  
  @Override public String toString() {
    return "DeltaBase event:" + baseEvent() + " τ:" + ΔbaseEvent_τ;
  }
  
  private TimelikeDeltaBase(Event ΔbaseEvent, double ΔbaseEvent_τ) {
    super(ΔbaseEvent);
    this.ΔbaseEvent_τ = ΔbaseEvent_τ;
  }
  
  private double ΔbaseEvent_τ;
}