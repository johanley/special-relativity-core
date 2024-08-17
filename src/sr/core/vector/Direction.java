package sr.core.vector;

import sr.core.Axis;

/** 
 A direction in space as a unit vector.
 <P>This class was created for modeling the wave-vector <em>k<sup>i</sup></em> for light.
*/
public final class Direction extends ThreeVectorImpl {

  /** 
   Factory method, taking the 3 components of the direction along the XYZ axes, in that order.
   The three components are allowed to be any vector, including the zero-vector.
   This method will use those three numbers to create a corresponding unit-vector, if needed.  
  */
  public static Direction of(double x, double y, double z) {
    ThreeVector result = ThreeVectorImpl.of(x, y, z);
    if (result.magnitude() > 0) {
      result = result.unitVector();
    }
    return new Direction(result.x(), result.y(), result.z());
  }
  
  public static Direction of(ThreeVector v) {
    return Direction.of(v.x(), v.y(), v.z());
  }

  /** Factory method. The vector has 1 non-zero component, along the given spatial coordinate axis. */
  public static Direction of(Axis axis) {
    return new Direction(axis, 1);
  }
  
  private Direction(double x, double y, double z) {
    super(x, y, z);
  }
  
  private Direction(Axis axis, double value) {
    super(axis, value);
  }

}