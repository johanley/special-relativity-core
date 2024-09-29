package sr.core.hist.timelike;

import sr.core.component.NEvent;
import sr.core.component.NPosition;
import sr.core.vec3.NAxisAngle;
import sr.core.vec3.NVelocity;
import sr.core.vec4.NFourDelta;

/**
 History for a particle with mass moving uniformly at a given speed, and in a given direction.
 The general character of this history: 
 
  <pre>
            CT
            ^
            |       *
            |      *
            |     *
 ----------------*------------&gt; X
            |   *
            |  * 
            | *
 </pre>
 <p>If the speed is 0, then this history corresponds to a stationary object.
*/
public final class NUniformVelocity extends NTimelikeMoveableHistory {

  /**
   Factory method.
   The overall speed must be in the range [0,1).
   @param deltaBase of the history, relative to which this history acts.
   @param velocity of the object, possibly 0 (for a stationary object).
  */
  public static NUniformVelocity of(NTimelikeDeltaBase deltaBase, NVelocity velocity) {
    return new NUniformVelocity(deltaBase, velocity);
  }

  /**
   Factory method.
   The overall speed must in the range [0,1).
   @param initialPosition for the object at ct=0, with proper-time zero at that time as well.
   @param velocity of the object, possibly 0 (for a stationary object).
  */
  public static NUniformVelocity of(NPosition initialPosition, NVelocity velocity) {
    return new NUniformVelocity(initialPosition, velocity);
  }
  
  /**
   Factory method for a stationary object.
   @param deltaBase of the history, relative to which this history acts.
  */
  public static NUniformVelocity stationary(NTimelikeDeltaBase deltaBase) {
    return new NUniformVelocity(deltaBase, NVelocity.zero());
  }
  
  /**
   Factory method for a stationary object.
   @param initialPosition for the object at ct=0, with proper time zero at that time as well.
  */
  public static NUniformVelocity stationary(NPosition initialPosition) {
    return new NUniformVelocity(NTimelikeDeltaBase.of(initialPosition), NVelocity.zero());
  }
  
  @Override protected NFourDelta delta(double Δct) {
    NEvent b = NEvent.of(Δct, Δct*velocity.x(), Δct*velocity.y(), Δct*velocity.z());
    return NFourDelta.withRespectToOrigin(b);
  }
  
  @Override protected double Δct(double Δτ) {
    return Δτ * velocity.Γ();
  }
  
  @Override protected double Δτ(double Δct) {
    return Δct / velocity.Γ();
  }
  
  @Override public NAxisAngle rotation(double Δct) {
    return NAxisAngle.zero();
  }
  
  @Override public String toString() {
    return "UniformVelocity " + timelikeDeltaBase() + " velocity:" + velocity;
  }
  
  private NVelocity velocity;

  private NUniformVelocity(NPosition initialPosition, NVelocity velocity) {
    this(NTimelikeDeltaBase.of(initialPosition), velocity);
  }
  
  private NUniformVelocity(NTimelikeDeltaBase deltaBase, NVelocity velocity) {
    super(deltaBase);
    this.velocity = velocity;
  }
}