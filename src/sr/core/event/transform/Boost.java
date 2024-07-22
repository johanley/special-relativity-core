package sr.core.event.transform;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.Util;
import sr.core.event.Event;

import static sr.core.Util.mustBeSpatial;

/**
 Lorentz Transformation of an {@link Event}, along one of the spatial axes.

<P>The geometry:
 <ul>
  <li>transforms from K to K'. K' is moving with respect to K along the given axis with speed β.
  <li>if the speed is positive (negative), then the relative motion is along the positive (negative) direction of the axis.
 </ul>
   
 @param spatialAxis the axis along which the K' frame is moving with respect to the K frame.
 @param β the velocity parallel to the given spatial axis; can be either sign.
*/
public final class Boost implements Transform {

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
   Transform the event to the boosted inertial frame.  
   The inverse of {@link #reverse(Event)}. 
  */
  @Override public Event apply(Event vec) {
    return boostIt(vec, +1);
  }
  
  /** 
   Transform the event to a new event in the same inertial frame.  
   The inverse of {@link #apply(Event)}. 
  */
  @Override public Event reverse(Event vecPrime) {
    return boostIt(vecPrime, -1);
  }
  
  @Override public String toString() {
    String sep = ",";
    return "boost[" + spatialAxis+sep+ Util.round(β,5) + "]";
  }
  
  public double β() {return β;}
  public Axis axis() {return spatialAxis;}
  
  // PRIVATE
  
  private Axis spatialAxis;
  private double β;

  private Event boostIt(Event v, int sign) {
    Event result = boost(v, spatialAxis, β * sign);
    Transform.sameIntervalFromOrigin(v, result);
    return result;
  }
  
  private Event boost(Event v, Axis spatialAxis, double β) {
    EntangledPair pair = entangle(v.ct(), v.on(spatialAxis), β);
    Event result = v;
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