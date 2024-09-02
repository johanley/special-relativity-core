package sr.core;


import java.util.ArrayList;
import java.util.List;

/** The axes in space-time. */
public enum Axis {
  
  CT(0), 
  X(1), 
  Y(2), 
  Z(3);

  /** ct-x-y-z maps to 0-1-2-3. */
  public int idx() { return idx; }
  
  /** Most operations involve the spatial axes. * */
  public boolean isSpatial() {
    return this != CT;
  }
  
  /** Without the ct-axis. */
  public static List<Axis> spatialAxes(){
    List<Axis> result = new ArrayList<>();
    result.add(X);
    result.add(Y);
    result.add(Z);
    return result;
  }

  /**  
   The order of the returned list defines a right-hand sense of rotation about the given (spatial) axis, 
   taken as a pole of the rotation.
   Where the integers represent indices in the returned list, you have:
   <ul>
    <li>about the z-axis: x(0) turns towards y(1).
    <li>about the y-axis: z(0) turns towards x(1).
    <li>about the x-axis: y(0) turns towards z(1).
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
