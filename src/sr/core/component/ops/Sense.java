package sr.core.component.ops;

/** 
 The sense in which a transformation takes place.
 Most transformations can take place in two opposite senses.
 Switching sense usually corresponds to simply negating an input of the transformation.
 
 <P>This enum exists in order to make the calling code more clear.
 It merely provides aliases for the numbers +1 and -1.

 <P>Aliases for +1:
 <ul>
  <li>Active
  <li>ChangeComponents
  <li>Unprimed
  <li>Plus
  <li>Forward
 </ul>
 
 <P>Aliases for -1:
 <ul>
  <li>Passive
  <li>ChangeGrid
  <li>Primed
  <li>Minus
  <li>Reverse
 </ul>
*/
public enum Sense {
  
  Active(1),
  Passive(-1),
  
  ChangeComponents(1), 
  ChangeGrid(-1),
  
  Unprimed(1),
  Primed(-1), 
  
  Plus(1),
  Minus(-1),
  
  Forward(1),
  Reverse(-1); 
  
  public int sign() { return sign; }
  
  private int sign;
  private Sense(int sign) {
    this.sign = sign;
  }
}
