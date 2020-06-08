package sr.core.transform;

import sr.core.Config;

/**
 The region of space-time towards which 4-vector is directed. 
 Defined by the sign of the magnitude-squared of the 4-vector. 
*/
public enum FourVectorType {
  
  /** Magnitude-squared is greater than 0. */
  TIMELIKE,
  
  /** Magnitude-squared is less than 0. */
  SPACELIKE,
  
  /** Magnitude-squared is 0. Synonym: 'null'. */
  LIGHTLIKE;

  public static FourVectorType of(FourVector a) {
    FourVectorType result = null;
    double lenSq = a.magnitudeSq();
    if (Math.abs(lenSq) < Config.ε()) {
      result = LIGHTLIKE; //this must come first, because of the epsilon!
    }
    else if (lenSq > 0) {
      result = TIMELIKE;
    }
    else if (lenSq < 0) {
      result = SPACELIKE;
    }
    return result;
  }
  
  public static boolean isTimelike(FourVector a) {
    return of(a) == TIMELIKE;
  }
  
  public static boolean isSpacelike(FourVector a) {
    return of(a) == SPACELIKE;
  }
  
  public static boolean isLightlike(FourVector a) {
    return of(a) == LIGHTLIKE;
  }
}
