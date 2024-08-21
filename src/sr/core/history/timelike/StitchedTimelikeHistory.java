package sr.core.history.timelike;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import sr.core.vector4.Event;

/** 
 Piece together histories in order to make another {@link TimelikeHistory}.
 
 <P>The mental model: the history-parts are full histories in themselves.
 But this class 'pays attention' to only one of those histories at a time, according the value of the coordinate-time.
 The history-parts are stitched together using various {@link TimelikeBranchPoint} objects to determine which history 
 to use for a given coordinate-time. 
*/
public class StitchedTimelikeHistory {

  /** 
   Factory method.
   @param firstLeg of the history. The first leg begins to be valid for ct = -infinity. 
  */
  public static StitchedTimelikeHistory startingWith(TimelikeHistory firstLeg) {
    return new StitchedTimelikeHistory(firstLeg);
  }

  /**
   Add another section to the history.  
   Each leg must be added in order of increasing coordinate-time.
   @param ct the coordinate time used to create a {@link TimelikeBranchPoint}; the τ value for the branch-point 
   is taken from the previously added leg. 
  */
  public void addTheNext(TimelikeHistory leg, double ct) {
    TimelikeBranchPoint branchPoint = branchPointFor(ct);
    addTheNext(leg, branchPoint);
  }

  /**  Return a full history whose pieces are the legs passed in previously. */
  public TimelikeHistory build() {
    return new TimelikeHistory() {
      @Override public Event event(double ct) {
        Function<TimelikeBranchPoint, Double> function = bp -> bp.ct(); 
        return legFor(ct, function).event(ct);
      }
      @Override public double ct(double τ) {
        Function<TimelikeBranchPoint, Double> function = bp -> bp.τ(); 
        return legFor(τ, function).ct(τ);
      }
      @Override public double τ(double ct) {
        Function<TimelikeBranchPoint, Double> function = bp -> bp.ct(); 
        return legFor(ct, function).τ(ct);
      }
    };
  }

  //LinkedHashMap: iteration order = insertion order; that's important here
  private Map<TimelikeBranchPoint, TimelikeHistory> legs = new LinkedHashMap<>();
  
  private StitchedTimelikeHistory(TimelikeHistory firstLeg) {
    double infin = Double.MAX_VALUE;
    TimelikeBranchPoint initialBranchPoint = TimelikeBranchPoint.of(-infin, -infin);
    this.legs.put(initialBranchPoint, firstLeg); 
  }
  
  /** Make a {@link TimelikeBranchPoint} using an event on the most recently added history. */
  private TimelikeBranchPoint branchPointFor(double ct) {
    TimelikeBranchPoint last = mostRecentlyAddedBranchPoint();
    TimelikeHistory lastHistory = legs.get(last);
    return TimelikeBranchPoint.of(lastHistory.event(ct).ct(), lastHistory.τ(ct));
  }
  
  /**
   Add another section to the history. 
   Each leg must be added in order of increasing coordinate-time for its {@link TimelikeBranchPoint}.
   @param branchPoint controls which leg is 'active' for a given coordinate-time. 
  */
  private  void addTheNext(TimelikeHistory leg, TimelikeBranchPoint branchPoint) {
    checkOrder(branchPoint);
    legs.put(branchPoint, leg);
  }
  
  private TimelikeHistory legFor(double target, Function<TimelikeBranchPoint, Double> function) {
    TimelikeHistory result = null;
    for(TimelikeBranchPoint branchPoint : legs.keySet()) {
      if (target >= function.apply(branchPoint)) {
        result = legs.get(branchPoint);
      }
    }
    return result;
  }
  
  private void checkOrder(TimelikeBranchPoint nextBranchPoint) {
    if (nextBranchPoint.ct() <= mostRecentlyAddedBranchPoint().ct()) {
      throw new IllegalArgumentException("BranchPoint " + nextBranchPoint + " must come after " + mostRecentlyAddedBranchPoint());
    }
  }
  
  private TimelikeBranchPoint mostRecentlyAddedBranchPoint() {
    TimelikeBranchPoint result = null;
    for(TimelikeBranchPoint branchPoint : legs.keySet() /* assume same order as the map */) {
      result = branchPoint;
    }
    return result;
  }
}
