package sr.core.vec3;

import static sr.core.Util.mustBeSpatial;
import static sr.core.Util.mustHave;
import static sr.core.Util.sqroot;

import java.util.Optional;

import sr.core.Axis;
import sr.core.Util;
import sr.core.component.NComponents;

/** 
A standard 3-vector, with three spatial components, with no constraints on the component values.

<P>Most, but not all, methods are final.

 <P>Note the following differences with 4-vectors:
 <ul>
  <li>there's no cross product for 4-vectors.
  <li>the 'square' of a 4-vector can be of either sign.
  <li>the idea of time-like, space-like, and light-like vectors applies only to 4-vectors 
  <li>the angle between four-vectors needs reinterpretation: the angle is not a Euclidean angle, but a hyperbolic angle 
  (arc lengths via Minkowski metric), and you it's defined only if the two four-vectors are both time-like or both space-like.
 </ul>
*/
public class NThreeVector {

  /** Factory method. */
  public static NThreeVector of(double x, double y, double z) {
    return new NThreeVector(x, y, z);
  }
  
  /** Factory method. The vector has 1 non-zero component, along the given spatial coordinate axis. */
  public static NThreeVector of(double magnitude, Axis axis) {
    mustBeSpatial(axis);
    return new NThreeVector(magnitude, axis);
  }
  
  /** A vector having all components 0.0. */
  public static NThreeVector zero() {
    return NThreeVector.of(0.0, 0.0, 0.0);
  }
  
  /** Some cases only make sense when the magnitude is non-zero. */
  public static NThreeVector nonZero(double x, double y, double z) {
    NThreeVector result = NThreeVector.of(x, y, z);
    Util.mustHave(result.magnitude() > 0, "Vector should have a non-zero magnitude.");
    return result;
  }

  /** The component of the vector along the given spatial-axis. */
  public final double on(Axis axis) {
    Util.mustBeSpatial(axis);
    return components.on(axis); 
  }
  /** The component of the vector along the X-axis. */
  public final double x() { return components.x(); }
  
  /** The component of the vector along the Y-axis. */
  public final double y() { return components.y(); }
  
  /** The component of the vector along the Z-axis. */
  public final double z() { return components.z(); }
  
  /** Replace one component of this three-vector with the given value on one spatial axis. Returns a new object. */
  public final NThreeVector overwrite(Axis axis, double value) {
    mustBeSpatial(axis);
    NComponents comps = components.overwrite(axis, value);
    return NThreeVector.of(
      comps.x(), 
      comps.y(), 
      comps.z() 
    );
  }
  
  /** The scalar product of this vector with another vector. */
  public final double dot(NThreeVector that) {
    double result = 0.0;
    for (Axis axis : components.axes()) {
      result = result + on(axis) * that.on(axis); 
    }
    return result;
  }
  
  /** The vector product of this vector with another vector. */
  public final NThreeVector cross(NThreeVector that) {
    //https://en.wikipedia.org/wiki/Cross_product
    double x = y() * that.z() - z() * that.y();
    double y = z() * that.x() - x() * that.z();
    double z = x() * that.y() - y() * that.x();
    return NThreeVector.of(x, y, z);
  }
  
  /** The scalar product of this vector with itself. */
  public final double square() {
    return dot(this);
  }

  /** The magnitude (norm) of the vector. */
  public final double magnitude() {
    return sqroot(square());  
  }

  /** The angle between this vector and that vector. Range 0..+π. */
  public final double angle(NThreeVector that) {
    double numerator = dot(that);
    double denominator = magnitude() * that.magnitude();
    return Math.acos(numerator / denominator);
  }
  
  /** 
   The angle between this vector and that vector, WITH BOTH VECTORS IN THE X-Y PLANE. 
   Range -π..+π.
   The angle turns 'this' vector into 'that' vector, using the right-hand rule.
   The sign is the same as the sign of the Z-component of <code>this.cross(that)</code>. 
  */
  public final double turnsTo(NThreeVector that) {
    Util.mustHave(this.z() == 0, "This vector is not in the XY plane: " + this);
    Util.mustHave(that.z() == 0, "That vector is not in the XY plane: " + that);
    double result = angle(that);
    int sign = Util.sign(this.cross(that).z());
    return result * sign;
  }
  
  /**
   Returns a non-empty value only in the case where there's exactly 1 non-zero component. 
   This method usually returns the an axis of symmetry in a given context.
   It always returns a spatial axis. 
  */
  public final Optional<Axis> axis(){
    Optional<Axis> result = Optional.empty();
    int nonZeros = 0;
    for(Axis axis : components.axes()) {
      if (components.on(axis) != 0.0) {
        ++nonZeros; 
      }
    }
    if (nonZeros == 1) {
      for(Axis axis : components.axes()) {
        if (components.on(axis) != 0.0) {
          result = Optional.of(axis);
        }
      }
    }
    return result;
  }
  
  /** This vector plus 'that' 3-vector (for each component). Returns a new object. */
  public final NThreeVector plus(NThreeVector that) {
    return NThreeVector.of(
      x() + that.x(), 
      y() + that.y(),
      z() + that.z()
    );
  }
  
  /** This vector minus 'that' vector (for each component).  Returns a new object. */
  public final NThreeVector minus(NThreeVector that) {
    return NThreeVector.of(
      x() - that.x(), 
      y() - that.y(),
      z() - that.z()
    );
  }
 
  /** Multiply each component by the given scalar. Returns a new object. */
  public final NThreeVector times(double scalar) {
    return NThreeVector.of(
      x() * scalar, 
      y() * scalar, 
      z() * scalar 
    );
  }
  
  /** Divide each component by the given (non-zero) scalar. Returns a new object. */
  public final NThreeVector divide(double scalar) {
    mustHave(scalar != 0, "Cannot divide by zero.");
    return NThreeVector.of(
      x() / scalar, 
      y() / scalar, 
      z() / scalar 
    );
  }
  
  /** Return a new vector in the same direction as this vector, but having unit magnitude. */
  public final NThreeVector unitVector() {
    return this.divide(magnitude());
  }
  
  /** This implementation applies rounding. */
  @Override public final String toString() {
    String sep = ",";
    return "[" + 
      roundIt(x()) + sep + 
      roundIt(y()) + sep + 
      roundIt(z()) + 
    "]" ;
  }
  
  protected NComponents components;
  
  /** Constructors are protected, in order to be visible to subclasses. */
  protected NThreeVector(double xComp, double yComp, double zComp) {
    this.components = NComponents.of(xComp, yComp, zComp);
  }
  
  protected NThreeVector(double value, Axis axis) {
    this(0.0, 0.0, 0.0);
    this.components = components.overwrite(axis, value);
  }
  
  protected NThreeVector(double magnitude, NDirection direction) {
    this(
      magnitude * direction.x(), 
      magnitude * direction.y(), 
      magnitude * direction.z()
    );
  }
  
  protected NThreeVector(NComponents components) {
    this.components = components;
  }

  private double roundIt(double value) {
    return Util.round(value, 5);
  }
}