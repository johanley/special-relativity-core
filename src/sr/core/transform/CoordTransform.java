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
  <li>given the components of a {@link Vector4} in one inertial frame K, find its components in a second frame K'
   ({@link #toNewFrame(FourVector))}, with inverse {@link #toNewVector4(FourVector))}) 
  <li>given the components of a {@link Vector4} in one inertial frame K, find the components of a second 4-vector in the same frame K.
     ({@link #toNewVector4(Vector4)}, with inverse {@link #toNewFrame(Vector4)}) 
 </ul>
 
 <P>
 Successive application of the two methods {@link #toNewFrame(Vector4)} and {@link #toNewVector4(Vector4)} 
 (in any order) must return the original event (aside from some rounding that usually occurs because of 
 floating-point operations).
*/
public interface CoordTransform {
  
  /** 
   For a given 4-vector, transform its components from frame K to K'. 
   The inverse operation is {@link #toNewVector4(Vector4)}.
   
   @param vec the compontents in the K frame.
   @return the components in the K' frame. 
  */
  FourVector toNewFrame(FourVector vec);
  
  /** 
   For a given frame K, transform the given 4-vector into another 4-vector. 
   The inverse operation is {@link #toNewFrame(Vector4)}.
   
   @param vec the components in a given frame K.
   @return the components of a second event in a given frame K. 
  */
  FourVector toNewVector4(FourVector vec);

  /**
   Whether or not this transformation will change {@link FourVector#ZERO} into some other 4-vector.
   Most operations leave the origin unchanged. 
   The <em>only</em> oddball is the {@link Displace} operation; it doesn't apply to a 4-velocity, 
   or any other differential 4-vector.
   <P>This asymmetry is captured in this method, which returns false by default. 
  */
  default boolean changesOrigin() {
    return false;
  }
  
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
