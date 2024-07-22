package sr.core.event.transform;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;
import static sr.core.Util.mustBeSpatial;

import java.util.List;

import sr.core.Axis;
import sr.core.Util;
import sr.core.event.Event;

/** 
 Rotate about one of the spatial axes.

 With spatial rotations, you need to be careful with the exact meaning of the operation, 
 because there are so many variations.

 <P>This class rotates about the given axis only.
 A general rotation about an arbitrary direction can be constructed in steps, using objects of this class in 
 the proper sequence. As usual, the sequence of operations usually matters when rotations are concerned.
 In this case, each successive rotation will be about the NEW axis, using the right-hand rule 
 to define the meaning of the operation for positive θ.
 
 <P>Right-hand rules defined by {@link Axis#rightHandRuleFor(Axis)} give the sense of rotation for positive angle θ  
 (for negative θ, the sense of rotation is simply reversed).
*/
public final class Rotate implements Transform {
  
  /** 
   Constructor.
   @param spatialAxis axis about which to rotate using a right-hand rule (see class comment)
   @param θ angle in radians to rotate about the spatial axis, with the right-hand rule (see class comment)
  */
  public Rotate(Axis spatialAxis, double θ) {
    mustBeSpatial(spatialAxis);
    this.spatialAxis = spatialAxis;
    this.θ = θ;
  }

  /** Construct with a factory method. */
  public static Rotate about(Axis spatialAxis, double θ) {
    return new Rotate(spatialAxis, θ);
  }
  
  @Override public Event apply(Event v) {
    return doIt(v, +1);
  }
  
  @Override public Event reverse(Event vPrime) {
    return doIt(vPrime, -1);
  }
  
  public double θ() { return θ; }
  public Axis axis() { return spatialAxis; }
  
  @Override public String toString() {
    String sep = ",";
    return "rotate[" 
      + spatialAxis + sep + 
      Util.round(θ,5) + 
    "]";
  }
  
  // PRIVATE
  
  private Axis spatialAxis;
  private double θ;

  private Event doIt(Event v, int sign) {
    EntangledPair entangled = null;
    if (Z == spatialAxis) {
      entangled = entangle(v.x(), v.y(), sign); //order is important! 
    }
    else if (Y == spatialAxis) {
      entangled = entangle(v.z(), v.x(), sign); 
    }
    else if (X == spatialAxis) {
      entangled = entangle(v.y(), v.z(), sign); 
    }
    List<Axis> axes = Axis.rightHandRuleFor(spatialAxis);
    Event result = Event.of(v.ct(), v.x(), v.y(), v.z()); //starting point
    result = result.put(axes.get(0), entangled.a);
    result = result.put(axes.get(1), entangled.b);
    Transform.sameIntervalFromOrigin(v, result);
    return result;
  }
  
  private EntangledPair entangle(double a, double b, int sign) {
    EntangledPair result = new EntangledPair();
    double η = sign * θ;
    result.a = a*   cos(η)  + b*sin(η);
    result.b = a* (-sin(η)) + b*cos(η);
    return result;
  }

  /** This was created to remove code repetition. */
  private static final class EntangledPair {
    double a;
    double b;
  }
}
