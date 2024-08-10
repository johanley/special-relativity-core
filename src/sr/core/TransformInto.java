package sr.core;

/** 
 The sense or direction of a transformation.
 Almost all transformations come in two flavours, distinguished by the sign of some quantity. 
*/
public enum TransformInto {
  
  /** A transform which returns quantities attached to the K' frame of reference. */
  PRIMED(+1),
  
  /** A transform which returns quantities attached to the K frame of reference. */
  UNPRIMED(-1);
  
  public static TransformInto from(int sign) {
    TransformInto result = null;
    for(TransformInto tr : values()) {
      if (tr.sign() == sign) {
        result = tr;
        break;
      }
    }
    if (result == null) {
      throw new IllegalArgumentException("Invalid sign: " + sign);
    }
    return result;
  }
  
  public int sign() {
    return sign;
  }
  
  private int sign;
  private TransformInto(int sign) {
    this.sign = sign;
  }
}
