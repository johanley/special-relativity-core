package sr.core.hist.lightlike;

import sr.core.component.Event;
import sr.core.hist.DeltaBase;
import sr.core.hist.MoveableHistory;
import sr.core.vec3.Direction;
import sr.core.vec3.Velocity;
import sr.core.vec4.FourDelta;

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
  public static PhotonStraight of(DeltaBase deltaBase, Direction direction) {
    return new PhotonStraight(deltaBase, direction);
  }

  /** Unit speed in the given direction. */
  @Override public Velocity velocity(double ct) {
    return Velocity.unity(direction);
  }
  
  @Override protected FourDelta delta(double Δct) {
    Event b = Event.of(Δct, Δct*direction.x(), Δct*direction.y(), Δct*direction.z());
    return FourDelta.withRespectToOrigin(b);
  }
  
  private Direction direction;
  
  private PhotonStraight(DeltaBase deltaBase, Direction direction) {
    super(deltaBase);
    this.direction = direction;
  }
}
