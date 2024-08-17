package sr.core.vector3.transform;

import sr.core.vector3.ThreeVector;
import sr.core.vector4.Event;

/**
 Map one 3-vector into another, and its inverse operation.
 
 <P>A transformation can be applied in <a href='https://en.wikipedia.org/wiki/Active_and_passive_transformation'>two complementary senses</a>: 
 <ul>
  <li>one frame of reference, two different vectors (active transformation)
  <li>one vector, two different frames of reference (passive transformation)
 </ul>
 You must be aware of which sense is needed in a given context, or errors will result.

 <P> 
 For every transformation, there must be a corresponding inverse transformation, which will recover the original 3-vector.
 That's why two methods are defined for this interface.
 Most, but not all, inverse operations involve the change of sign of some quantity.
 
 <P>
 Successive application of the two methods {@link #changeFrame(ThreeVector)} and {@link #changeVector(ThreeVector)} 
 (in any order) must return the original 3-vector (aside from some rounding that usually occurs because of 
 floating-point operations).
*/
public interface SpatialTransform {
  
  /** 
   Return the same vector expressed in a different coordinate system.
    
   The inverse of this operation is {@link #changeVector(Event)}.
  */
  public ThreeVector changeFrame(ThreeVector v);
  
  /** 
   Return a new vector in the same coordinate system.
   
   The inverse of this operation is {@link #changeFrame(Event)}.
  */
  public ThreeVector changeVector(ThreeVector v);
}
