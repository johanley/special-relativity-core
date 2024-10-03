package sr.core.hist;

import java.util.LinkedHashMap;
import java.util.Map;

import sr.core.component.Event;

/** 
 Piece together histories in order to make another {@link History}.
 
 <P>The mental model: the history-parts are full histories in themselves.
 But this class 'pays attention' to only one of those histories at a time, according the value of the coordinate-time.
 The history-parts are stitched together using various branch-points (values of <em>ct</em>) to determine which history 
 to use for a given coordinate-time. 
*/
public final class StitchedHistory {

  /** 
   Factory method.
   @param firstLeg of the history. The first leg begins to be valid for <em>ct = -infinity</em>. 
  */
  public static StitchedHistory startingWith(History firstLeg) {
    return new StitchedHistory(firstLeg);
  }

  /**
   Add another section to the history. 
   Each leg must be added in order of increasing coordinate-time for its branch-point.
   @param branchPoint controls which leg is 'active' for a given coordinate-time. 
  */
  public void addTheNext(History leg, Double branchPoint) {
    checkOrder(branchPoint);
    legs.put(branchPoint, leg);
  }
  
  /**  Return a full history whose pieces are the legs passed in previously. */
  public History build() {
    return new History() {
      @Override public Event event(double ct) {
        return legFor(ct).event(ct);
      }
    };
  }

  //LinkedHashMap: iteration order = insertion order; that's important here
  private Map<Double /*branch-point*/, History> legs = new LinkedHashMap<>();
  
  private StitchedHistory(History firstLeg) {
    double infin = Double.MAX_VALUE;
    double initialBranchPoint = -infin;
    this.legs.put(initialBranchPoint, firstLeg); 
  }
  
  private History legFor(double target) {
    History result = null;
    for(Double branchPoint : legs.keySet()) {
      if (target >= branchPoint) {
        result = legs.get(branchPoint);
      }
    }
    return result;
  }
  
  private void checkOrder(Double nextBranchPoint) {
    if (nextBranchPoint <= mostRecentlyAddedBranchPoint()) {
      throw new IllegalArgumentException("Branch-point " + nextBranchPoint + " must come after " + mostRecentlyAddedBranchPoint());
    }
  }
  
  private Double mostRecentlyAddedBranchPoint() {
    Double result = null;
    for(Double branchPoint : legs.keySet() /* assume same order as the map */) {
      result = branchPoint;
    }
    return result;
  }
}
