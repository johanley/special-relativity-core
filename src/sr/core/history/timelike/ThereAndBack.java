package sr.core.history.timelike;

import sr.core.Util;
import sr.core.vector3.Velocity;
import sr.core.vector4.Event;

/**
 History for a particle with mass moving uniformly from infinity to some event, then in the opposite direction back out to infinity.
 
 <P>The two legs take place at the same speed.

 <P>If the initial velocity is directed along the negative X-axis, then the history has this general appearance: 
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
            |   * 
 </pre>
*/
public final class ThereAndBack implements TimelikeHistory {
  
  /**
   Factory method.
   The overall speed must be in the range (0,1).
   @param deltaBase of the history, relative to which this history acts.
   @param velocity before the turnaround event; the speed cannot be zero
  */
  public static ThereAndBack of(TimelikeDeltaBase deltaBase, Velocity velocity) {
    return new ThereAndBack(deltaBase, velocity);
  }

  @Override public Event event(double ct) {
    return stitchedHistory.event(ct);
  }

  @Override public double τ(double ct) {
    return stitchedHistory.τ(ct);
  }

  @Override public double ct(double τ) {
    return stitchedHistory.ct(τ);
  }
  
  @Override public String toString() {
    return "ThereAndBack stitched history: " + stitchedHistory;
  }

  private TimelikeHistory stitchedHistory;
  
  private ThereAndBack(TimelikeDeltaBase deltaBase, Velocity velocity) {
    Util.mustHave(velocity.magnitude() > 0, "Speed cannot be zero.");
    this.stitchedHistory = stitchedHistory(deltaBase, velocity);
  }
  
  private TimelikeHistory stitchedHistory(TimelikeDeltaBase deltaBase, Velocity velocity) {
    TimelikeMoveableHistory leg1 = UniformVelocity.of(deltaBase, velocity);
    TimelikeMoveableHistory leg2 = UniformVelocity.of(deltaBase, Velocity.of(velocity.times(-1)));
    StitchedTimelikeHistory builder = StitchedTimelikeHistory.startingWith(leg1);
    builder.addTheNext(leg2, deltaBase.ΔbaseEvent().ct());
    return builder.build();
  }
}
