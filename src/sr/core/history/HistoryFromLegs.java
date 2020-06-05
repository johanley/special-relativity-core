package sr.core.history;

import java.util.List;

import sr.core.transform.FourVector;

/** 
 Make a {@link History} from simpler pieces called {@link Leg}s.
 Abstract Base Class (ABC).  
*/
public abstract class HistoryFromLegs implements History {

  /** The legs of the history. Subclasses define what the legs are.  */
  abstract protected List<Leg> initLegs();

  /**
   Find out which {@link Leg} corresponds to τ, and compute the corresponding event.
   
   <P>Each leg has its own coord system, so the nominal coords of the event 
   are transformed into the coord system being used by the caller.
   This allows the core logic of each leg to be defined simply, while still allowing 
   for the many variations that are needed for different cases.
  */
  @Override public final FourVector event(double τ) {
    withinLimits(τ);
    
    //which leg are we currently looking for? get its 'basic' event
    int legIdx = Leg.legIdxForTau(τ, legs());
    FourVector event = legs().get(legIdx).history().event(τ);

    //transform the basic event coords back to the beginning-frame
    int idx = legIdx;
    while (idx >= 0) {
      //just keep writing over the same object, to get the final result
      event = legs().get(idx).transform().toNewVector4(event);
      idx--;
    }
    return event;    
  }
  
  /** The value of τ at the end-event is {@link #τmin} plus the τ-durations of all the legs. */
  @Override public final double τmax() { 
    double result = τmin();
    for(Leg leg : legs) {
      double legτ = leg.history().τmax() - leg.history().τmin(); 
      result = result + legτ; 
    }
    return result;
  }
  
  // PRIVATE 
  
  private List<Leg> legs;

  private final List<Leg> legs(){
    if (legs == null) {
      legs = initLegs();
    }
    return legs;
  }
}
