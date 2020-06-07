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
  
  /** The first event on the history, corresponding to {@link #τmin()}. */
  FourVector start();
  
  /** The last event on the history, corresponding to {@link #τmax()}. */
  FourVector end();
  
  /** 
   The 4-velocity is computed, by default, simply by manually calculating the 
   derivative using the given {@link #event(double)} method.
   This default implementation assumes that the τ parameter is indeed the proper time.
   
   <P>Many Implementations will override this default with more direct means of computing the 
   4-velocity. 
  */
  FourVector fourVelocity(double τ);
  
  /** Magnitude of the 3-velocity. */
  double β(double τ);

  /** The start-value for the τ parameter. */
  double τmin();
  
  /** The end-value for the τ parameter. */
  double τmax();
}
