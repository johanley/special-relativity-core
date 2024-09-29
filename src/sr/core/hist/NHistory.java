package sr.core.hist;

import sr.core.component.NEvent;

/** 
The parameterized history (world-line) of an object (with or without mass) represented as a particle.

<P>In this interface, the coordinate-time is taken as the parameter to a history.
*/
public interface NHistory {

  /** Return the event for the given coordinate-time. */
  public NEvent event(double ct);

}
