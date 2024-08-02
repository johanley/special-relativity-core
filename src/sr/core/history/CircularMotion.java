package sr.core.history;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.Util;
import sr.core.event.Event;
import sr.core.event.transform.Rotation;

/**
 History for a mass particle moving uniformly in a circle.
 
 <P>The axis of the circular motion is one of the spatial coordinate axes.
 A change in sign of the speed changes the direction of circular motion.
 
 <P>This class isn't very general. 
 It could be improved by allowing any direction for the spin axis, and by allowing helical motion.
*/
public class CircularMotion extends MoveableHistory {

  /**
   Constructor.
   
   <P>The exact meaning of the items is best illustrated with an example:
   
   <P>For a Z-rotational axis and 0 initial phase, the ray from circle-center to the object is parallel to the +X-axis, and 
   the positive sense of rotation is towards the +Y-axis (as defined by the right-hand rule). 
   
   @param rotationalAxis must be spatial
   @param radius must be positive
   @param β must be a non-zero value in the range (-1,1). Negative values reverse the sense of the rotation.
   @param theta0 initial phase in radians. Must be in range (-2pi,+2pi).
  */
  public static CircularMotion of(DeltaBase deltaBase, double radius, double β, Axis rotationalAxis, double theta0) {
    return new CircularMotion(deltaBase, radius, β, rotationalAxis, theta0);
  }

  @Override protected Event Δevent(double Δct) {
    return displacement(Δct);
  }
  
  @Override protected double Δct(double Δτ) {
    return Δτ * Physics.Γ(β);
  }
  
  @Override protected double Δτ(double Δct) {
    return Δct / Physics.Γ(β);
  }
  
  @Override public String toString() {
    return "CircularMotion axis:" + rotationalAxis + " radius:" + radius + " speed:" + β + " initial-phase:" + theta0; 
  }

  /** Radius of the circle. */
  private double radius;
  
  /** 
   Fixed speed of the circular motion.
   Positive means the motion is from the 'first' axis of the pole to the 'second' axis (right-hand rule). 
   Negative reverses the sense of the circular motion. 
  */
  private double β;

  /** Defines the plane of the circle, and also the positive sense of circular motion, using the right-hand rule. */
  private Axis rotationalAxis;
  
  /** The initial phase of the circular motion (0,2pi). If zero, then the object is on the 'first' axis of the pole, given the right-hand rule. */
  private double theta0;
  
  private CircularMotion(DeltaBase deltaBase, double radius, double β, Axis rotationalAxis, double theta0) {
    super(deltaBase);
    Util.mustBeSpatial(rotationalAxis);
    Util.mustHave(radius > 0, "Radius must be positive.");
    Util.mustHaveSpeedRange(β);
    Util.mustHave(Math.abs(β) > 0, "Speed cannot be 0");
    Util.mustHave(Math.abs(theta0) <= (2*Math.PI), "Initial-phase must be in range 0..2pi: " + theta0);
    
    this.rotationalAxis = rotationalAxis;
    this.radius = radius;
    this.β = β;
    this.theta0 = theta0;
  }

  /** Return the displacement from the delta-base (which uses the center of the circle). */
  private Event displacement(double Δct) {
    Event displacement = Event.origin();
    displacement = displacement.put(Axis.CT, Δct);
    
    //for a rotational axis of Z, this gives the start position on the X-axis
    Axis start = Axis.rightHandRuleFor(rotationalAxis).get(0);
    displacement = displacement.put(start, radius);
    
    Rotation rotate = Rotation.of(rotationalAxis, theta0 + Δθ(Δct));
    displacement = rotate.changeEvent(displacement);
    return displacement;
  }
  
  /** Angle in radians. */
  private double Δθ(double ct) {
    double ω = β/radius; //per length
    return ω * ct; //θ=0 at ct=0; c=1 here; rads
  }
}