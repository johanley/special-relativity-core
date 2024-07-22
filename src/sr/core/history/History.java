package sr.core.history;

import sr.core.event.Event;

/** 
 The parameterized history (world-line) of an object represented as a particle.
 
 <P>The caller needs to define exactly what the parameter λ of the history represents.
 λ can represent different things:
 <ul> 
  <li>the coordinate-time ct in a given frame 
  <li>the proper-time cτ of an object having mass)  
  <li>any other parameter having suitable uniqueness/continuity attributes
 </ul>
 
 <P>In most cases, the coordinate-time is the most convenient way to identify events.
 
 <P>This interface, by itself, models only particles, not extended objects.
 So, the histories it represents are curves in space-time, not extended tubes.
 
 <P>The history-tube of an extended object might be represented with N of these particle-histories.
 For example, a stick might be represented by two particle histories, one for each end of the stick. 
*/
public interface History {

  /**
   Return the event for the given parameter.
   @param λ uniquely identifies each event in the history.
  */
  public Event event(double λ);

  /**
   Return a proper-time value.
   There's no single proper-time, since the zero can be chosen in different ways.
     
   @param λ uniquely identifies each event in the history.
  */
  public double τ(double λ);
  
}
