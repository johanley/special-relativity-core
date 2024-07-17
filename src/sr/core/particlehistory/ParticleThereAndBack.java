package sr.core.particlehistory;

import sr.core.Physics;
import sr.core.Util;
import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.FourVector;

/**
 History for a particle with mass moving uniformly from infinity to the frame's origin, then in the opposite direction back out to infinity.
 The two legs take place at the same speed.
 The origin event is the turnaround event.

 <P>In this case, the parameter for the history is identified with the proper time cτ. 
 The zero of proper time is taken as ct=0.
*/
public final class ParticleThereAndBack implements ParticleHistory {

  /**
  Constructor.
   
  The overall speed must be in the range (0,1).
  @param mass must be positive
  @param βx speed in the range (-1,1).
  @param βy speed in the range (-1,1).
  @param βz speed in in the range (-1,1).
 */
  public ParticleThereAndBack(double mass, double βx, double βy, double βz) {
    Util.mustHave(mass > 0, "Mass must be positive.");
    checkSpeeds(βx,βy,βz);
    
    this.βx = βx;
    this.βy = βy;
    this.βz = βz;
    double β = Util.sqroot(
      Util.sq(βx) + Util.sq(βy) + Util.sq(βz)
    );
    checkSpeeds(β);
    Util.mustHave(β > 0, "Overall speed cannot be 0.");
    this.Γ = Physics.Γ(β);
    
    FourVector fourVelocity = FourVector.from(Γ, Γ*βx, Γ*βy, Γ*βz, ApplyDisplaceOp.NO);
    fourMomentum = fourVelocity.multiply(mass); //c=1 in this project
  }
  
  @Override public FourVector event(double cτ) {
    double ct = ct(cτ);
    FourVector displacement = FourVector.from(ct, ct*βx, ct*βy, ct*βz, ApplyDisplaceOp.NO);
    return ct < 0 ? turnaroundEvent.plus(displacement) : turnaroundEvent.plus(displacement.spatialReflection());
  }
  
  @Override public FourVector fourMomentum(double cτ) {
    return cτ <= 0 ? fourMomentum : fourMomentum.spatialReflection();
  }

  private double Γ;
  private FourVector turnaroundEvent = FourVector.ZERO_AFFINE; //the origin event
  private FourVector fourMomentum;
  private double βx;
  private double βy;
  private double βz;

  /** Time dilation. Convert from proper time to coordinate time. */
  private double ct(double cτ) {
    return Γ * cτ;
  }

  private void checkSpeeds(Double... βs) {
    for (Double βi : βs) {
      Util.mustHaveSpeedRange(βi);
    }
  }
}
