package sr.core.history;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.Rotation;
import sr.core.Util;
import sr.core.event.Event;
import sr.core.vector.Velocity;

/**
 History for a particle with mass moving uniformly in a circle.
 The circle is centered at the origin.
 
 <P>The axis of the circular motion is one of the spatial coordinate axes.
 A change in sign of the speed changes the direction of circular motion.
 
 <P>At ct=0, the particle is on one of the spatial axes.
*/
public class CircularMotion implements History {

  /**
   Constructor.
   @param rotationalAxis must be spatial
   @param radius must be positive
   @param β must be a non-zero value in the range (-1,1). Negative values reverse the sense of the rotation.
  */
  public CircularMotion(Axis rotationalAxis, double radius, double β) {
    Util.mustBeSpatial(rotationalAxis);
    Util.mustHave(radius > 0, "Radius must be positive.");
    Util.mustHaveSpeedRange(β);
    Util.mustHave(Math.abs(β) > 0, "Speed cannot be 0.");
    
    this.rotationalAxis = rotationalAxis;
    this.radius = radius;
    this.initialEvent = initialEvent();
    this.β = β;
    // At ct=0, the momentum is directed toward a spatial axis. 
    Axis startDirection = Axis.rightHandRuleFor(rotationalAxis).get(1);
    this.initialVelocity = Velocity.of(startDirection, β);
  }

  /** @param ct is the coordinate-time. */
  @Override public Event event(double ct) {
    return rotateThe(initialEvent, ct);
  }
  
  /** 
   The zero of proper-time is taken as the event with ct = 0.
   @param ct is the coordinate-time.
  */
  @Override public double τ(double ct) {
   return ct / Physics.Γ(initialVelocity.magnitude());
  }
  
  @Override public String toString() {
    return "CircularMotion axis:" + rotationalAxis + " radius:" + radius + " speed:" + β; 
  }
  
  /** Defines the plane of the circle, and also the sense of circular motion. */
  private Axis rotationalAxis;
  
  /** Radius of the circle. */
  private double radius;
  
  /** 
   Fixed speed. Negative reverses the sense of the circular motion.
   Velocity.β() can't be used here, since it has no sign.
   We want the sign of β to control the sense of circular motion.
  */
  private double β;

  private Event initialEvent;
  private Velocity initialVelocity;
  
  /** At ct=0, the position is on a spatial axis. */
  private Event initialEvent() {
    Event result = Event.origin();
    Axis startPosition = Axis.rightHandRuleFor(rotationalAxis).get(0);
    result = result.put(startPosition, radius);
    return result;
  }

  /** Spatial rotation of the given 4-vector, to return a new 4-vector. */
  private Event rotateThe(Event fourVector, double ct) {
    return fourVector.spatialRotation(Rotation.from(rotationalAxis, θ(ct)));
  }
  
  /** Angle in radians, measured from the starting-point spatial axis.*/
  private double θ(double ct) {
    double ω = β/radius; //per length
    return ω * ct; //θ=0 at ct=0; c=1 here; rads
  }
}