package sr.core.vector;

import java.util.Optional;

import sr.core.Axis;

/** 
 A standard 3-vector, with three spatial components, with no constraints on the component values.
 
  <P>Using an interface was found to be necessary. 
  Different vectors have different requirements.
  For example, the velocity components need to be in the range (-1,1).
  
  <P>Note the following differences with 4-vectors:
  <ul>
   <li>there's no cross product for 4-vectors.
   <li>the 'square' of a 4-vector can be of either sign.
   <li>the idea of time-like, space-like, and light-like vectors. 
   <li>the angle between four-vectors needs reinterpretation: the angle is not a Euclidean angle, but a hyperbolic angle 
   (arc lengths via Minkowski metric), and you it's defined only if the two four-vectors are both time-like or both space-like.
  </ul>
*/
public interface ThreeVector {

  /** The component of the vector along the given spatial-axis. */
  public double on(Axis axis);
  
  /** The component of the vector along the X-axis. */
  public double x();
  
  /** The component of the vector along the Y-axis. */
  public double y();
  
  /** The component of the vector along the Z-axis. */
  public double z();

  /** The vector product of this vector with another vector. */
  public ThreeVector cross(ThreeVector that);

  /** The scalar product of this vector with another vector. */
  public double dot(ThreeVector that);

  /** The scalar product of this vector with itself. */
  public double square();

  /** The magnitude (norm) of the vector. */
  public double magnitude();
  
  /** The angle between this vector and that vector. Range 0..+π. */
  public double angle(ThreeVector that);
  
  /** 
   The angle between this vector and that vector, WITH BOTH VECTORS IN THE X-Y PLANE. 
   Range -π..+π.
   The angle turns 'this' vector into 'that' vector, using the right-hand rule.
   The sign is the same as the sign of the Z-component of <code>this.cross(that)</code>. 
  */
  public double turnsTo(ThreeVector that);
  
  /**
   Returns a non-empty value only in the case where there's exactly 1 non-zero component. 
   This method usually returns the an axis of symmetry in a given context.
   It always returns a spatial axis. 
  */
  public Optional<Axis> axis();

  /** Return a copy of this object, as a new object. */
  public ThreeVector copy();

  /** Replace one component of this three-vector with the given value. Returns a new object. */
  public ThreeVector put(Axis axis, double value);
  
  /** This vector plus 'that' 3-vector (for each component). Returns a new object.*/
  public ThreeVector plus(ThreeVector that);
  
  /** This vector minus 'that' vector (for each component).  Returns a new object.*/
  public ThreeVector minus(ThreeVector that);

  /** Multiply each component by the given scalar. Returns a new object. */
  public ThreeVector times(double scalar);
  
  /** Divide each component by the given (non-zero) scalar. Returns a new object. */
  public ThreeVector divide(double scalar);
  
  /** Return a new vector in the same direction as this vector, but having unit magnitude. */
  public ThreeVector unitVector();
  
}
