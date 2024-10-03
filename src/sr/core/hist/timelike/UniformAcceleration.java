package sr.core.hist.timelike;

import static sr.core.Util.mustBeSpatial;
import static sr.core.Util.mustHave;
import static sr.core.Util.sq;
import static sr.core.Util.sqroot;

import sr.core.Axis;
import sr.core.component.Event;
import sr.core.component.Position;
import sr.core.vec3.AxisAngle;
import sr.core.vec3.Velocity;
import sr.core.vec4.FourDelta;

/**
 History for a particle with mass moving with uniform 
 <a href='https://en.wikipedia.org/wiki/Proper_acceleration'>proper acceleration</a>, 
 in a fixed direction.
 
 <P>An electron moving in a uniform electric field follows this type of history.

 <P>A person riding in a rocket with uniform proper acceleration feels a constant g-force.
 <P>The initial velocity at the origin-event is 0. 
  <b>This history forms a hyperbola in space-time.</b>. 
  When the origin is at (0,0,0,0), it has this general appearance:
  
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
 <P>Formula reference: <a href='https://en.wikipedia.org/wiki/Acceleration_(special_relativity)'>Wikipedia</a> and Rindler's book <em>Introduction to Special Relativity</em>.
*/
public final class UniformAcceleration extends TimelikeMoveableHistory {

  /**
   Factory method.
   @param deltaBase of the history, relative to which this history acts.
   @param axis spatial axis parallel to the linear acceleration.
   @param gee the constant, non-zero proper-acceleration 'felt' by the astronaut.
  */
  public static UniformAcceleration of(TimelikeDeltaBase deltaBase, Axis axis, double gee) {
    return new UniformAcceleration(deltaBase, axis, gee);
  }

  /**
   Factory method.
   @param axis spatial axis parallel to the linear acceleration.
   @param gee the constant, non-zero proper-acceleration 'felt' by the astronaut. 
   @param initialPosition for the object at ct=0, with proper time zero at that time as well.
  */
  public static UniformAcceleration of(Position initialPosition, Axis axis, double gee) {
    return new UniformAcceleration(TimelikeDeltaBase.of(initialPosition), axis, gee);
  }

  @Override protected FourDelta delta(double Δct) {
    double a = sqroot(1.0 + sq(gp(Δct)));
    double distance = (sq(c)/gee)*(a - 1);
    Position displacement = Position.of(axis, distance);
    Event b = Event.of(Δct, displacement);
    return FourDelta.withRespectToOrigin(b);
  }

  @Override protected double Δct(double Δτ) {
    return (c/gee) * Math.sinh(gp(Δτ));
  }
  
  @Override protected double Δτ(double Δct) {
    double gp = gp(Δct);
    double a = sqroot(1.0 + sq(gp));
    double b = a + gp;
    return (c/gee) * Math.log(b);
  }

  @Override public Velocity velocity(double Δct) {
    double gp = gp(Δct);
    double β = (gee * Δct) * Math.pow(1 + sq(gp), -0.5);
    return Velocity.of(β, axis);
  }
  
  @Override public AxisAngle rotation(double Δct) {
    return AxisAngle.zero();
  }
  
  @Override public String toString() {
    return "UniformAcceleration: " + timelikeDeltaBase() + " axis:" + axis  + " gee:" + gee;
  }

  private Axis axis;
  private double gee;
  private static final double c = 1.0; //to allow for easier comparison with references
  
  private UniformAcceleration(TimelikeDeltaBase deltaBase, Axis axis, double gee) {
    super(deltaBase);
    mustBeSpatial(axis);
    mustHave(Math.abs(gee) > 0, "Must have a non-zero acceleration.");
    this.axis = axis;
    this.gee = gee;
  }
  
  /** The 'g-param' that's repeated in the formulas. */
  private double gp(double λ) {
    return (gee*λ)/c;
  }
}