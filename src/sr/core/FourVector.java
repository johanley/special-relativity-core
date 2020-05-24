package sr.core;

/**
 4-vectors: their parts, and how to build the 4-vector from the parts.
 
 4-vectors contain different kinds of data, with different units (always implemented as doubles).
 But the Lorentz transformation of all 4-vectors is always exactly the same.
*/
public interface FourVector<T> {
  
  /** 
   How to build the 4-vector from 4 doubles. The sequence is t-x-y-z.
   Small defect: classes usually use static factory methods, but this method isn't static.
   So, implementations of this method will usually simply forward calls to the corresponding 
   static method.
   
   <P>This method exists because objects are immutable. 
   During a Lorentz transformation, the state of the object isn't changed in place. 
   Instead, an entirely new object is created.  
  */
  VectorBuilder<T> buildit();
  
  /** 
   Method-references to the 4 object methods that return 4 components (doubles), 
   in the conventional order t-x-y-z (time component first, then the 3 space components, in order x-y-z).
  
   As usual with method-references, it's not the method name that's important, it's the 
   args and return type. In this case, all that matters is that the methods have 
   no args, and return a double. (Supplier&lt;Double&gt; is not used, because it doesn't 
   play well with array literals.)
   
   Design note: the alternate design of using 4 conventional method names (zero, one, two, three)
   would not allow each 4-vector to use more natural or descriptive names.
  */
  VectorPart[] parts();
  
}
