package sr.core.history;

import sr.core.transform.FourVector;

/** 
 The parameterized history (worldline) of an object represented as a particle.
 
 <P>The caller needs to define exactly what the parameter τ represents.
 τ can represent different things:
 <ul> 
  <li>the coordinate-time in a given frame 
  <li>the proper-time of the object  
  <li>any other parameter having suitable uniqueness/continuity attributes
 </ul>
 (It's even possible for an implementation to allow for more than 1 of the above cases, perhaps
 with an indicator passed to the constructor.) There's even 1 case in which 
 the coordinate-time is the same as the proper-time: when the object is stationary.
 
 <P>You may find it best to prefer the time-coordinate; it may make it 
 easiest when N histories are needed (for taking a time-slice, for instance).
  
 <P>This interface, by itself, models only particles, not extended objects.
 So, the histories it represents are curves in space-time, not extended tubes.
 
 <P>The history-tube of an extended object might be represented with N of these particle-histories. 
*/
public interface History {

  /**
   Return the event for the given parameter.
   The τ parameter uniquely identifies each event in the history.
  */
  FourVector event(double τ);

  /** 
   Return the 4-velocity for the given parameter.
   The τ parameter uniquely identifies each event in the history.
   
   <P>Implementations can check their work by validating that the magnitudeSquared of 
   the returned vector is 1.0 (allowing for the usual small rounding differences). 
  */
  FourVector fourVelocity(double τ);
  
  /** 
   Return the magnitude of the 3-velocity, for the given parameter. Always positive. 
   The τ parameter uniquely identifies each event in the history.
  */
  double β(double τ);
  
  /** The start-value for the τ parameter. */
  double τmin();
  
  /** The end-value for the τ parameter. */
  double τmax();
  
  /** The first event on the history, corresponding to {@link #τmin()}. */
  FourVector start();
  
  /** The last event on the history, corresponding to {@link #τmax()}. */
  FourVector end();
  
}
