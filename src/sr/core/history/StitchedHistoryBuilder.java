package sr.core.history;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import sr.core.event.Event;

/** 
 Piece together histories in order to make another {@link History}.
 This class works closely with {@link MoveableHistory}.
 
 <P>The mental model: the history-parts are full histories in themselves.
 But this class 'pays attention' to only one of those histories at a time, according the value of the coordinate-time.
 The history-parts are stitched together using various {@link BranchPoint} objects to determine which history 
 to use for a given coordinate-time. 
*/
public class StitchedHistoryBuilder {

  /** 
   Factory method.
   @param firstLeg of the history. The first leg begins to be valid for ct = -infinity. 
  */
  public static StitchedHistoryBuilder startingWith(History firstLeg) {
    return new StitchedHistoryBuilder(firstLeg);
  }

  /**
   Add another section to the history.  
   Each leg must be added in order of increasing coordinate-time for its {@link BranchPoint}.
   @param ct the coordinate time used to create a {@link BranchPoint}; the τ value for the branch-point 
   is taken from the previously added leg. 
  */
  public void addTheNext(History leg, double ct) {
    BranchPoint branchPoint = branchPointFor(ct);
    addTheNext(leg, branchPoint);
  }

  /** 
   Return a full history whose pieces are the legs passed in previously.
   The position of the origin is not used here. 
  */
  public History build() {
    return new History() {
      @Override public Event event(double ct) {
        Function<BranchPoint, Double> function = bp -> bp.ct(); 
        return legFor(ct, function).event(ct);
      }
      @Override public double ct(double τ) {
        Function<BranchPoint, Double> function = bp -> bp.τ(); 
        return legFor(τ, function).ct(τ);
      }
      @Override public double τ(double ct) {
        Function<BranchPoint, Double> function = bp -> bp.ct(); 
        return legFor(ct, function).τ(ct);
      }
    };
  }

  //LinkedHashMap: iteration order = insertion order; that's important here
  private Map<BranchPoint, History> legs = new LinkedHashMap<>();
  
  private StitchedHistoryBuilder(History firstLeg) {
    double infin = Double.MAX_VALUE;
    BranchPoint initialBranchPoint = BranchPoint.of(-infin, -infin);
    this.legs.put(initialBranchPoint, firstLeg); 
  }
  
  /** Make a {@link BranchPoint} using an event on the most recently added history. */
  private BranchPoint branchPointFor(double ct) {
    BranchPoint last = mostRecentlyAddedBranchPoint();
    History lastHistory = legs.get(last);
    return BranchPoint.of(lastHistory.event(ct).ct(), lastHistory.τ(ct));
  }
  
  /**
   Add another section to the history. 
   Each leg must be added in order of increasing coordinate-time for its {@link BranchPoint}.
   @param branchPoint controls which leg is 'active' for a given coordinate-time. 
  */
  private  void addTheNext(History leg, BranchPoint branchPoint) {
    checkOrder(branchPoint);
    legs.put(branchPoint, leg);
  }
  
  private History legFor(double target, Function<BranchPoint, Double> function) {
    History result = null;
    for(BranchPoint branchPoint : legs.keySet()) {
      if (target >= function.apply(branchPoint)) {
        result = legs.get(branchPoint);
      }
    }
    return result;
  }
  
  private void checkOrder(BranchPoint nextBranchPoint) {
    if (nextBranchPoint.ct() <= mostRecentlyAddedBranchPoint().ct()) {
      throw new IllegalArgumentException("BranchPoint " + nextBranchPoint + " must come after " + mostRecentlyAddedBranchPoint());
    }
  }
  
  private BranchPoint mostRecentlyAddedBranchPoint() {
    BranchPoint result = null;
    for(BranchPoint branchPoint : legs.keySet() /* assume same order as the map */) {
      result = branchPoint;
    }
    return result;
  }
}
