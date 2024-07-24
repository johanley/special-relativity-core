package sr.core.event.transform;

import static sr.core.Util.isTiny;

import sr.core.event.Event;

/**
 Map one event to another event, and its inverse operation.

 <P> 
 For every transformation, there must be a corresponding inverse transformation, which will recover the original event.
 That's why two methods are defined for this interface.
 Most, but not all, inverse operations involve the change of sign of some quantity.
 
 <P><b>There are 2 opposite use cases here</b>:
 <ul>
  <li>given the components of a {@link Event} in one inertial frame K, find its components in a second frame K'
   ({@link #apply(Event)}, with inverse {@link #reverse(Event)}) 
  <li>given the components of a {@link Event} in one inertial frame K, find the components of a second event in the same frame K.
     ({@link #reverse(Event)}, with inverse {@link #apply(Event)}) 
 </ul>
 
 <P>
 Successive application of the two methods {@link #apply(Event)} and {@link #reverse(Event)} 
 (in any order) must return the original event (aside from some rounding that usually occurs because of 
 floating-point operations).
*/
public interface Transform {
  
  /** 
   For a given event represented relative to K, represent the same event relative to K'. 
    
   The inverse operation is {@link #reverse(Event)}.
   
   @param event the components of an event in the K frame.
   @return the components of the same event in the K' frame. 
  */
  Event apply(Event event);
  
  /** 
   For a given event represented relative to K, return a second event represented relative to K. 
    
   The inverse operation is {@link #apply(Event)}.
   
   @param event the components in a given frame K.
   @return the components of a second event in a given frame K. 
  */
  Event reverse(Event event);

  /**
   Asserts that the magnitude-squared has not changed (very much).
   
   Convenience method for implementations to validate their result. 
   This will not apply to all coord transforms.  
   Uses an assertion, which are turned off at runtime by default. 
  */
  static void sameIntervalFromOrigin(Event a, Event b) {
    assert isTiny(a.square() - b.square()) : "Magnitude-squared has changed too much.";
  }
}
