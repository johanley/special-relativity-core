package sr.core;

/**
 Used for switching the direction of coord axes (space or time).
*/
public enum Parity {
  
  EVEN(1),
  
  ODD(-1);
  
  /** Plus for even, minus for odd. */
  public int sign() {
    return sign;
  }
  
  // PRIVATE
  
  private Parity(int sign) {
    this.sign = sign;
  }
  
  private int sign;

}
