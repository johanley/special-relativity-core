package sr.core.history;

/** How a history is parameterized. */
public enum LambdaParam {
  
  COORDINATE_TIME,
  
  /** 
   There's more than one way of defining proper time, 
   because the zero can be chosen arbitrarily. 
   Hence the 'a' prefix here. 
  */
  A_PROPER_TIME;

}
