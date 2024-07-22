package sr.core.history;

import sr.core.event.Event;
import sr.core.vector.Position;

/** 
 History for a particle with mass that doesn't move from a given position. 
 This is the simplest possible history.
*/
public final class Stationary implements History {

  /**
   Constructor. 
    
   @param position initial position for <em>ct=0</em>.
  */
  public Stationary(Position position) {
    initialEvent = Event.of(0.0, position);
  }

  /** @param ct is the coordinate-time. In this case, it is also a proper-time. */
  @Override public Event event(double ct) {
    Event displaceInTimeOnly = Event.of(ct, 0.0, 0.0, 0.0);
    return initialEvent.plus(displaceInTimeOnly);
  }

  /**
   In this case, the proper-time and the coordinate-time are the same value.
   @param ct is the coordinate-time.
  */ 
  @Override public double τ(double ct) {
    return ct;
  }
  
  private Event initialEvent;
}
