package sr.core;

/** The axes in space-time. */
public enum Axis {
  
  CT(0), X(1), Y(2), Z(3);

  /** Used to map to the parts of a 4-vector (order: ct-x-y-z). */
  public int idx() { return idx; }

  // PRIVATE 
  private Axis(int idx) {
    this.idx = idx;
  }
  private int idx;
}
