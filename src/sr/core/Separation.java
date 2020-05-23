package sr.core;

/** 
 The separation between any pair of events.
 Defined by the sign of the interval-squared between the two events. 
*/
public enum Separation {
  
  /** Interval-squared is greater than 0. */
  TIMELIKE,
  
  /** Interval-squared is less than 0. */
  SPACELIKE,
  
  /** 
   Interval-squared is less than 0.
   Same as light-like. 
  */
  NULL;

  /** Return the separation between the 2 events. */
  public static Separation between(Event a, Event b) {
    Separation result = null;
    double intSq = a.intervalSqBetween(b);
    if (Math.abs(intSq) < Config.ε()) {
      result = NULL; //this must come first, because of the epsilon!
    }
    else if (intSq > 0) {
      result = TIMELIKE;
    }
    else if (intSq < 0) {
      result = SPACELIKE;
    }
    return result;
  }
  
  public static boolean isTimelike(Event a, Event b) {
    return between(a, b) == TIMELIKE;
  }
  
  public static boolean isSpacelike(Event a, Event b) {
    return between(a, b) == SPACELIKE;
  }
  
  public static boolean isNull(Event a, Event b) {
    return between(a, b) == NULL;
  }
}
