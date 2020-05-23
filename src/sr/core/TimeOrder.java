package sr.core;

/** 
 The time-order of any pair of events.
 If the {@link Separation} is time-like, then the time-order is invariant. 
 If the {@link Separation} is space-like, then the time-order is NOT invariant. 
*/
public enum TimeOrder {
  
  BEFORE,
  
  AFTER,
  
  SIMULTANEOUS;

  /** Time-order of a with respect to b. */
  public static TimeOrder of(Event a, Event b) {
    TimeOrder result = null;
    double ct = a.ct() - b.ct();
    if (Math.abs(ct) < Config.ε()) {
      result = SIMULTANEOUS; //must be first!
    }
    else if (ct > 0) {
      result = AFTER;
    }
    else if (ct < 0) {
      result = BEFORE;
    }
    return result;
  }

}
