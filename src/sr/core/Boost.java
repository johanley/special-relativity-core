package sr.core;

import static sr.core.Axis.*;

/** Lorentz transformation. */
public final class Boost {

  /**
   The boost is applied to the inertial frame, not to events in the frame.
   @param β is positive for boosts parallel to the given axis, and negative for 
   boosts antiparallel to the given axis. 
  */
  public static Boost along(Axis axis, double β) {
    return new Boost(axis, β);
  }
  
  /** Apply the boost to the given event. */
  public Event applyTo(Event e) {
    Event res = null;
    //there's a small bit of code repetition here, but it's not too bad
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
  
  private Boost(Axis axis, double β) {
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
