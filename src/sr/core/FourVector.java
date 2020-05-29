package sr.core;

/**
 Identify the 4 parts of a 4-vector that transform according to the Lorentz Transformation.
 
 4-vectors contain different kinds of data, with different units (always implemented as doubles).
 But the Lorentz Transformation of all 4-vectors is always exactly the same.
*/
public interface FourVector<T> {
  
  /** 
   Method-references to the 4 object methods that return 4 components (doubles), 
   in the conventional order t-x-y-z (time component first, then the 3 space components, in order x-y-z).

   <P>Example: 
   <pre>
    public VectorPart[] parts() {
      VectorPart[] result = {this::ct, this::x, this::y, this::z}; 
      return result;
    }
   </pre>
   
   <P>As usual with method-references, it's not the method name that's important, it's the 
   args and return type. In this case, all that matters is that the methods have 
   no args, and return a double. 
   
   <P>Design note: the alternate design of using 4 conventional method names (zero, one, two, three)
   would not allow each 4-vector to use more natural or descriptive names.
   
   <P>Design note: Supplier&lt;Double&gt; is not used in this design, because it doesn't play well with array literals.
  */
  VectorPart[] parts();
  
}
