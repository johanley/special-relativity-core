package sr.core;

/** 
 Build a 4-vector out of its 4 components (doubles). 
*/
public interface VectorBuilder<T> {
  
  T from(Double zero, Double one, Double two, Double three);

}
