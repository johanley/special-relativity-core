package sr.core.hist.lightlike;

import sr.core.component.NEvent;
import sr.core.hist.NDeltaBase;
import sr.core.hist.NHistory;
import sr.core.hist.NMoveableHistory;
import sr.core.hist.NStitchedHistory;
import sr.core.vec3.NDirection;

/**
 History for a particle with no mass moving uniformly in a vacuum from infinity to some event, then in the opposite direction back out to infinity.
 
 <P>Mental model: a light pulse bouncing off a mirror.
 
 <P>If the initial velocity is directed along the negative X-axis, then the history has this general appearance: 
<pre>
            CT
            ^       *
            |     *
            |   *  
            | *    
 -----------*-----------&gt; X
            | *    
            |   *   
            |     *
            |       * 
 </pre>
*/
public final class NMirrorReflection implements NHistory {
  
  /**
   Factory method.
   @param deltaBase of the history, relative to which this history acts.
   @param direction before the turnaround event.
  */
  public static NMirrorReflection of(NDeltaBase deltaBase, NDirection direction) {
    return new NMirrorReflection(deltaBase, direction);
  }

  @Override public NEvent event(double ct) {
    return stitchedHistory.event(ct);
  }

  @Override public String toString() {
    return "PhotonReflection history: " + stitchedHistory;
  }

  private NHistory stitchedHistory;
  
  private NMirrorReflection(NDeltaBase deltaBase, NDirection direction) {
    this.stitchedHistory = stitchedHistory(deltaBase, direction);
  }
  
  private NHistory stitchedHistory(NDeltaBase deltaBase, NDirection direction) {
    NMoveableHistory leg1 = NPhotonStraight.of(deltaBase, direction);
    NMoveableHistory leg2 = NPhotonStraight.of(deltaBase, NDirection.of(direction.times(-1)));
    NStitchedHistory builder = NStitchedHistory.startingWith(leg1);
    builder.addTheNext(leg2, deltaBase.baseEvent().ct());
    return builder.build();
  }
}
