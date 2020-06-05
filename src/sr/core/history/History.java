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
  
  default FourVector fourVelocity(double τ) {
    //TO DO !!!
    return null;
  }

  /** The start-value for the τ parameter.  */
  double τmin();
  
  /** The end-value for the τ parameter.  */
  double τmax();
  
  /** Throws a runtime exception if the τ parameter isn't within its min-max limits. */ 
  default void withinLimits(double τ) {
    if (τ < τmin() || τ > τmax()) {
      throw new IllegalArgumentException("Proper time " + τ + " is not in range " + τmin() + ".." + τmax());
    }
  }

  /** The difference between the τ parameter and its minimum. */
  default double Δτ(double τ) {
    return τ - τmin();
  }

}
