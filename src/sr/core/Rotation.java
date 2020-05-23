package sr.core;

import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;
import static java.lang.Math.*;

/** 
 Rotate about one of the spatial axes.
 
 With spatial rotations, you need to be careful with the exact meaning of the operation, because there are 
 so many variations.
 
 <P>This class rotates about the given axis only.
 A general rotation about an arbitrary direction can be constructed in steps, using objects of this class in 
 the proper sequence. As usual, the sequence of operations usually matters when rotations are concerned.
 In this case, each successive rotation will be about the NEW axis, using the right-hand rule.
*/
public final class Rotation {

  /**
   For example, a rotation about the z-axis will follow the right-hand rule. 
   For positive θ, the x-axis will be rotated towards the y-axis.
   
   <P>Right-hand rules give the sense of rotation:
   <ul>
    <li>about the z-axis: x turns towards y.
    <li>about the y-axis: z turns towards x.
    <li>about the x-axis: y turns towards z.
   </ul>
  */
  public static Rotation along(Axis axis, double θ) {
    return new Rotation(axis, θ);
  }
  
  /** Apply the rotation to the given event. */
  public Event applyTo(Event e) {
    Event res = null;
    //there's a small bit of code repetition here, but it's not too bad
    if (Z == axis) {
      EntangledPair pair = entangle(e.x(), e.y()); //turn x towards y
      res = Event.from(e.ct(), pair.a, pair.b, e.z());
    }
    else if (Y == axis) {
      EntangledPair pair = entangle(e.z(), e.x()); //turn z towards x
      res = Event.from(e.ct(), pair.a, e.y(), pair.b);
    }
    else if (X == axis) {
      EntangledPair pair = entangle(e.y(), e.z()); //turn y towards z
      res = Event.from(e.ct(), e.x(), pair.a, pair.b);
    }
    return res;
  }
  
  //PRIVATE
  
  private Axis axis;
  private double θ;
  
  private Rotation(Axis axis, double θ) {
    this.axis = axis;
    this.θ = θ;
  }
  
  private EntangledPair entangle(double a, double b) {
    EntangledPair result = new EntangledPair();
    result.a = a*   cos(θ)  + b*sin(θ);
    result.b = a* (-sin(θ)) + b*cos(θ);
    return result;
  }

  /** This was created to remove code repetition. */
  private static final class EntangledPair {
    double a;
    double b;
  }
}
