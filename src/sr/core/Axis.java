package sr.core;

import java.util.ArrayList;
import java.util.List;

/** The axes in space-time. */
public enum Axis {
  
  CT(0), X(1), Y(2), Z(3);

  /** ct-x-y-z map to 0-1-2-3. */
  public int idx() { return idx; }
  
  /** Most operations involve the spatial axes. * */
  public boolean isSpatial() {
    return this != CT;
  }

  /**  
   The order of the returned list defines a right-hand sense of rotation about the given (spatial) axis, 
   taken as a pole of the rotation.
   <ul>
    <li>about the z-axis: x turns towards y.
    <li>about the y-axis: z turns towards x.
    <li>about the x-axis: y turns towards z.
   </ul>
  */
  public static List<Axis> rightHandRuleFor(Axis pole){
    List<Axis> result = new ArrayList<>();
    if (pole.isSpatial()) {
      if (X == pole) {
        result.add(Y);
        result.add(Z);
      }
      else if (Y == pole) {
        result.add(Z);
        result.add(X);
      }
      else if (Z == pole) {
        result.add(X);
        result.add(Y);
      }
    }
    return result;
  }
  
  // PRIVATE
  
  private Axis(int idx) {
    this.idx = idx;
  }
  
  private int idx;
}
