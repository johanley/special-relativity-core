package sr.core.history;

import sr.core.event.Event;

/** 
The parameterized history (world-line) of an object represented as a particle.

<P>In general, the parameter to a history could be:
<ul> 
 <li>the coordinate-time ct in a given frame 
 <li>a proper-time τ of an object having mass travelling along the history
 <li>any other parameter having suitable uniqueness/continuity attributes
</ul>

<P>In this interface, the coordinate-time is taken as the parameter to a history.
Note, however, that the proper-time can be used as a parameter implicitly, since 
this interface supplies the necessary conversion methods from a proper-time to 
coordinate-time (and <em>vice versa</em>).

<P>This interface, by itself, models only particles, not extended objects.
So, the histories it represents are curves in space-time, not extended tubes.

<P>The history-tube of an extended object might be represented with N of these particle-histories.
For example, a stick might be represented by two particle histories, one for each end of the stick. 
*/
public interface History2 {

  /** Return the event for the given coordinate-time. */
  public Event event(double ct);
  
  /**
   Convert a proper-time into a coordinate-time.
   The inverse of {@link #τ(double)}.
  */
  public double ct(double τ);
  
  /** 
   Convert the coordinate-time into a proper time. 
   Since the zero of proper-time is arbitrary, the proper-time is not unique. 
   The inverse of {@link #ct(double)}. 
  */
  public double τ(double ct);

}
