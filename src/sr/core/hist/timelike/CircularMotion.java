package sr.core.hist.timelike;

import sr.core.Axis;
import sr.core.ThomasPrecession;
import sr.core.Physics;
import sr.core.Util;
import sr.core.component.Components;
import sr.core.component.Event;
import sr.core.component.ops.Sense;
import sr.core.vec3.Acceleration;
import sr.core.vec3.AxisAngle;
import sr.core.vec3.ThreeVector;
import sr.core.vec3.Velocity;
import sr.core.vec4.FourDelta;

/**
 History for a mass particle moving uniformly in a circle.
 
 <P>An electron moving perpendicular to a uniform magnetic field follows this type of history.
 (A helical motion is the general case of an electron moving in a uniform magnetic field.)

 <P>The axis of the circular motion is one of the spatial coordinate axes.
 A change in sign of the speed changes the direction of circular motion.
 
 <P>This class isn't very general. 
 It could be improved by allowing any direction for the spin axis, and by allowing helical motion.
*/
public class CircularMotion extends TimelikeMoveableHistory {

  /**
   Constructor.
   
   <P>The exact meaning of the items is best illustrated with an example:
   
   <P>For a Z-rotational axis and 0 initial phase, the ray from circle-center to the object is parallel to the +X-axis, and 
   the positive sense of rotation is towards the +Y-axis (as defined by the right-hand rule). 
   
   @param rotationalAxis must be spatial
   @param radius must be positive
   @param β must be a non-zero value in the range (-1,1). Negative values reverse the sense of the rotation.
   @param theta0 initial phase in radians. Must be in range [0,2pi).
  */
  public static CircularMotion of(TimelikeDeltaBase deltaBase, double radius, double β, Axis rotationalAxis, double theta0) {
    return new CircularMotion(deltaBase, radius, β, rotationalAxis, theta0);
  }

  @Override protected FourDelta delta(double Δct) {
    return displacement(Δct);
  }
  
  @Override protected double Δct(double Δτ) {
    return Δτ * Physics.Γ(β);
  }
  
  @Override protected double Δτ(double Δct) {
    return Δct / Physics.Γ(β);
  }
  
  @Override public Velocity velocity(double ct) {
    return tangentialVelocity(ct);
  }
  
  /**
   There's no restriction on the magnitude of the returned axis-angle; that is, it's not restricted to the range 0..2pi range.
   This helps in knowing how many full rotations have taken place. 
  */
  @Override public AxisAngle rotation(double Δct) {
    AxisAngle precessionRate = ThomasPrecession.ofKprime(acceleration(Δct), velocity(Δct));
    ThreeVector rotation = precessionRate.times(Δct);
    return AxisAngle.of(rotation.x(), rotation.y(), rotation.z());
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
  
  /** The initial phase of the circular motion [0,2pi). If zero, then the object is on the 'first' axis of the pole, given the right-hand rule. */
  private double theta0;
  
  private CircularMotion(TimelikeDeltaBase deltaBase, double radius, double β, Axis rotationalAxis, double theta0) {
    super(deltaBase);
    Util.mustBeSpatial(rotationalAxis);
    Util.mustHave(radius > 0, "Radius must be positive.");
    Util.mustHaveSpeedRange(β);
    Util.mustHave(Math.abs(β) > 0, "Speed cannot be 0");
    Util.mustHave(theta0 >= 0 && theta0 < (2*Math.PI), "Initial-phase must be in range [0..2pi): " + theta0);
    
    this.rotationalAxis = rotationalAxis;
    this.radius = radius;
    this.β = β;
    this.theta0 = theta0;
  }

  /** Return a displacement from the delta-base (which uses the center of the circle). */
  private FourDelta displacement(double Δct) {
    Axis startAxis = Axis.rightHandRuleFor(rotationalAxis).get(0);
    Components comps = Components.of(Δct, 0, 0, 0);
    comps = comps.overwrite(startAxis, radius);
    Event initialEvent = Event.of(comps);
    Event b = initialEvent.rotate(angleFor(Δct), Sense.ChangeComponents);
    return FourDelta.withRespectToOrigin(b);
  }
  
  private Velocity tangentialVelocity(double Δct) {
    Velocity base = Velocity.of(β, Axis.rightHandRuleFor(rotationalAxis).get(1));
    return base.rotate(angleFor(Δct), Sense.ChangeComponents);
  }
  
  private Acceleration acceleration(double Δct) {
    double mag = Util.sq(β) / radius;
    Acceleration base = Acceleration.of(Axis.rightHandRuleFor(rotationalAxis).get(0), -mag);
    return base.rotate(angleFor(Δct), Sense.ChangeComponents);
  }
  
  private AxisAngle angleFor(double Δct) {
    return AxisAngle.of(phase(Δct), rotationalAxis);
  }
  
  private double phase(double Δct) {
    return theta0 + Δθ(Δct);
  }
  
  /** Angle in radians. */
  private double Δθ(double ct) {
    double ω = β/radius; //per length
    return ω * ct; //θ=0 at ct=0; c=1 here; rads
  }
}