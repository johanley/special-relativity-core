package sr.core.history;

import sr.core.transform.FourVector;

/** A parameterized history (worldline) in space-time. */
public interface History {

  /**
   For the given parameter, return an event.
   
   The parameter uniquely identifies each event in the history.
   
   <P>The parameter is often the proper-time of an object having this history, but it 
   doesn't have to be the proper-time. It might even be simply the coordinate-time, 
   or any other convenient param.
  */
  FourVector event(double τ);

  /** The start-value for the τ parameter. */
  double τmin();
  
  /** The end-value for the τ parameter. */
  double τmax();
  
  /** The first event on the history, corresponding to {@link #τmin()}. */
  FourVector start();
  
  /** The last event on the history, corresponding to {@link #τmax()}. */
  FourVector end();
  
  /** The 4-velocity for the given value of the parameter. */
  FourVector fourVelocity(double τ);
  
  /** Magnitude of the 3-velocity. Always positive. */
  double β(double τ);

}
