package sr.core.event.transform;

import sr.core.event.Event;

/**
 Add a fixed amount to 1 or more components.
 If the displacement is for one component only, then just pass 0 for the remaining ones.
 
 <P>Note that this transform is the only one that uses numbers that have dimensions.
 <P>Also note that 4-vectors in general are differential and not affected by a displacement operation.
*/
public final class Displace implements Transform {
  
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
  public static Transform originTo(Event vec) {
    return new Displace(-vec.ct(), -vec.x(), -vec.y(), -vec.z());
  }
  
  @Override public Event apply(Event vec) {
    return doIt(vec, +1);
  }
  
  @Override public Event reverse(Event vecPrime) {
    return doIt(vecPrime, -1);
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
  
  private Event doIt(Event vec, int sign) {
    Event result = vec;
    result = Event.of(
      vec.ct() + ctD * sign, 
      vec.x() + xD * sign, 
      vec.y() + yD * sign, 
      vec.z() + zD * sign
    );
    return result;
 }
}