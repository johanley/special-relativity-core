package sr.core.history;

import java.util.List;

import sr.core.transform.FourVector;

/** 
 Make a {@link History} from simpler pieces called {@link Leg}s.
 Abstract Base Class (ABC).
 
 <P>Each leg has its own independent coord system.
 The caller wants to use a single coord system.
 So the nominal components of 4-vectors are always transformed by this 
 class back into the coord system being used by the caller.
 
 <P>This allows the core logic of each leg to be defined simply, while still allowing 
 for the many variations that are needed for different cases.
 
 <P>EXCEPTION: displacement operations apply to events, but not to differential 4-vectors.
 So, such operations are ignored for the 4-velocity, for example.
 
 <P>WARNING: it's by far the easiest if the meaning of τ is the same for all legs.
*/
public abstract class HistoryFromLegs extends HistoryAbc {

  /** The legs of the history. Subclasses define what the legs are.  */
  abstract protected List<Leg> initLegs();

  /**
   Find out which {@link Leg} corresponds to τ, and compute the corresponding event.
  */
  @Override protected final FourVector eventFor(double τ) {
    //which leg are we currently looking for? get its 'basic' event
    int legIdx = legIdx(τ);
    FourVector event = legs().get(legIdx).history().event(τ);
    return transformCoords(legIdx, event);
  }
  
  /** The value of τ at the end-event is {@link #τmin} plus the τ-durations of all the legs. */
  @Override public final double τmax() { 
    double result = τmin();
    for(Leg leg : legs()) {
      double legτ = leg.history().τmax() - leg.history().τmin(); 
      result = result + legτ; 
    }
    return result;
  }

  /** Returns the 4-velocity as defined by the corresponding leg. */
  @Override protected final FourVector fourVelocityFor(double τ) {
    int legIdx = legIdx(τ);
    FourVector v = legs().get(legIdx).history().fourVelocity(τ);
    return transformCoords(legIdx, v);
  }
  
  /** Returns the β as defined by the corresponding leg. */
  /*
  @Override public final double β(double τ) {
    return legFor(τ).history().β(τ);
  }
  */
  
  // PRIVATE 
  
  private List<Leg> legs;

  private final List<Leg> legs(){
    if (legs == null) {
      legs = initLegs();
    }
    return legs;
  }

  private int legIdx(double τ) {
    return Leg.legIdxForTau(τ, legs());
  }
  
  private FourVector transformCoords(int legIdx, FourVector input) {
    FourVector result = input;
    //transform the basic 4-vector components back to the beginning-frame
    int idx = legIdx;
    while (idx >= 0) {
      //just keep writing over the same object, to get the final result
      result = legs().get(idx).transform().toNewFourVector(result);
      idx--;
    }
    return result;    
  }
}
