package sr.core.vector4.transform;

import sr.core.vector4.Builder;
import sr.core.vector4.FourVector;

/**
 Map one four-vector to another four-vector, and its inverse operation.

 <P> 
 For every transformation, there must be a corresponding inverse transformation, which will recover the original event.
 That's why two methods are defined for this interface.
 Most, but not all, inverse operations involve the change of sign of some quantity.
 
 <P><b>There are 2 opposite use cases here</b>:
 <ul>
  <li>given the components of a {@link FourVector} in one inertial grid K, find its components in a second grid K'
   ({@link #changeGrid(FourVector)}, with inverse {@link #changeVector(FourVector)}) 
  <li>given the components of a {@link FourVector} in one inertial grid K, find the components of a second four-vector in the same grid K
     ({@link #changeVector(FourVector)}, with inverse {@link #changeGrid(FourVector)}).
 </ul>
 
 <P>
 Successive application of the two methods {@link #changeGrid(FourVector)} and {@link #changeVector(FourVector)} 
 (in any order) must return the original four-vector (aside from some rounding that usually occurs because of 
 floating-point operations).
 
 <p>Design note: attaching the type information to the methods, and not the interface, means 
 I can retain the style of using factory methods. It also means that a given implementation 
 can handle different types of four-vectors, without being married to one specific type.
*/
public interface Transform {
  
  /** 
   For a given four-vector represented relative to K, represent the same four-vector relative to K'. 
    
   The inverse operation is {@link #changeVector(FourVector)}.
   
   @param fourVector the components of a four-vector in the K grid.
   @return the components of the same four-vector in the K' grid. 
  */
  <T extends FourVector & Builder<T>> T changeGrid(T fourVector);
  
  /** 
   For a given four-vector represented relative to K, return a second four-vector represented relative to K. 
    
   The inverse operation is {@link #changeGrid(T)}.
   
   @param fourVector the components in a given grid K.
   @return the components of a second four-vector in a given grid K. 
  */
  <T extends FourVector & Builder<T>> T changeVector(T fourVector);

}
