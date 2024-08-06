package sr.core.event.transform;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.Util;
import sr.core.event.Event;
import sr.core.vector.Position;
import sr.core.vector.ThreeVector;
import sr.core.vector.Velocity;

/**
 <a href='https://en.wikipedia.org/wiki/Lorentz_transformation#Vector_transformations'>Lorentz Transformation</a> of an {@link Event}.

 <P>The boost is a velocity. It can be in any direction. 
 Here, it isn't restricted to being along one of the spatial coordinate axes.

 <P>The geometry involves two frames of reference, here called K and K'.
The K' frame is moving with a given velocity with respect the K frame.
The origins of the two frames coincide; that is, 
<pre>(ct,x,y,z) = (ct',x',y',z') = (0,0,0,0)</pre>
 
 <P>A reversal of a Lorentz transformation corresponds to multiplying a velocity vector by -1 (reversing its direction).
 When K and K' share the same orientation of the spatial axes, reversing the sign of the relative velocity corresponds to 
 switching viewpoint from one frame to another.
*/
public final class Boost implements Transform {

  /**
   Factory method.
   @param velocity of K' relative to K.
  */
  public static Boost of(Velocity velocity) {
    return new Boost(velocity);
  }
  
  /** Convenience factory method for the case of a boost parallel to one of the spatial axes. */
  public static Boost of(Axis axis, double β) {
    Util.mustBeSpatial(axis);
    return Boost.of(Velocity.of(axis, β));
  }

  /**
   Transform the event to the boosted inertial frame.  
   The inverse of {@link #changeEvent(Event)}. 
  */
  @Override public Event changeFrame(Event event) {
    return boostIt(event, +1);
  }
  
  /** 
   Transform the event to a new event in the same inertial frame.  
   The inverse of {@link #changeFrame(Event)}. 
  */
  @Override public Event changeEvent(Event event) {
    return boostIt(event, -1);
  }
  
  @Override public String toString() {
    return "boost " + unit.times(β);
  }
  
  // PRIVATE

  /** Unsigned speed. */
  private double β;
  
  /** Unit vector pointing in the direction of the boost-velocity. */
  private ThreeVector unit;
  
  private Boost(Velocity velocity) {
    this.β = velocity.magnitude();
    this.unit = velocity.unitVector();
  }

  //>The formula is taken from 
  //<a href='https://en.wikipedia.org/wiki/Lorentz_transformation#Vector_transformations'>Wikipedia</a>.
  private Event boostIt(Event event, int sign) {
    double Γ = Physics.Γ(β);
    Position r = event.position();
    double ct_Kp = Γ * (event.ct() - sign * r.dot(unit) * β);
    
    ThreeVector a = unit.times(r.dot(unit) * (Γ - 1));
    ThreeVector b = unit.times(Γ * event.ct() * β * sign);
    ThreeVector position_Kp = event.position().plus(a).minus(b);
    return Event.of(ct_Kp, position_Kp.x(), position_Kp.y(), position_Kp.z());
  }
}