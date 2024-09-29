package sr.core.component.ops;

/** 
 The sense in which a transformation takes place.
 Most transformations can take place in two opposite senses.
 Switching sense usually corresponds to simply negating an input of the transformation.
 
 <P>This enum exists in order to make the calling code more clear.
 It merely provides aliases for the numbers +1 and -1.

 <P>Some operations don't need to be told the sense, because they are the same forwards and backwards (parity, time-reversal).
 In those cases, the caller should use {@link #NotNeeded}.

 Aliases for +1:
 <ul>
  <li>Plus
  <li>Active
  <li>Primed
  <li>ChangeComponents
  <li>Forward
 </ul>
 
 Aliases for -1:
 <ul>
  <li>Minus
  <li>Passive
  <li>Unprimed
  <li>ChangeGrid
  <li>Reverse
 </ul>
*/
public enum NSense {
  
  Active(1),
  Passive(-1),
  
  Primed(1), 
  Unprimed(-1),
  
  ChangeGrid(1),
  ChangeComponents(-1), 
  
  Plus(1),
  Minus(-1),
  
  Forward(1),
  Reverse(-1), 

  /** Some operations don't need to be told the sense, because they are the same forwards and backwards (parity, time-reversal). */
  NotNeeded(0);
  
  public int sign() { return sign; }
  
  private int sign;
  private NSense(int sign) {
    this.sign = sign;
  }
}
