package sr.core.transform;

import static java.util.Comparator.comparing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import sr.core.Axis;
import static sr.core.Axis.*;

import sr.core.Util;

/**
 By definition, a 4-vector is an ordered tuple of physical quantities 
 whose parts (1 time-part and 3 space-parts) transform in the same way as the parts of the displacement vector Δx.  
 
 <P>Classical Theory of Fields treats events as the prototype 4-vector:
 <em>"In general a set of four quantities A0, A1, A2, A3, which transform like the components of the radius
 four-vector x_i under transformations of the four-dimensional coordinate system 
 is called a four-dimensional vector (four-vector) A_i."</em>. 
 
 <P>However, the above seems inexact (the affine versus linear issue): it's likely best to regard the displacement Δx<sup>i</sup> as the prototype 4-vector, 
 not the event coordinates x (see <a href='http://www.scholarpedia.org/article/Special_relativity:_mechanics'>Rindler</a>). 
 (The 4-momentum isn't affected by changes to the origin, but x<sup>i</sup> is.)
 
 <P>In this implementation, the components are named ct, x, y, and z.
 For an event, these label distances and times; for any other 4-vector, they simply label the components.
*/
public class FourVector implements Comparable<FourVector> {

  /** All components are 0, and {@link Displace} operations DON'T apply to this object.*/
  public static final FourVector ZERO_LINEAR = FourVector.from(0.0, 0.0, 0.0, 0.0, ApplyDisplaceOp.NO);
  
  /** All components are 0, and {@link Displace} operations DO apply to this object.*/
  public static final FourVector ZERO_AFFINE = FourVector.from(0.0, 0.0, 0.0, 0.0, ApplyDisplaceOp.YES);
  
  /** 
   Factory method.
   Since this library uses units in which c=1 always, note that the 0th coord can be taken 
   either as t, or ct.
  
  <P>If this four-vector is not an event, then the parameter names <code>(ct,x,y,z)</code> represent labels, not values.
  
   <P>If you are working in less than 3 spatial dimensions, then pass 0 for the unused spatial coords (don't pass null).
   @throws RuntimeException if any param is null 
  */
  public static final FourVector from(Double ct, Double x, Double y, Double z, ApplyDisplaceOp applyDisplaceOp) {
    return new FourVector(ct, x, y, z, applyDisplaceOp);
  }
  
  /** Replace one component of this 4-vector, but build a new object in doing so. */
  public FourVector put(Axis axis, Double val) {
    Double[] parts = parts();
    parts[axis.idx()] = val; //apply override to one value
    return new FourVector(parts[CT.idx()], parts[X.idx()], parts[Y.idx()], parts[Z.idx()], this.applyDisplaceOp);
  }
  
  /** Return one component of this 4-vector, corresponding to the given axis. */
  public Double part(Axis axis) {
    return parts()[axis.idx()];
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
    return FourVector.from(ct + that.ct, x + that.x, y + that.y, z + that.z, this.applyDisplaceOp);
  }
  
  /** This 4-vector minus 'that' 4-vector (for each component). */
  public final FourVector minus(FourVector that) {
    return FourVector.from(ct - that.ct, x - that.x, y - that.y, z - that.z, this.applyDisplaceOp);
  }

  /** Multiply each component by the given scalar. */
  public final FourVector multiply(double scalar) {
    return FourVector.from(scalar*ct, scalar*x, scalar*y, scalar*z, this.applyDisplaceOp);
  }
  
  /** Divide each component by the given (non-zero) scalar. */
  public final FourVector divide(double scalar) {
    if (scalar == 0) {
      throw new IllegalArgumentException("Division by 0 not defined.");
    }
    return FourVector.from(ct/scalar, x/scalar, y/scalar, z/scalar, this.applyDisplaceOp);
  }
  
  /** 
   The squared-magnitude of this 4-vector (an invariant quantity).
   This is sometimes called the 'squared-length' of the 4-vector.
   But, since the unit/dimension of the components of a 4-vector aren't fixed, using the term 'length' is misleading. 
   
   <P>Can be positive or negative!
   With the metric signature +--- used by this library (and The Classical Theory of Fields), for 
   time-like 4-vectors the squared-magnitude will be positive. 
   For space-like 4-vectors (less common), it will be negative. 
  */
  public final double magnitudeSq() {
    return scalarProd(this);
  }

  /** 
   The magnitude of this 4-vector (an invariant quantity).
   This is sometimes called the 'length' of the 4-vector.
   But, since the unit/dimension of the components of a 4-vector aren't fixed, using the term 'length' is misleading. 
  
   <P>Does not apply to spacelike vectors!
   Returns a non-negative value only.
  */
  public final double magnitude() {
    if (FourVectorType.isSpacelike(this)) {
      throw new RuntimeException("Cannot find magnitude, since 4-vector is spacelike. Its mag-squared is negative, so its mag is an imaginary number.");
    }
    return Math.sqrt(scalarProd(this));
  }
  
  /** Magnitude (not squared) of this 4-vector's spatial part (its 3-vector). Always positive. */
  public final double spatialMagnitude() {
    return Math.sqrt(x*x + y*y + z*z); 
  }
  
  /** 
   Dot-product of this 4-vector's spatial part (its 3-vector) with the spatial parts of 'that'. 
   Always positive. 
  */
  public final double spatialScalarProduct(FourVector that) {
    return x*that.x + y*that.y + z*that.z; 
  }
  
  /** The region of space-time towards which this 4-vector is directed. */
  public final FourVectorType vectorType() {
    return FourVectorType.of(this);
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
  /**  {@link Displace} operations only affect 4-vectors for which this method returns YES.  */
  public final ApplyDisplaceOp applyDisplaceOp() { return applyDisplaceOp; }
  
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
  
  @Override public String toString() {
    String sep = ", ";
    return "[" + round(ct)+sep+ round(x)+sep+ round(y)+sep+ round(z)+sep+ applyDisplaceOp + "]";
  }
  
  public final String toStringNoRounding() {
    String sep = ", ";
    return "[" + ct+sep+ x+sep+ y+sep+ z+sep + applyDisplaceOp+ "]";
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
  private ApplyDisplaceOp applyDisplaceOp;

  private FourVector(Double ct, Double x, Double y, Double z, ApplyDisplaceOp applyDisplaceOp) {
    this.ct = ct;
    this.x = x;
    this.y = y;
    this.z = z;
    this.applyDisplaceOp = applyDisplaceOp;
    validate();
  }
  
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
    checkIfNull(applyDisplaceOp, "applyDisplaceOp", errors);
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
  
  private Double[] parts(){
    Double[] parts = {ct, x, y, z};
    return parts;
  }
}
