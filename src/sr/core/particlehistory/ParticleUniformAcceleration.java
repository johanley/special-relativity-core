package sr.core.particlehistory;

import static sr.core.Util.mustBeSpatial;
import static sr.core.Util.mustHave;
import static sr.core.Util.sq;
import static sr.core.Util.sqroot;

import sr.core.Axis;
import sr.core.Position;
import sr.core.Velocity;
import sr.core.transform.FourVector;

/**
 History for a particle with mass moving with uniform 
 <a href='https://en.wikipedia.org/wiki/Proper_acceleration'>proper acceleration</a>, 
 in a fixed direction.

 <P>A person riding in a rocket with uniform proper acceleration feels a constant g-force.
 <P>The initial velocity is zero.
 <P>The velocity is always parallel to one of the spatial axes of the coordinate system.
 <P>Formula reference: <a href='https://en.wikipedia.org/wiki/Acceleration_(special_relativity)'>Wikipedia</a>.
*/
public final class ParticleUniformAcceleration implements ParticleHistory {
  
  /*
   * THIS NEEDS REVIEW, and confirmation of the formulas.
   * See MTW page 166. Should I parameterize using proper-time in this case?
   * In this case cosh and sinh provide compact formulas.
   * What does Rindler say?
   */

  /**
   Constructor.
   
   @param mass must be positive
   @param axis a spatial axis
   @param acceleration must be non-zero
  */
  public ParticleUniformAcceleration(double mass, Axis axis, double acceleration, Position initialPosition) {
    mustBeSpatial(axis);
    mustHave(Math.abs(acceleration) > 0, "Must have a non-zero acceleration.");
    mustHave(mass > 0, "Must must be greater than zero.");
    this.mass = mass;
    this.axis = axis;
    this.accel = acceleration;
    this.initialEvent = initialPosition.eventForTime(0.0);
  }
  
  /** For a particle having unit mass. */
  public ParticleUniformAcceleration(Axis axis, double acceleration, Position initialPosition) {
    this(1.0, axis, acceleration, initialPosition);
  }
  
  /** @param ct is the coordinate-time. */
  @Override public FourVector event(double ct) {
    double c = 1.0; //to allow comparison with references
    double a = sqroot(1.0 + sq(accel*ct/c));
    double distance = (sq(c)/accel)*(a - 1);
    Position displacement = Position.of(axis, distance);
    return initialEvent.plus(displacement.eventForTime(ct));
  }
  
  /** @param ct is the coordinate-time. */
  @Override public FourVector fourMomentum(double ct) {
    double c = 1.0; 
    double αt = accel * ct;
    double speed = αt / sqroot(1 + sq(αt/c));
    Velocity velocity = Velocity.of(axis, speed);
    return velocity.fourMomentumFor(mass);
  }

  /** 
   The zero of proper-time is taken as the event with ct=0.
   @param ct is the coordinate-time.
   */
  @Override public double τ(double ct) {
    double c = 1.0; 
    double a = sqroot(1.0 + sq(accel*ct/c));
    double b = a + (accel*ct)/c;
    return (c/accel)*Math.log(b);
  }

  private Axis axis;
  private double accel;
  private double mass;
  private FourVector initialEvent;
}
