package sr.core.transform;

import static sr.core.Axis.CT;
import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;

import java.util.ArrayList;
import java.util.List;

import sr.core.Axis;
import sr.core.Physics;

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
    if (! spatialAxis.isSpatial()) {
      throw new RuntimeException("Not allowed to boost along the ct axis. Use a spatial axis please.");
    }
    this.spatialAxis = spatialAxis;
    this.β = β;
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

  private FourVector boostIt(FourVector vec, WhichDirection dir) {
    FourVector result = boost(vec, spatialAxis, β * dir.sign());
    CoordTransform.sameIntervalFromOrigin(vec, result);
    return result;
  }
  
  private FourVector boost(FourVector vec, Axis spatialAxis, double β) {
    if (!spatialAxis.isSpatial()) {
      throw new IllegalArgumentException("Axis cannot be the time axis.");
    }
    int space = spatialAxis.idx();
    EntangledPair pair = entangle(vec.ct(), part(vec, space), β);
    List<Double> parts = new ArrayList<>();
    parts.add(pair.time);
    for (Axis a : Axis.values()) {
      if (spatialAxis.idx() == a.idx()) {
        parts.add(pair.space);
      }
      else if (a.idx() > CT.idx()){
        parts.add(part(vec, a.idx()));
      }
    }
    return FourVector.from(parts.get(CT.idx()), parts.get(X.idx()), parts.get(Y.idx()), parts.get(Z.idx()));
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
  
  private double part(FourVector v, int axisIdx) {
    Double[] parts = {v.ct(), v.x(), v.y(), v.z()};
    return parts[axisIdx];
  }
}