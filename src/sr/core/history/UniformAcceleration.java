package sr.core.history;

import static sr.core.Util.mustBeSpatial;
import static sr.core.Util.mustHave;
import static sr.core.Util.sq;
import static sr.core.Util.sqroot;

import sr.core.Axis;
import sr.core.event.Event;
import sr.core.vector.Position;
import static sr.core.history.ParameterizedBy.*;

/**
 History for a particle with mass moving with uniform 
 <a href='https://en.wikipedia.org/wiki/Proper_acceleration'>proper acceleration</a>, 
 in a fixed direction.

 <P>A person riding in a rocket with uniform proper acceleration feels a constant g-force.
 <P>The initial velocity is zero. The initial position is the origin.
  The history forms a hyperbola in space-time. When the initial position is the origin, it has this
  general appearance:
  
 <pre>
            CT
            ^
            |      *
            |  *
            |*
 -----------*-----------------&gt; X
            |*
            |  *
            |      *
 </pre>
 
 <P>The velocity is always parallel to one of the spatial axes of the coordinate system.
 <P>Formula reference: <a href='https://en.wikipedia.org/wiki/Acceleration_(special_relativity)'>Wikipedia</a> and Rindler's book.
 <P>This history can be parameterized either by proper time or by coordinate time, according to data passed to the constructor.
*/
public final class UniformAcceleration implements History {
  
  /**
   Factory method.
   
   @param axis a spatial axis
   @param gee must be non-zero
   @param initialPosition at ct=0
   @param paramBy proper-time or coordinate-time
  */
  public static UniformAcceleration of(Axis axis, double gee, Position initialPosition, ParameterizedBy paramBy) {
    return new UniformAcceleration(axis, gee, initialPosition, paramBy);
  }
  
  @Override public Event event(double λ) {
    return PROPER_TIME == paramBy ? eventFromProperTime(λ) : eventFromCoordinateTime(λ);
  }

  @Override public double convert(double λ) {
    return PROPER_TIME == paramBy ? convertFromProperTime(λ) : convertFromCoordinateTime(λ);
  }

  @Override public String toString() {
    return "UniformAcceleration: axis:" + axis + " gee:" + gee + " initial-event:" + initialEvent + " parameterized-by:" + paramBy;
  }

  private Axis axis;
  private double gee;
  private Event initialEvent;
  private ParameterizedBy paramBy;
  private static double c = 1.0; //to allow for easier comparison with references
  
  private UniformAcceleration(Axis axis, double gee, Position initialPosition, ParameterizedBy paramBy) {
    mustBeSpatial(axis);
    mustHave(Math.abs(gee) > 0, "Must have a non-zero acceleration.");
    this.axis = axis;
    this.gee = gee;
    this.initialEvent = Event.of(0.0, initialPosition);
    this.paramBy = paramBy;
  }
  
  private Event eventFromProperTime(double τ) {
    double a = Math.cosh(gp(τ)) - 1;
    double distance = (sq(c)/gee)*a;
    Position displacement = Position.of(axis, distance);
    return initialEvent.plus(Event.of(τ, displacement));
  }

  private Event eventFromCoordinateTime(double ct) {
    double a = sqroot(1.0 + sq(gp(ct)));
    double distance = (sq(c)/gee)*(a - 1);
    Position displacement = Position.of(axis, distance);
    return initialEvent.plus(Event.of(ct, displacement));
  }
  
  /** The 'g-param' that's repeated in the formulas. */
  private double gp(double λ) {
    return (gee*λ)/c;
  }
  
  private double convertFromProperTime(double τ) {
    return (c/gee)*Math.sinh(gp(τ));
  }
  
  private double convertFromCoordinateTime(double ct) {
    double gp = gp(ct);
    double a = sqroot(1.0 + sq(gp));
    double b = a + gp;
    return (c/gee)*Math.log(b);
  }
}