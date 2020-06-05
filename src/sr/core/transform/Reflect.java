package sr.core.transform;

import sr.core.Parity;

/** Change the direction of axes (including the ct axis). */
public final class Reflect implements CoordTransform {

  /**
   Change the sign of 1 or more components. 
   Includes changing the sign of the ct coord, if desired.
   If a component is unaffected, then just pass {@link Parity#EVEN} for it. 
  */
  public Reflect(Parity ctP, Parity xP, Parity yP, Parity zP) {
    this.ctP = ctP;
    this.xP = xP;
    this.yP = yP;
    this.zP = zP;
  }
  
  @Override public FourVector toNewFrame(FourVector vec) {
    return doIt(vec);
  }
  
  @Override public FourVector toNewVector4(FourVector vecPrime) {
    return doIt(vecPrime);
  }
  
  @Override public String toString() {
    String sep = ",";
    return "[" + ctP+sep+ xP+sep+ yP+sep+ zP + "]";
  }
  
  // PRIVATE
  private Parity ctP;
  private Parity xP;
  private Parity yP;
  private Parity zP;
  
  private FourVector doIt(FourVector vec) {
    FourVector result = FourVector.from(
        vec.ct() * ctP.sign(), 
        vec.x() * xP.sign(), 
        vec.y() * yP.sign(), 
        vec.z() * zP.sign()
      );
    CoordTransform.sameIntervalFromOrigin(vec,  result);
    return result;
  }
}
