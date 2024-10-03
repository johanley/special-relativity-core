package sr.core.hist.lightlike;

import sr.core.component.Event;
import sr.core.hist.DeltaBase;
import sr.core.hist.MoveableHistory;
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
public final class PhotonStraight extends MoveableHistory {
  
  /**
   Factory method.
   @param deltaBase of the history, relative to which this history acts.
  */
  public static PhotonStraight of(DeltaBase deltaBase, NDirection direction) {
    return new PhotonStraight(deltaBase, direction);
  }

  /** Unit speed in the given direction. */
  @Override public NVelocity velocity(double ct) {
    return NVelocity.unity(direction);
  }
  
  @Override protected NFourDelta delta(double Δct) {
    Event b = Event.of(Δct, Δct*direction.x(), Δct*direction.y(), Δct*direction.z());
    return NFourDelta.withRespectToOrigin(b);
  }
  
  private NDirection direction;
  
  private PhotonStraight(DeltaBase deltaBase, NDirection direction) {
    super(deltaBase);
    this.direction = direction;
  }
}
