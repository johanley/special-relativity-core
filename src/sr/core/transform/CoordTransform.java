package sr.core.transform;

import static sr.core.Util.isTiny;

/**
 Map one event to another event, and its inverse operation.

 <P> 
 For every transformation, there must be a corresponding inverse transformation, which will recover the original event.
 That's why two methods are defined for this interface.
 Most, but not all, inverse operations involve the change of sign of some quantity.
 
 <P><b>There are 2 opposite use cases here</b>:
 <ul>
  <li>given the components of a {@link FourVector} in one inertial frame K, find its components in a second frame K'
   ({@link #toNewFrame(FourVector))}, with inverse {@link #toNewFourVector(FourVector))}) 
  <li>given the components of a {@link FourVector} in one inertial frame K, find the components of a second 4-vector in the same frame K.
     ({@link #toNewFourVector(FourVector)}, with inverse {@link #toNewFrame(FourVector)}) 
 </ul>
 
 <P>
 Successive application of the two methods {@link #toNewFrame(FourVector)} and {@link #toNewFourVector(FourVector)} 
 (in any order) must return the original event (aside from some rounding that usually occurs because of 
 floating-point operations).
*/
public interface CoordTransform {
  
  /** 
   For a given 4-vector, transform its components from frame K to K'. 
   The inverse operation is {@link #toNewFourVector(FourVector)}.
   
   @param vec the compontents in the K frame.
   @return the components in the K' frame. 
  */
  FourVector toNewFrame(FourVector vec);
  
  /** 
   For a given frame K, transform the given 4-vector into another 4-vector. 
   The inverse operation is {@link #toNewFrame(FourVector)}.
   
   @param vec the components in a given frame K.
   @return the components of a second event in a given frame K. 
  */
  FourVector toNewFourVector(FourVector vec);

  /**
   Asserts that the magnitude-squared has not changed (very much).
   
   Convenience method for implementations to validate their result. 
   This will not apply to all coord transforms.  
   By default, assertions are turned off at runtime. 
  */
  static void sameIntervalFromOrigin(FourVector a, FourVector b) {
    assert isTiny(a.magnitudeSq() - b.magnitudeSq()) : "Magnitude-squared has changed too much";
  }
}
