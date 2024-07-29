package sr.core.history;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.Rotation;
import sr.core.Util;
import sr.core.event.Event;

/**
 History for a particle with mass moving uniformly in a circle.
 
 <P>The axis of the circular motion is one of the spatial coordinate axes.
 A change in sign of the speed changes the direction of circular motion.
 
 <p>You need to exercise care with the parameters, to ensure that the starting conditions actually give you what's desired.
 
 <P>THIS CLASS IN UNFINISHED AND UNTESTED. 
 THE POLE OF ROTATION IS NOT GENERAL, but aligned with a coordinate axis.
*/
public class CircularMotion extends MoveableHistory {
  
  /**
   Constructor.
   Example: for a Z-rotational axis and 0 initial phase, the ray from circle-center to object is parallel to the +Y-axis.
   @param rotationalAxis must be spatial
   @param radius must be positive
   @param β must be a non-zero value in the range (-1,1). Negative values reverse the sense of the rotation.
   @param initialPhase must be in range (-2pi,+2pi)
  */
  private CircularMotion(DeltaBase deltaBase, double radius, double β, Axis rotationalAxis, double initialPhase) {
    super(deltaBase);
    Util.mustBeSpatial(rotationalAxis);
    Util.mustHave(radius > 0, "Radius must be positive.");
    Util.mustHaveSpeedRange(β);
    Util.mustHave(Math.abs(β) > 0, "Speed cannot be 0");
    Util.mustHave(Math.abs(initialPhase) <= (2*Math.PI), "Phase must be in range 0..2pi: " + initialPhase);
    
    this.rotationalAxis = rotationalAxis;
    this.radius = radius;
    this.β = β;
    this.initialPhase = initialPhase;
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
    return "CircularMotion axis:" + rotationalAxis + " radius:" + radius + " speed:" + β; 
  }

  /** Radius of the circle. */
  private double radius;
  
  /** 
   Fixed speed of the circular motion.
   Positive means the motion is from the 'first' axis of the pole to the 'second' axis (right-hand rule). 
   Negative reverses the sense of the circular motion. 
  */
  private double β;

  /** Defines the plane of the circle, and also the positive sense of circular motion. */
  private Axis rotationalAxis;
  
  /** The initial phase of the circular motion (0,2pi). If zero, then the object is on the 'first' axis of the pole. */
  private double initialPhase;

  /** Return the displacement from the delta-base (which uses the center of the circle). */
  private Event displacement(double ct) {
    Event result = Event.origin();
    //for a rotational axis of Z, this gives the start position on the Y-axis
    Axis startPosition = Axis.rightHandRuleFor(rotationalAxis).get(1);
    result = result.put(startPosition, radius);
    result = result.spatialRotation(Rotation.from(rotationalAxis, initialPhase + Δθ(ct)));
    return result;
  }
  
  /** Angle in radians. */
  private double Δθ(double ct) {
    double ω = β/radius; //per length
    return ω * ct; //θ=0 at ct=0; c=1 here; rads
  }
}