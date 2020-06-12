package sr.core.transform;

/**
 Add a fixed amount to 1 or more components.
 If the displacement is for one component only, then just pass 0 for the remaining ones.
 
 <p>IMPORTANT: most 4-vectors should NOT react to this transform, because they are 
 differential, and aren't affected by changes to the origin.
 See {@link ApplyDisplaceOp}.
 
 <P>Also note that this transform is the only one that:
 <ul>
  <li>can't be represented as a matrix
  <li>uses numbers that have dimensions
 </ul>
*/
public final class Displace implements CoordTransform {
  
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
  
  /*** Displace the origin to the given FourVector. */
  public static CoordTransform originTo(FourVector vec) {
    return new Displace(-vec.ct(), -vec.x(), -vec.y(), -vec.z());
  }
  
  @Override public FourVector toNewFrame(FourVector vec) {
    return doIt(vec, WhichDirection.NOMINAL);
  }
  
  @Override public FourVector toNewFourVector(FourVector vecPrime) {
    return doIt(vecPrime, WhichDirection.INVERSE);
  }
  
  @Override public String toString() {
    String sep = ",";
    return "displace[" + ctD+sep+ xD+sep+ yD+sep+ zD + "]";
  }

  // PRIVATE 
  
  private double ctD;
  private double xD;
  private double yD;
  private double zD;
  
  private FourVector doIt(FourVector vec, WhichDirection dir) {
    FourVector result = vec;
    if (ApplyDisplaceOp.YES == vec.applyDisplaceOp()) {
      int sign = dir.sign();
      result = FourVector.from(
        vec.ct() + ctD * sign, 
        vec.x() + xD * sign, 
        vec.y() + yD * sign, 
        vec.z() + zD * sign,
        ApplyDisplaceOp.YES
      );
    }
    else {
      //System.out.println("Not applying.");
    }
    return result;
 }
}