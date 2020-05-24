package sr.core;

import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;

/** 
 Lorentz transformation.
 
 It's important to note that the Lorentz transformation applies not just to events, but 
 to all 4-vectors. This is related to the fact that the transformation uses dimensionless 
 coefficients (β and Γ). That means that input-units are the same as output-units. 
*/
public final class BoostDEPRECATED {

  /**
   The boost is applied to the inertial frame, not to things in the inertial frame.
   @param β is positive for boosts parallel to the given axis, and negative for 
   boosts antiparallel to the given axis. 
  */
  public static BoostDEPRECATED along(Axis axis, double β) {
    return new BoostDEPRECATED(axis, β);
  }
  
  /** Apply the boost to the given event. */
  public Event applyTo(Event e) {
    Event res = null;
    if (X == axis) {
      EntangledPair pair = entangle(e.ct(), e.x());
      res = Event.from(pair.time, pair.space, e.y(), e.z());
    }
    else if (Y == axis) {
      EntangledPair pair = entangle(e.ct(), e.y());
      res = Event.from(pair.time, e.x(), pair.space, e.z());
    }
    else if (Z == axis) {
      EntangledPair pair = entangle(e.ct(), e.z());
      res = Event.from(pair.time, e.x(), e.y(), pair.space);
    }
    return res;
  }
  
  //PRIVATE
  
  private Axis axis;
  private double β;
  
  private BoostDEPRECATED(Axis axis, double β) {
    this.axis = axis;
    this.β = β;
  }
  
  /** ct gets entangled with a single given spatial coord: x, y, or z. */
  private EntangledPair entangle(double ct, double space) {
    double Γ = Physics.Γ(β);
    EntangledPair result = new EntangledPair();
    result.time =     Γ*ct - Γ*β*space;
    result.space = -Γ*β*ct +   Γ*space;
    return result;
  }

  /** This was created to remove code repetition. */
  private static final class EntangledPair {
    double time;
    double space;
  }
}
