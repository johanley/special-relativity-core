package sr.core;

/** 
 A rotation about a spatial axis (as a struct).
 The sense of the rotation is defined by {@link Axis#rightHandRuleFor(Axis pole)}.
 
 <P>Such a combination is sometimes referred to as an <em>axis-angle variable</em>. 
*/
public final class Rotation {
  
  public static Rotation from(Axis axis, double θ) {
    Util.mustHave(axis.isSpatial(), "Rotational axis must be spatial.");
    Rotation result = new Rotation();
    result.axis = axis;
    result.θ = θ;
    return result;
  }
  
  /** 
   The axis or pole of the rotation. 
   The sense of the rotation is defined by {@link Axis#rightHandRuleFor(Axis pole)}.
  */
  public Axis axis;
  
  /** 
   The angle in radians in which to rotate about the axis.
   A negative angle reverses the sense of the rotation. 
  */
  public double θ;

}
