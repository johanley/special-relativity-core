package sr.core.hist;

import java.util.LinkedHashMap;
import java.util.Map;

import sr.core.component.NEvent;

/** 
 Piece together histories in order to make another {@link NHistory}.
 
 <P>The mental model: the history-parts are full histories in themselves.
 But this class 'pays attention' to only one of those histories at a time, according the value of the coordinate-time.
 The history-parts are stitched together using various branch-points (values of <em>ct</em>) to determine which history 
 to use for a given coordinate-time. 
*/
public final class NStitchedHistory {

  /** 
   Factory method.
   @param firstLeg of the history. The first leg begins to be valid for <em>ct = -infinity</em>. 
  */
  public static NStitchedHistory startingWith(NHistory firstLeg) {
    return new NStitchedHistory(firstLeg);
  }

  /**
   Add another section to the history. 
   Each leg must be added in order of increasing coordinate-time for its branch-point.
   @param branchPoint controls which leg is 'active' for a given coordinate-time. 
  */
  public void addTheNext(NHistory leg, Double branchPoint) {
    checkOrder(branchPoint);
    legs.put(branchPoint, leg);
  }
  
  /**  Return a full history whose pieces are the legs passed in previously. */
  public NHistory build() {
    return new NHistory() {
      @Override public NEvent event(double ct) {
        return legFor(ct).event(ct);
      }
    };
  }

  //LinkedHashMap: iteration order = insertion order; that's important here
  private Map<Double /*branch-point*/, NHistory> legs = new LinkedHashMap<>();
  
  private NStitchedHistory(NHistory firstLeg) {
    double infin = Double.MAX_VALUE;
    double initialBranchPoint = -infin;
    this.legs.put(initialBranchPoint, firstLeg); 
  }
  
  private NHistory legFor(double target) {
    NHistory result = null;
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
