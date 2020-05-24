package sr.core;


import java.util.function.UnaryOperator;
import static sr.core.Util.*;
import static sr.core.Event.ORIGIN;

/**
 Change the coords of an {@link Event}.
 
 There are two ways of looking at a coord change.
 <ul>
  <li>one event viewed in two inertial frames (more common)
  <li>two events viewed in the one inertial frame
 </ul>
*/
public final class Transformer {
  
  /** 
   @param operations 0 or more transform operations on an event.
   The order is significant. A pipeline of operations needed to do the desired job. 
  */
  public Transformer(UnaryOperator<Event>... operations) {
    this.operations = operations;
  }
  
  /** Apply the operations, in order, to the given event. */
  Event applyTo(Event e) {
    Event result = e;
    for (UnaryOperator<Event> op : operations) {
      result = op.apply(result);
    }
    return result;
  }

  /**
   Change the sign of 1 or more coords. Includes changing the sign of the ct coord, if desired.
   If a coord is unaffected, then just pass {@Parity#EVEN} for that coord. 
  */
  public static UnaryOperator<Event> parity(Parity ctP, Parity xP, Parity yP, Parity zP){
    UnaryOperator<Event> result = (e) -> {
      Event res =  Event.from(
        e.ct() * ctP.sign(), 
        e.x() * xP.sign(), 
        e.y() * yP.sign(), 
        e.z() * zP.sign()
      );
      sameIntervalFromOrigin(e,  res);
      return res;
    };
    return result;
  }
  
  /**
   Add a fixed amount to 1 or more coords.
   If the displacement is for one coord only, then just pass 0 for the remaining ones.
  */
  public static UnaryOperator<Event> displace(double ctD, double xD, double yD, double zD){
    UnaryOperator<Event> result = (e) -> {
      return Event.from(
        e.ct() + ctD, 
        e.x() + xD, 
        e.y() + yD, 
        e.z() + zD
      );
    };
    return result;
  }

  /** Rotation around the given spatial axis. See {@link Rotation}. */
  public static UnaryOperator<Event> rotate(Axis axis, double θ) {
    UnaryOperator<Event> result = (e) -> {
      Rotation rotation = Rotation.along(axis, θ);
      Event res = rotation.applyTo(e);
      sameIntervalFromOrigin(e, res);
      return res;
    };
    return result;
  }
  
  /** Lorentz transformation along the given axis. See {@link LorentzTransformation}. */
  public static UnaryOperator<Event> boostAlong(Axis axis, double β) {
    UnaryOperator<Event> result = (e) -> {
      LorentzTransformation boost = LorentzTransformation.along(axis, β);
      Event res = boost.applyTo(e);
      sameIntervalFromOrigin(e, res);
      return res;
    };
    return result;
  }
  
  // PRIVATE
  private UnaryOperator<Event>[] operations;
  
  /** By default, assertions are turned off at runtime. */
  private static void sameIntervalFromOrigin(Event a, Event b) {
    assert isTiny(Physics.sqInterval(a, ORIGIN) - Physics.sqInterval(b, ORIGIN)) : "Interval has changed too much";
  }
}
