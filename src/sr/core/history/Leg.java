package sr.core.history;

import java.util.List;

import sr.core.transform.CoordTransform;

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
   @param transform how to transform the 'native' coords of the given {@link History}
   into the coords needed by the caller.
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
  
  public History history() { return history; }
  public CoordTransform transform() { return transform; }
  
  // PRIVATE 
  
  private History history;
  private CoordTransform transform;
}
