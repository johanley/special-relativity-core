package sr.core.history;

import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;

import sr.core.Physics;
import sr.core.Util;
import sr.core.event.Event;
import sr.core.vector.Velocity;

/**
 History for a particle with mass moving uniformly from infinity to the frame's origin, then in the opposite direction back out to infinity.
 
 <P>The two legs take place at the same speed.
 The origin event is the turnaround event.

 <P>If the initial velocity is directed along the negative X-axis, the history has this general appearance: 
<pre>
            CT
            ^   *
            |  *
            | *  
            |*    
 -----------*-----------&gt; X
            |*    
            | *   
            |  * 
 </pre>

 
  <P>The parameter for the history is the coordinate-time <em>ct</em>.
 Negative <em>ct</em> means before the turnaround, and positive <em>ct</em> is after the turnaround.

*/
public final class ThereAndBack implements History {

  /**
   Factory method.
   
   The overall speed must be in the range (0,1).
   @param velocity before the turnaround event; the speed cannot be zero
  */
  public static ThereAndBack of(Velocity velocity) {
    return new ThereAndBack(velocity);
  }
  
  /** @param ct is the coordinate-time. */
  @Override public Event event(double ct) {
    Event displacement = Event.of(ct, ct*velocity.x(), ct*velocity.y(), ct*velocity.z());
    return ct >= 0 ? turnaroundEvent.plus(displacement) : turnaroundEvent.plus(displacement.spatialReflection());
  }
  
  /** 
   Convert coordinate-time to proper time.
   The zero of proper-time is taken as the event with ct = 0.
   @param ct is the coordinate-time.
  */
  @Override public double convert(double ct) {
    return ct / Physics.Γ(velocity.magnitude()); 
  }
  
  @Override public String toString() {
    return "ThereAndBack: turnaround-event:" + turnaroundEvent + " velocity:" + velocity;
  }

  private Event turnaroundEvent = Event.origin(); 
  private Velocity velocity;
  
  private ThereAndBack(Velocity velocity) {
    Util.mustHave(velocity.magnitude() > 0, "Speed cannot be zero.");
    this.velocity = velocity;
  }
}
