package sr.core;

/** 
 Build a 4-vector out of its 4 components (doubles). 
*/
@FunctionalInterface
public interface VectorBuilder<T> {
  
  /**
   How to build the 4-vector from 4 doubles. The sequence is t-x-y-z.
  
  <P>This method exists because objects are immutable. 
   During a Lorentz transformation, the state of the object isn't changed in place. 
   Instead, an entirely new object is created.
   
   <P>(The policy for building a new object during a Lorentz Transformation is 
   particular to the design of this library; it's not sufficiently important to
   be included in the {@link FourVector} interface itself.)  
  */
  T from(Double zero, Double one, Double two, Double three);

}
