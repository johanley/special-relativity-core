package sr.core.history.lightlike;

import sr.core.history.DeltaBase;
import sr.core.history.History;
import sr.core.history.MoveableHistory;
import sr.core.history.StitchedHistory;
import sr.core.vector3.Direction;
import sr.core.vector4.Event;

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
public final class MirrorReflection implements History {
  
  /**
   Factory method.
   @param deltaBase of the history, relative to which this history acts.
   @param direction before the turnaround event.
  */
  public static MirrorReflection of(DeltaBase deltaBase, Direction direction) {
    return new MirrorReflection(deltaBase, direction);
  }

  @Override public Event event(double ct) {
    return stitchedHistory.event(ct);
  }

  @Override public String toString() {
    return "PhotonReflection history: " + stitchedHistory;
  }

  private History stitchedHistory;
  
  private MirrorReflection(DeltaBase deltaBase, Direction direction) {
    this.stitchedHistory = stitchedHistory(deltaBase, direction);
  }
  
  private History stitchedHistory(DeltaBase deltaBase, Direction direction) {
    MoveableHistory leg1 = PhotonStraight.of(deltaBase, direction);
    MoveableHistory leg2 = PhotonStraight.of(deltaBase, Direction.of(direction.times(-1)));
    StitchedHistory builder = StitchedHistory.startingWith(leg1);
    builder.addTheNext(leg2, deltaBase.ΔbaseEvent().ct());
    return builder.build();
  }
}
