package sr.core.hist.timelike;

import sr.core.Util;
import sr.core.component.NEvent;
import sr.core.vec3.NVelocity;

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
public final class NThereAndBack implements NTimelikeHistory {
  
  /**
   Factory method.
   The overall speed must be in the range (0,1).
   @param deltaBase of the history, relative to which this history acts.
   @param velocity before the turnaround event; the speed cannot be zero
  */
  public static NThereAndBack of(NTimelikeDeltaBase deltaBase, NVelocity velocity) {
    return new NThereAndBack(deltaBase, velocity);
  }

  @Override public NEvent event(double ct) {
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

  private NTimelikeHistory stitchedHistory;
  
  private NThereAndBack(NTimelikeDeltaBase deltaBase, NVelocity velocity) {
    Util.mustHave(velocity.magnitude() > 0, "Speed cannot be zero.");
    this.stitchedHistory = stitchedHistory(deltaBase, velocity);
  }
  
  private NTimelikeHistory stitchedHistory(NTimelikeDeltaBase deltaBase, NVelocity velocity) {
    NTimelikeMoveableHistory leg1 = NUniformVelocity.of(deltaBase, velocity);
    NTimelikeMoveableHistory leg2 = NUniformVelocity.of(deltaBase, NVelocity.of(velocity.times(-1)));
    NStitchedTimelikeHistory builder = NStitchedTimelikeHistory.startingWith(leg1);
    builder.addTheNext(leg2, deltaBase.baseEvent().ct());
    return builder.build();
  }
}
