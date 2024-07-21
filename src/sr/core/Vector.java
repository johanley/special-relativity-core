package sr.core;

import java.util.Optional;

/** 
 A standard 3-vector, with three spatial components, with no constraints on the component values.
 In this project, 'vector' always implies a 3-vector.
 
  <P>Using an interface was found to be necessary. 
  Different vectors have different requirements.
  For example, the velocity components need to be in the range (-1,1).
  
  <P>Four-vectors have some essential differences with three-vectors:
  <ul>
   <li>four-vectors have no cross product
   <li>the angle between four-vectors needs reinterpretation: the angle is not a Euclidean angle, but a hyperbolic angle (arc lengths via Minkowski metric), and you it's defined only if the
   two four-vectors are both time-like or both space-like.
  </ul>
*/
public interface Vector {

  /** The component of the vector along the given spatial-axis. */
  public double on(Axis axis);

  /** The scalar product of this vector with another vector. */
  public double dot(Vector that);

  /** The vector product of this vector with another vector. */
  public Vector cross(Vector that);
  
  /** The angle between this vector and that vector. Range 0..π. */
  public double angle(Vector that);
  
  /** The squared-magnitude  of the vector. (Note that 4-vectors have no cross-product!) */
  public double square();

  /** The magnitude (norm) of the vector. */
  public double magnitude();
  
  /**
   Returns a non-empty value only in the case where there's exactly 1 non-zero component. 
   This method usually returns the an axis of symmetry in a given context.
   It always returns a spatial axis. 
  */
  public Optional<Axis> axis();

  /** This vector plus 'that' 3-vector (for each component). Returns a new object.*/
  public Vector plus(Vector that);
  
  /** This vector minus 'that' vector (for each component).  Returns a new object.*/
  public Vector minus(Vector that);

  /** Multiply each component by the given scalar. Returns a new object. */
  public Vector multiply(double scalar);
  
  /** Divide each component by the given (non-zero) scalar. Returns a new object. */
  public Vector divide(double scalar);
  
  /** Return a new vector whose components have been rotated about an axis of the coordinate system. */
  public Vector rotation(Rotation rotation);
  
  /** Return a new vector whose components have all been multiplied by -1. */
  public Vector reflection();
  
  /** Return a new vector for which a single component (for the given Axis) is multiplied by -1. */
  public Vector reflection(Axis axis);
  
}
