package sr.core.transform;

/** 
 The direction of a {@link CoordTransform}.
 
 Each transform operation has a corresponding inverse operation, 
 usually differing by the <em>sign</em> of some quantity. 
*/
public enum WhichDirection {

  /** The forward (+) direction. */
  NOMINAL(1),
  
  /** The reverse (-) direction. */
  INVERSE(-1);

  /** Returns +1 or -1. */
  public int sign(){ return direction; } 
  
  // PRIVATE
  
  private WhichDirection(int direction) {
    this.direction = direction;
  }
  
  private int direction;
  
}
