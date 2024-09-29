package sr.core.hist.lightlike;

import sr.core.component.NEvent;
import sr.core.hist.NDeltaBase;
import sr.core.hist.NMoveableHistory;
import sr.core.vec3.NDirection;
import sr.core.vec3.NVelocity;
import sr.core.vec4.NFourDelta;

/**
History for a particle with no mass moving uniformly in a vacuum at speed <em>c=1</em> in a given direction.
The general character of this history:

 <pre>
           CT
           ^
           |             *
           |           *
           |         *
-------------------*------------&gt; X
           |     *
           |   * 
           | *
</pre>
*/
public final class NPhotonStraight extends NMoveableHistory {
  
  /**
   Factory method.
   @param deltaBase of the history, relative to which this history acts.
  */
  public static NPhotonStraight of(NDeltaBase deltaBase, NDirection direction) {
    return new NPhotonStraight(deltaBase, direction);
  }

  /** Unit speed in the given direction. */
  @Override public NVelocity velocity(double ct) {
    return NVelocity.unity(direction);
  }
  
  @Override protected NFourDelta delta(double Δct) {
    NEvent b = NEvent.of(Δct, Δct*direction.x(), Δct*direction.y(), Δct*direction.z());
    return NFourDelta.withRespectToOrigin(b);
  }
  
  private NDirection direction;
  
  private NPhotonStraight(NDeltaBase deltaBase, NDirection direction) {
    super(deltaBase);
    this.direction = direction;
  }
}
