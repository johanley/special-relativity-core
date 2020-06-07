package sr.core.transform;

/**
 Add a fixed amount to 1 or more components.
 If the displacement is for one component only, then just pass 0 for the remaining ones.
*/
public final class Displace implements CoordTransform {
  
  /** 
   Returns true. 
   Displacement operations are the only operation to change {@link FourVector#ZERO}.
   (In mathematical terms, displacements represent the difference between linear and affine transformations.)
  */
  @Override public boolean changesOrigin() {
    return true;
  }

  /** 
   Constructor. 
   Pass the displacement to be ADDED to each coord.
   Simply pass 0 if the coord is left unaffected.
  */
  public Displace(double ctD, double xD, double yD, double zD) {
    this.ctD = ctD;
    this.xD = xD;
    this.yD = yD;
    this.zD = zD;
  }
  
  /*** Displace the origin to the given Vector4. */
  public static CoordTransform originTo(FourVector vec) {
    return new Displace(-vec.ct(), -vec.x(), -vec.y(), -vec.z());
  }
  
  @Override public FourVector toNewFrame(FourVector vec) {
    return doIt(vec, WhichDirection.NOMINAL);
  }
  
  @Override public FourVector toNewVector4(FourVector vecPrime) {
    return doIt(vecPrime, WhichDirection.INVERSE);
  }
  
  @Override public String toString() {
    String sep = ",";
    return "[" + ctD+sep+ xD+sep+ yD+sep+ zD + "]";
  }

  // PRIVATE 
  
  private double ctD;
  private double xD;
  private double yD;
  private double zD;
  
  private FourVector doIt(FourVector vec, WhichDirection dir) {
    int sign = dir.sign();
    return FourVector.from(
      vec.ct() + ctD * sign, 
      vec.x() + xD * sign, 
      vec.y() + yD * sign, 
      vec.z() + zD * sign
    );
  }
}