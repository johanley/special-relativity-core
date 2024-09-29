package sr.core.hist.timelike;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import sr.core.component.NEvent;

/** 
 Piece together histories in order to make another {@link NTimelikeHistory}.
 
 <P>The mental model: the history-parts are full histories in themselves.
 But this class 'pays attention' to only one of those histories at a time, according the value of the coordinate-time.
 The history-parts are stitched together using various {@link NBranchPoint} objects to determine which history 
 to use for a given coordinate-time. 
*/
public class NStitchedTimelikeHistory {

  /** 
   Factory method.
   @param firstLeg of the history. The first leg begins to be valid for ct = -infinity. 
  */
  public static NStitchedTimelikeHistory startingWith(NTimelikeHistory firstLeg) {
    return new NStitchedTimelikeHistory(firstLeg);
  }

  /**
   Add another section to the history.  
   Each leg must be added in order of increasing coordinate-time.
   @param ct the coordinate time used to create a {@link NBranchPoint}; the τ value for the branch-point 
   is taken from the previously added leg. 
  */
  public void addTheNext(NTimelikeHistory leg, double ct) {
    NBranchPoint branchPoint = branchPointFor(ct);
    addTheNext(leg, branchPoint);
  }

  /**  Return a full history whose pieces are the legs passed in previously. */
  public NTimelikeHistory build() {
    return new NTimelikeHistory() {
      @Override public NEvent event(double ct) {
        Function<NBranchPoint, Double> function = bp -> bp.ct(); 
        return legFor(ct, function).event(ct);
      }
      @Override public double ct(double τ) {
        Function<NBranchPoint, Double> function = bp -> bp.τ(); 
        return legFor(τ, function).ct(τ);
      }
      @Override public double τ(double ct) {
        Function<NBranchPoint, Double> function = bp -> bp.ct(); 
        return legFor(ct, function).τ(ct);
      }
    };
  }

  //LinkedHashMap: iteration order = insertion order; that's important here
  private Map<NBranchPoint, NTimelikeHistory> legs = new LinkedHashMap<>();
  
  private NStitchedTimelikeHistory(NTimelikeHistory firstLeg) {
    double infin = Double.MAX_VALUE;
    NBranchPoint initialBranchPoint = NBranchPoint.of(-infin, -infin);
    this.legs.put(initialBranchPoint, firstLeg); 
  }
  
  /** Make a {@link NBranchPoint} using an event on the most recently added history. */
  private NBranchPoint branchPointFor(double ct) {
    NBranchPoint last = mostRecentlyAddedBranchPoint();
    NTimelikeHistory lastHistory = legs.get(last);
    return NBranchPoint.of(lastHistory.event(ct).ct(), lastHistory.τ(ct));
  }
  
  /**
   Add another section to the history. 
   Each leg must be added in order of increasing coordinate-time for its {@link NBranchPoint}.
   @param branchPoint controls which leg is 'active' for a given coordinate-time. 
  */
  private  void addTheNext(NTimelikeHistory leg, NBranchPoint branchPoint) {
    checkOrder(branchPoint);
    legs.put(branchPoint, leg);
  }
  
  private NTimelikeHistory legFor(double target, Function<NBranchPoint, Double> function) {
    NTimelikeHistory result = null;
    for(NBranchPoint branchPoint : legs.keySet()) {
      if (target >= function.apply(branchPoint)) {
        result = legs.get(branchPoint);
      }
    }
    return result;
  }
  
  private void checkOrder(NBranchPoint nextBranchPoint) {
    if (nextBranchPoint.ct() <= mostRecentlyAddedBranchPoint().ct()) {
      throw new IllegalArgumentException("BranchPoint " + nextBranchPoint + " must come after " + mostRecentlyAddedBranchPoint());
    }
  }
  
  private NBranchPoint mostRecentlyAddedBranchPoint() {
    NBranchPoint result = null;
    for(NBranchPoint branchPoint : legs.keySet() /* assume same order as the map */) {
      result = branchPoint;
    }
    return result;
  }
}
