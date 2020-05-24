package sr.core;

//@FunctionalInterface
public interface Vector<T> {
  
  /** The object methods that return the 4 components in sequence t-x-y-z. */
  VectorPart[] vectorParts();
  
  /** 
   How to build the 4-vector from its parts. The sequence is t-x-y-z.
   Small defect: classes usually have a static factory methods, but this method isn't static. 
  */
  T build(Double one, Double two, Double three, Double four);
    
}
