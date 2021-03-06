package sr.core.history;

import java.util.List;

import sr.core.transform.CoordTransform;
import sr.core.transform.NoOpTransform;

/**
 A part of a larger {@link History}, like the legs (stages) of a plane trip.
 
 Struct that combines a simple implementation of a building-block {@link History} with a 
 {@link CoordTransform}, such that the event returned by the building-block 
 can be transformed into the coords needed by the caller.
*/
public final class Leg {
  
  /**
   Constructor.
   
   @param history a simple history, that uses its own natural coordinate system
   @param transform how to transform the 'native' coords of 4-vectors 
   into other coords, as defined by the needs of the caller.
   This almost always transforms the 4-vector components into the frame used by the previous leg.
   For the first leg, you'll almost always use {@link NoOpTransform}. 
  */
  public Leg(History history, CoordTransform transform){
    this.history = history;
    this.transform = transform;
  }
  
  /** Return the index of the {@link Leg} that applies to the given τ. */
  static int legIdxForTau(double τ, List<Leg> legs) {
    int result = 0;
    for(Leg leg : legs) {
      if (leg.history.τmin() <= τ && τ <= leg.history.τmax()) {
        //found a match
        break;
      }
      ++result;
    }
    return result;
  }
  
  /** Return the {@link Leg} that applies to the given τ. Returns null if no match found. */
  static Leg legForTau(double τ, List<Leg> legs) {
    Leg result = null;
    for(Leg leg : legs) {
      if (leg.history.τmin() <= τ && τ <= leg.history.τmax()) {
        //found a match
        result = leg;
        break;
      }
    }
    return result;
  }
  
  public History history() { return history; }
  public CoordTransform transform() { return transform; }
  
  // PRIVATE 
  
  private History history;
  private CoordTransform transform;
}
