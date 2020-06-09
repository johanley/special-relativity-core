package sr.core.transform;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;
import static sr.core.Util.mustBeSpatial;

import java.util.List;

import sr.core.Axis;

/** 
 Rotate about one of the spatial axes.

 With spatial rotations, you need to be careful with the exact meaning of the operation, because there are 
 so many variations.

 <P>This class rotates about the given axis only.
 A general rotation about an arbitrary direction can be constructed in steps, using objects of this class in 
 the proper sequence. As usual, the sequence of operations usually matters when rotations are concerned.
 In this case, each successive rotation will be about the NEW axis, using the right-hand rule 
 to define the meaning of the operation for positive θ.
 
 <P>Right-hand rules defined by {@link Axis#rightHandRuleFor(Axis)} give the sense of rotation for positive angle θ  (for negative θ, the sense of 
 rotation is simply reversed).
*/
public final class Rotate implements CoordTransform {
  
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
  
  @Override public FourVector toNewFrame(FourVector v) {
    return doIt(v, WhichDirection.NOMINAL);
  }
  
  @Override public FourVector toNewFourVector(FourVector vPrime) {
    return doIt(vPrime, WhichDirection.INVERSE);
  }
  
  public double θ() { return θ; }
  public Axis axis() { return spatialAxis; }
  
  @Override public String toString() {
    String sep = ",";
    return "rotate[" + spatialAxis+sep+ θ + "]";
  }
  
  // PRIVATE
  
  private Axis spatialAxis;
  private double θ;

  private FourVector doIt(FourVector v, WhichDirection dir) {
    int sign = dir.sign();
    //there's a small bit of code repetition here, but it's not too bad
    /*
    FourVector result = null;
    if (Z == spatialAxis) {
      EntangledPair pair = entangle(v.x(), v.y(), sign); 
      result = FourVector.from(v.ct(), pair.a, pair.b, v.z(), v.applyDisplaceOp());
    }
    else if (Y == spatialAxis) {
      EntangledPair pair = entangle(v.z(), v.x(), sign); 
      result = FourVector.from(v.ct(), pair.b, v.y(), pair.a, v.applyDisplaceOp());
    }
    else if (X == spatialAxis) {
      EntangledPair pair = entangle(v.y(), v.z(), sign); 
      result = FourVector.from(v.ct(), v.x(), pair.a, pair.b, v.applyDisplaceOp());
    }
    */
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
    FourVector result = FourVector.from(v.ct(), v.x(), v.y(), v.z(), v.applyDisplaceOp()); //starting point
    result = result.put(axes.get(0), entangled.a);
    result = result.put(axes.get(1), entangled.b);
    CoordTransform.sameIntervalFromOrigin(v, result);
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
