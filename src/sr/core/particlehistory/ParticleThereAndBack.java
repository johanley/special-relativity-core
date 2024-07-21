package sr.core.particlehistory;

import sr.core.Util;
import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.FourVector;
import sr.core.vector.Velocity;

import static sr.core.Axis.*;

/**
 History for a particle with mass moving uniformly from infinity to the frame's origin, then in the opposite direction back out to infinity.
 The two legs take place at the same speed.
 The origin event is the turnaround event.

 <P>The parameter for the history is the coordinate-time <em>ct</em>.
 Negative <em>ct</em> means before the turnaround, and positive <em>ct</em> is after the turnaround.
*/
public final class ParticleThereAndBack implements ParticleHistory {

  /**
   Constructor.
   
   The overall speed must be in the range (0,1).
   @param mass must be positive
   @param velocity before the turnaround event; the speed cannot be zero
  */
  public ParticleThereAndBack(double mass, Velocity velocity) {
    Util.mustHave(mass > 0, "Mass must be positive.");
    Util.mustHave(velocity.magnitude() > 0, "Speed cannot be zero.");
    fourMomentum = velocity.fourMomentumFor(mass); //c=1 in this project
  }
  
  /** For a particle having unit mass. */
  public ParticleThereAndBack(Velocity velocity) {
    this(1.0, velocity);
  }
  
  /** @param ct is the coordinate-time. */
  @Override public FourVector event(double ct) {
    FourVector displacement = FourVector.from(ct, ct*velocity.on(X), ct*velocity.on(Y), ct*velocity.on(Z), ApplyDisplaceOp.NO);
    return ct >= 0 ? turnaroundEvent.plus(displacement) : turnaroundEvent.plus(displacement.spatialReflection());
  }
  
  /** @param ct is the coordinate-time. */
  @Override public FourVector fourMomentum(double ct) {
    return ct <= 0 ? fourMomentum : fourMomentum.spatialReflection();
  }
  
  /** 
   The zero of proper-time is taken as the event with ct = 0.
   @param ct is the coordinate-time.
  */
  @Override public double τ(double ct) {
    return ct / velocity.Γ(); 
  }

  private FourVector turnaroundEvent = FourVector.ZERO_AFFINE; //the origin event
  private Velocity velocity;
  private FourVector fourMomentum;
}
