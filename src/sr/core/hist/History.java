package sr.core.hist;

import sr.core.component.Event;

/** 
The parameterized history (world-line) of an object (with or without mass) represented as a particle.

<P>In this interface, the coordinate-time is taken as the parameter to a history.
*/
public interface History {

  /** Return the event for the given coordinate-time. */
  public Event event(double ct);

}
