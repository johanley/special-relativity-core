package sr.core.vector4;

import static sr.core.Axis.CT;
import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;

import java.util.Map;

import sr.core.Axis;
import sr.core.vector3.Direction;
import sr.core.vector3.PhaseGradient;
import sr.core.vector3.Velocity;

/**
 Four phase-gradient <em>k<sup>i</sup></em> for a plane monochromatic wave of any speed.
 Here, 'gradient' includes a time component.
 
 <P>Core data:
  <ul>
   <li>velocity of the wave <b>u</b> (its phase velocity)
   <li>the spatial phase gradient number <b>k</b> as a vector (the change in phase per unit distance). 
   The spatial phase-gradient is always perpendicular to the wavefront.
  </ul>

  <P>Important to note: <b>The direction of <em>k</em> is not necessarily the same as the direction of <em>u</em>.</b>
  For a plane wave moving in a stationary medium, the two vectors are indeed parallel.
  But when you boost to another frame moving relative to the stationary medium, then they are usually not parallel.

  <P>The angular frequency ω of the wave equals <em>k*u</em>.
  
  <P>This implementation is more general than the typical model you find in textbooks. 
  Here, the speed of the wave is allowed to be less than the speed limit.
  Thus, this class can be used as a simple model of light in a medium, or of sound in a medium.
  
  <P>Reference: <a href='https://arxiv.org/pdf/0801.3149v2'>article</a> of Aleksandar Gjurchinovski (2008) for the formula for the 
  phase-gradient in the more general case.
*/
public final class FourPhaseGradientSlow extends FourVector implements Builder<FourPhaseGradientSlow> {
  
  /**
   Factory method.
   @param u phase velocity
   @param k spatial phase-gradient
  */
  public static FourPhaseGradientSlow of(Velocity u, PhaseGradient k) {
    return new FourPhaseGradientSlow(u, k);
  }
  
  /** Unit vector in the direction of the spatial phase-gradient. */
  public Direction direction() {  
    return Direction.of(k);  
  }

  /** The phase velocity. FAILS (null) if this object resulted from a calculation! */
  public Velocity u() { 
    return u; 
  }

  /** The spatial phase-gradient. */
  public PhaseGradient k() {
    return k;
  }
  
  @Override public FourPhaseGradientSlow build(Map<Axis, Double> components) {
    //I can't fully 'reverse engineer' the data!!
    //k is ok, but u is not
    //the ct-component is k.u
    //k.u and k gives only the projection u.cos(θ), not the full vector!
    //I can't assume a speed or a direction
    //4 pieces of data can't let me make 6 pieces of data
    return new FourPhaseGradientSlow(
      components.get(CT),
      components.get(X),
      components.get(Y),
      components.get(Z)
    );
  }
 
  private Velocity u;
  private PhaseGradient k;

  /** All construction must pass through here. */
  private FourPhaseGradientSlow(Velocity u, PhaseGradient k) {
    this.u = u;
    this.k = k;
    //see: https://arxiv.org/pdf/0801.3149v2
    this.components.put(CT, this.k.dot(u)); //divided by c=1
    this.components.put(X, k.x());
    this.components.put(Y, k.y());
    this.components.put(Z, k.z());
  }
 
  /** DEFECT. THIS DOESN'T SET THE PHASE VELOCITY. */
  private FourPhaseGradientSlow(double ct, double x, double y, double z) {
    this.k = PhaseGradient.of(x, y, z);
    this.components.put(CT, ct);
    this.components.put(X, k.x());
    this.components.put(Y, k.y());
    this.components.put(Z, k.z());
  }
}