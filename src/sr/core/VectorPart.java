package sr.core;

/**
 The methods of an object that represent the components of a 4-vector.
 
 Supplier&lt;Double&gt; is not used here, because it leads to less compact code 
 in implementations (it doesn't play well with array literals).  
*/
@FunctionalInterface
public interface VectorPart {
  Double val();
}
