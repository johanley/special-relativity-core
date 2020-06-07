package sr.core.transform;

import sr.core.Axis;
import sr.core.Physics;
import static sr.core.Util.mustBeSpatial;

/**
 Lorentz Transformation of a {@Vector4}.
 Note that a LorentzTransformation applies not just to an event, but to any 4-vector. 

<P>The geometry:
 <ul>
  <li>transforms from K to K'. K' is moving with respect to K along the given axis with speed β.
  <li>if the speed is positive (negative), then the relative motion is along the positive (negative) direction of the axis.
 </ul>
   
 @param spatialAxis the axis along which the K' frame is moving with respect to the K frame.
 @param β the velocity parallel to the given spatial axis; can be either sign.
*/
public final class Boost implements CoordTransform {

  /**
   Constructor.
   The geometry:
   <ul>
    <li>transforms from K to K'. K' is moving with respect to K along the given axis with speed β.
    <li>if the speed is positive (negative), then the relative motion is along the positive (negative) direction of the axis.
   </ul>
   
    @param spatialAxis the axis along which the K' frame is moving with respect to the K frame.
    @param β the velocity parallel to the given spatial axis; can be either sign.
  */
  public Boost(Axis spatialAxis, double β) {
    mustBeSpatial(spatialAxis);
    this.spatialAxis = spatialAxis;
    this.β = β;
  }

  /** Factory method. */
  public static Boost alongThe(Axis spatialAxis, double β) {
    return new Boost(spatialAxis, β);
  }

  /**
   Transform the 4-vector to the boosted inertial frame.  
   The inverse of {@link #toNewVector4(FourVector)}. 
  */
  @Override public FourVector toNewFrame(FourVector vec) {
    return boostIt(vec, WhichDirection.NOMINAL);
  }
  
  /** 
   Transform the 4-vector to a new 4-vector in the same inertial frame.  
   The inverse of {@link #toNewFrame(FourVector)}. 
  */
  @Override public FourVector toNewVector4(FourVector vecPrime) {
    return boostIt(vecPrime, WhichDirection.INVERSE);
  }
  
  @Override public String toString() {
    String sep = ",";
    return "[" + spatialAxis+sep+ β + "]";
  }
  
  public double β() {return β;}
  public Axis axis() {return spatialAxis;}
  
  // PRIVATE
  
  private Axis spatialAxis;
  private double β;

  private FourVector boostIt(FourVector v, WhichDirection dir) {
    FourVector result = boost(v, spatialAxis, β * dir.sign());
    CoordTransform.sameIntervalFromOrigin(v, result);
    return result;
  }
  
  private FourVector boost(FourVector v, Axis spatialAxis, double β) {
    EntangledPair pair = entangle(v.ct(), v.part(spatialAxis), β);
    FourVector result = v;
    result = result.put(Axis.CT, pair.time);
    result = result.put(spatialAxis, pair.space);
    return result;
  }
 
  private static class EntangledPair {
    double time;
    double space;
  }
  
  private EntangledPair entangle(double ct, double space, double β) {
    double Γ = Physics.Γ(β);
    EntangledPair result = new EntangledPair();
    result.time =     Γ*ct - Γ*β*space;
    result.space = -Γ*β*ct +   Γ*space;
    return result;
  }
}