package sr.core;

/** The axes in space-time. */
public enum Axis {
  
  CT(0), X(1), Y(2), Z(3);

  /** ct-x-y-z map to 0-1-2-3. */
  public int idx() { return idx; }
  
  /** Most operations involve the spatial axes. * */
  public boolean isSpatial() {
    return this != Axis.CT;
  }

  // PRIVATE
  
  private Axis(int idx) {
    this.idx = idx;
  }
  
  private int idx;
}
