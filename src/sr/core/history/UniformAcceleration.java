package sr.core.history;

import static sr.core.Util.mustBeSpatial;
import static sr.core.Util.mustHave;
import static sr.core.Util.sq;
import static sr.core.Util.sqroot;

import sr.core.Axis;
import sr.core.event.Event;
import sr.core.vector.Position;

/**
 History for a particle with mass moving with uniform 
 <a href='https://en.wikipedia.org/wiki/Proper_acceleration'>proper acceleration</a>, 
 in a fixed direction.

 <P>A person riding in a rocket with uniform proper acceleration feels a constant g-force.
 <P>The initial velocity is zero.
 <P>The velocity is always parallel to one of the spatial axes of the coordinate system.
 <P>Formula reference: <a href='https://en.wikipedia.org/wiki/Acceleration_(special_relativity)'>Wikipedia</a>.
*/
public final class UniformAcceleration implements History {
  
  /*
   * THIS NEEDS REVIEW, and confirmation of the formulas.
   * See MTW page 166. Should I parameterize using proper-time in this case?
   * In this case cosh and sinh provide compact formulas.
   * What does Rindler say?
   */

  /**
   Factory method.
   
   @param axis a spatial axis
   @param acceleration must be non-zero
  */
  public static UniformAcceleration of(Axis axis, double acceleration, Position initialPosition) {
    return new UniformAcceleration(axis, acceleration, initialPosition);
  }
  
  /** @param ct is the coordinate-time. */
  @Override public Event event(double ct) {
    double c = 1.0; //to allow comparison with references
    double a = sqroot(1.0 + sq(accel*ct/c));
    double distance = (sq(c)/accel)*(a - 1);
    Position displacement = Position.of(axis, distance);
    return initialEvent.plus(Event.of(ct, displacement));
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
  
  @Override public String toString() {
    return "UniformAccelaration: axis:" + axis + " proper-accelaration:" + accel + " initial-event:" + initialEvent;
  }

  private Axis axis;
  private double accel;
  private Event initialEvent;
  
  private UniformAcceleration(Axis axis, double acceleration, Position initialPosition) {
    mustBeSpatial(axis);
    mustHave(Math.abs(acceleration) > 0, "Must have a non-zero acceleration.");
    this.axis = axis;
    this.accel = acceleration;
    this.initialEvent = Event.of(0.0, initialPosition);
  }
  
}

