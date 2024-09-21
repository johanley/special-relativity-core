package sr.core.vector3;

import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;
import static sr.core.Util.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import sr.core.Axis;
import sr.core.Util;

/** 
 A standard 3-vector, with three spatial components, with no constraints on the component values.
 
 (Note: the javadoc for the {@link ThreeVector} interface methods is inherited in the interface.)
 
 <P>There are valid reasons for creating other classes that either implement the interface or subclass this class:
 <ul>
  <li>providing a more meaningful name, which clarifies the caller
  <li>providing a means to validate data coming into the constructor
  <li>pseudo-vectors have distinct behaviour under reflection
 </ul>
 
 <P>If desired, classes that implement this interface can 'call-forward' all (non-constructor) methods to this implementation.
 This provides a way of avoid the <em>extends</em> keyword.
*/
public class ThreeVectorImpl implements ThreeVector {

  /** Factory method. */
  public static ThreeVectorImpl of(double x, double y, double z) {
    return new ThreeVectorImpl(x, y, z);
  }
  
  /** Factory method. The vector has 1 non-zero component, along the given spatial coordinate axis. */
  public static ThreeVectorImpl of(Axis axis, double value) {
    mustBeSpatial(axis);
    return new ThreeVectorImpl(axis, value);
  }
  
  /** A vector having all components 0.0. */
  public static ThreeVectorImpl zero() {
    return ThreeVectorImpl.of(0.0, 0.0, 0.0);
  }
  
  /** Some cases only make sense when the magnitude is non-zero. */
  public static ThreeVectorImpl nonZero(double x, double y, double z) {
    ThreeVectorImpl result = ThreeVectorImpl.of(x, y, z);
    Util.mustHave(result.magnitude() > 0, "Vector should have a non-zero magnitude.");
    return result;
  }

  @Override public double on(Axis axis) {
    mustBeSpatial(axis);
    return components.get(axis);
  }
  
  @Override public double x() {
    return on(X);
  }
  
  @Override public double y() {
    return on(Y);
  }
  
  @Override public double z() {
    return on(Z);
  }

  @Override public Optional<Axis> axis(){
    Optional<Axis> result = Optional.empty();
    int nonZeros = 0;
    for(Axis axis : components.keySet()) {
      if (components.get(axis) != 0.0) {
        ++nonZeros; 
      }
    }
    if (nonZeros == 1) {
      for(Axis axis : components.keySet()) {
        if (components.get(axis) != 0.0) {
          result = Optional.of(axis);
        }
      }
    }
    return result;
  }
  
  @Override public ThreeVector copy() {
    Map<Axis, Double> copy = new LinkedHashMap<>();
    copy.putAll(components);
    return ThreeVectorImpl.of(
      copy.get(X), 
      copy.get(Y), 
      copy.get(Z) 
    );
  }
  
  @Override public ThreeVector put(Axis axis, double value) {
    mustBeSpatial(axis);
    Map<Axis, Double> updated = new LinkedHashMap<>();
    updated.putAll(components);
    updated.put(axis, value);
    return ThreeVectorImpl.of(
      updated.get(X), 
      updated.get(Y), 
      updated.get(Z) 
    );
  }
  
  @Override public double dot(ThreeVector that) {
    double result = 0.0;
    for (Axis axis : components.keySet()) {
      result = result + on(axis) * that.on(axis); 
    }
    return result;
  }
  
  @Override public ThreeVector cross(ThreeVector that) {
    //https://en.wikipedia.org/wiki/Cross_product
    double x = y() * that.z() - z() * that.y();
    double y = z() * that.x() - x() * that.z();
    double z = x() * that.y() - y() * that.x();
    return ThreeVectorImpl.of(x, y, z);
  }

  @Override public double square() {
    return dot(this);
  }

  @Override public double magnitude() {
    return sqroot(square());  
  }

  @Override public double angle(ThreeVector that) {
    double numerator = dot(that);
    double denominator = magnitude() * that.magnitude();
    return Math.acos(numerator / denominator);
  }
  
  @Override public double turnsTo(ThreeVector that) {
    Util.mustHave(this.z() == 0, "This vector is not in the XY plane: " + this);
    Util.mustHave(that.z() == 0, "That vector is not in the XY plane: " + that);
    double result = angle(that);
    int sign = Util.sign(this.cross(that).z());
    return result * sign;
  }

  @Override public ThreeVector plus(ThreeVector that) {
    return ThreeVectorImpl.of(
      x() + that.x(), 
      y() + that.y(),
      z() + that.z()
    );
  }
  
  @Override public ThreeVector minus(ThreeVector that) {
    return ThreeVectorImpl.of(
      x() - that.x(), 
      y() - that.y(),
      z() - that.z()
    );
  }

  @Override public ThreeVector times(double scalar) {
    return ThreeVectorImpl.of(
      x() * scalar, 
      y() * scalar, 
      z() * scalar 
    );
  }
  
  @Override public ThreeVector divide(double scalar) {
    Util.mustHave(scalar != 0, "Cannot divide by zero.");
    return ThreeVectorImpl.of(
      x() / scalar, 
      y() / scalar, 
      z() / scalar 
    );
  }
  
  @Override public ThreeVector unitVector() {
    return this.divide(magnitude());
  }
  
  /** This implementation applies rounding. */
  @Override public String toString() {
    String sep = ",";
    return "[" + 
      roundIt(x()) + sep + 
      roundIt(y()) + sep + 
      roundIt(z()) + 
    "]" ;
  }

  /** Constructors are protected, in order to be visible to subclasses. */
  protected ThreeVectorImpl(double xComp, double yComp, double zComp) {
    this.components.put(X, xComp);
    this.components.put(Y, yComp);
    this.components.put(Z, zComp);
  }
  
  protected ThreeVectorImpl(Axis axis, double value) {
    this(0.0, 0.0, 0.0);
    this.components.put(axis, value);
  }
  
  protected ThreeVectorImpl(double magnitude, Direction direction) {
    this(
      magnitude * direction.x(), 
      magnitude * direction.y(), 
      magnitude * direction.z()
    );
  }

  private Map</*spatial*/Axis, Double> components = new LinkedHashMap<>();
  
  private double roundIt(double value) {
    return Util.round(value, 5);
  }
}
