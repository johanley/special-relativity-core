package sr.core.history.lightlike;

import sr.core.history.DeltaBase;
import sr.core.history.MoveableHistory;
import sr.core.vector3.Direction;
import sr.core.vector3.Velocity;
import sr.core.vector4.Event;

/**
History for a particle with no mass moving uniformly at speed <em>c=1</em> in a given direction.
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
    return Velocity.of(1, direction);
  }
  
  @Override protected Event Δevent(double Δct) {
    return Event.of(Δct, Δct*direction.x(), Δct*direction.y(), Δct*direction.z());
  }
  
  private Direction direction;
  
  private PhotonStraight(DeltaBase deltaBase, Direction direction) {
    super(deltaBase);
    this.direction = direction;
  }
}
