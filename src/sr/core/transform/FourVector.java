package sr.core.transform;

import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import sr.core.Direction;
import sr.core.Util;

/**
 By definition, a 4-vector is an ordered tuple of physical quantities 
 whose parts (1 time-part and 3 space-parts) obey the Lorentz Transformation (and 
 other transformations as well).
 
 <P>Classical Theory of Fields treats events as the prototype 4-vector:
 <em>"In general a set of four quantities A0, A1, A2, A3, which transform like the components of the radius
 four-vector x_i under transformations of the four-dimensional coordinate system 
 is called a four-dimensional vector (four-vector) A_i."</em>
 
 <P>In this implementation, the components are named ct, x, y, and z.
 For an event, these label distances and times; for any other 4-vector, they simply label the components.
*/
public class FourVector implements Comparable<FourVector> {

  /** All components are 0. */
  public static final FourVector ZERO = FourVector.from(0.0, 0.0, 0.0, 0.0);
  
  /**
   Constructor. 
   Since this library uses units in which c=1 always, note that the 0th coord can be taken 
   either as t, or ct.
  
   <P>If you are working in less than 3 spatial dimensions, then pass 0 for the unused spatial coords (don't pass null).
   @throws RuntimeException if any param is null 
  */
  public FourVector(Double ct, Double x, Double y, Double z) {
    this.ct = ct;
    this.x = x;
    this.y = y;
    this.z = z;
    validate();
  }
  
  /** Factory method version of the constructor.  */
  public static final FourVector from(Double ct, Double x, Double y, Double z) {
    return new FourVector(ct, x, y, z);
  }
  
  /** 
   The scalar product of this 4-vector with another 4-vector (an invariant quantity).
   The two 4-vectors can represent different physical quantities (speed and acceleration, for example).
   Returns positive and negative values. 
  */
  public final double scalarProd(FourVector that) {
    return ct*that.ct - x*that.x - y*that.y - z*that.z; 
  }
  
  /** This 4-vector plus 'that' 4-vector (for each component). */
  public final FourVector plus(FourVector that) {
    return FourVector.from(ct + that.ct, x + that.x, y + that.y, z + that.z);
  }
  
  /** This 4-vector minus 'that' 4-vector (for each component). */
  public final FourVector minus(FourVector that) {
    return FourVector.from(ct - that.ct, x - that.x, y - that.y, z - that.z);
  }

  /** Multiply each component by the given scalar. */
  public final FourVector multiply(double scalar) {
    return FourVector.from(scalar*ct, scalar*x, scalar*y, scalar*z);
  }
  
  /** Divide each component by the given (non-zero) scalar. */
  public final FourVector divide(double scalar) {
    if (scalar == 0) {
      throw new IllegalArgumentException("Division by 0 not defined.");
    }
    return FourVector.from(ct/scalar, x/scalar, y/scalar, z/scalar);
  }
  
  /** 
   The squared-magnitude of this 4-vector (an invariant quantity).
   This is sometimes called the 'length' of the 4-vector.
   Since the unit/dimension of the components of a 4-vector aren't fixed, using the term 'length' is misleading. 
   
   Can be positive or negative!
   With the metric signature +--- used by this library (and the Classical Theory of Fields), for 
   time-like 4-vectors the squared-magnitude will be positive. 
   For space-like 4-vectors (less common), it will be negative. 
  */
  public final double magnitudeSq() {
    return scalarProd(this);
  }

  /** The region of space-time towards which this 4-vector is directed. */
  public final Direction direction() {
    return Direction.of(this);
  }

  /** Magnitude (not squared) of this 4-vector's spatial part (its 3-vector). Always positive. */
  public final double spatialMagnitude() {
    return Math.sqrt(x*x + y*y + z*z); 
  }
  
  /** The time component. */
  public final Double ct() { return ct; }
  /** Synonym for ct, since this library uses c=1 only.*/
  public final Double t() { return ct; }
  /** The spatial component along the X-axis. */
  public final Double x() { return x; }
  /** The spatial component along the Y-axis. */
  public final Double y() { return y; }
  /** The spatial component along the Z-axis. */
  public final Double z() { return z; }
  
  /** WARNING: this implementation makes no allowance for rounding errors (which are often significant).  */
  @Override final public boolean equals(Object aThat) {
    //unusual: multiple return statements
    if (this == aThat) return true;
    if (!(aThat instanceof FourVector)) return false;
    FourVector that = (FourVector)aThat;
    for(int i = 0; i < this.getSigFields().length; ++i){
      if (!Objects.equals(this.getSigFields()[i], that.getSigFields()[i])){
        return false; 
      }
    }
    return true;
  }    
  
  /** This method allows for rounding errors (which are often significant), and is usually recommended instead of {@link #equals(Object)}. */
  public final boolean equalsWithEpsilon(FourVector that) {
    if (this == that) return true;
    for(int i = 0; i < this.getSigFields().length; ++i){
      if (!Util.equalsWithEpsilon(this.getSignificantFields()[i], that.getSignificantFields()[i])){
        return false; 
      }
    }
    return true;
  }
  
  @Override final public int hashCode() {
    return Objects.hash(getSigFields());
  }
  
  /** Debugging only. */
  @Override public String toString() {
    String sep = ", ";
    return "[" + ct+sep+ x+sep+ y+sep+ z + "]";
  }
  
  /** Debugging only. */
  public final String toStringRounded() {
    String sep = ", ";
    return "[" + round(ct)+sep+ round(x)+sep+ round(y)+sep+ round(z) + "]";
  }

  /** Sorts by the time-component first, then by x-y-z. */
  @Override final public int compareTo(FourVector that) {
    return COMPARATOR.compare(this, that);
  }
  
  // PRIVATE

  private Double ct;
  private Double x;
  private Double y;
  private Double z;

  private Object[] getSigFields() {
    //optimize: start with items that are most likely to differ
    return new Object[] {ct, x, y, z};
  }
  
  private Double[] getSignificantFields() {
    //optimize: start with items that are most likely to differ
    return new Double[] {ct, x, y, z};
  }

  /** Static: avoid creating this object every time a comparison is made.*/
  private static Comparator<FourVector> COMPARATOR = getComparator();  
  
  /**
   Note the 'thenComparing' chain: when comparing, the implementation goes 
   to the next level of comparison ONLY IF the previous level has 
   returned '0' (equal).
  */
  private static Comparator<FourVector> getComparator(){
    //use the same fields used by the equals method, if at all possible!
    Comparator<FourVector> result = 
      comparing(FourVector::ct)
      .thenComparing(FourVector::x)
      .thenComparing(FourVector::y)
      .thenComparing(FourVector::z)
    ;
    return result;
  }
  
  private void validate() throws RuntimeException {
    List<String> errors = new ArrayList<>();
    checkIfNull(ct, "ct", errors);    
    checkIfNull(x, "x", errors);    
    checkIfNull(y, "y", errors);    
    checkIfNull(z, "z", errors);
    if (!errors.isEmpty()) {
      RuntimeException ex = new RuntimeException("4-vector not valid.");
      for(String error: errors) {
        ex.addSuppressed(new RuntimeException(error));
      }
      throw ex;
    }    
  }
  
  private void checkIfNull(Object field, String errorMsg, List<String> errors) {
    if (field == null) {
      errors.add(errorMsg + " is null");
    }
  }
  
  private double round(Double val) {
    return Util.round(val, 4);
  }
}
