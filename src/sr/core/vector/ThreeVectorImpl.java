package sr.core.vector;

import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;
import static sr.core.Util.sqroot;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import sr.core.Axis;
import sr.core.Parity;
import sr.core.Rotation;
import sr.core.Util;
import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.CoordTransform;
import sr.core.transform.FourVector;
import sr.core.transform.Reflect;
import sr.core.transform.Rotate;

/** 
 A standard 3-vector, with three spatial components, with no constraints on the component values.
 
 (Note: the javadoc for the {@link ThreeVector} interface methods is inherited in the interface.)
 
 <P>This class is <em>final</em>, and cannot be subclassed.
 It's meant to be used as a field, to which a class can 'call-forward' most of its methods to this class.
 
 <P>If it was directly subclassed, then that would be dangerous: if new methods needed to be added to 
 the subclass, then it would no longer be a pure Vector. 
 The 'call-forward' technique, on the other hand, is always safe.
*/
public final class ThreeVectorImpl implements ThreeVector {

  /** Factory method. */
  public static ThreeVectorImpl of(double xComp, double yComp, double zComp) {
    return new ThreeVectorImpl(xComp, yComp, zComp);
  }
  
  /** Factory method. The vector has 1 non-zero component, along the given coordinate axis. */
  public static ThreeVectorImpl of(Axis axis, double value) {
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
    return components.get(axis);
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
  
  @Override public double dot(ThreeVector that) {
    double result = 0.0;
    for (Axis axis : components.keySet()) {
      result = result + on(axis) * that.on(axis); 
    }
    return result;
  }
  
  @Override public ThreeVector cross(ThreeVector that) {
    double x = on(Y) * that.on(Z) - on(Z)*that.on(Y);
    double y = on(X) * that.on(Z) - on(Z)*that.on(X);
    double z = on(X) * that.on(Y) - on(Y)*that.on(X);
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

  /** This 3-vector plus 'that' 3-vector (for each component). Returns a new Vector.*/
  @Override public ThreeVector plus(ThreeVector that) {
    return ThreeVectorImpl.of(
      on(X) + that.on(X), 
      on(Y) + that.on(Y),
      on(Z) + that.on(Z)
    );
  }
  
  @Override public ThreeVector minus(ThreeVector that) {
    return ThreeVectorImpl.of(
      on(X) - that.on(X), 
      on(Y) - that.on(Y),
      on(Z) - that.on(Z)
    );
  }

  @Override public ThreeVector multiply(double scalar) {
    return ThreeVectorImpl.of(
      on(X) * scalar, 
      on(Y) * scalar, 
      on(Z) * scalar 
    );
  }
  
  @Override public ThreeVector divide(double scalar) {
    Util.mustHave(scalar != 0, "Cannot divide by zero.");
    return ThreeVectorImpl.of(
      on(X) / scalar, 
      on(Y) / scalar, 
      on(Z) / scalar 
    );
  }
  
  @Override public  ThreeVector rotation(Rotation rotation) {
    //this implementation uses items build for 4-vectors; the extra dimension is simply ignored in the result
    CoordTransform rotate = Rotate.about(rotation.axis, rotation.θ);
    FourVector a = FourVector.from(0.0, on(X), on(Y), on(Z), ApplyDisplaceOp.NO);
    FourVector b = rotate.toNewFourVector(a);
    return ThreeVectorImpl.of(b.x(),b.y(),b.z());
  }
  
  @Override public ThreeVector reflection() {
    CoordTransform reflection = new Reflect(Parity.EVEN, Parity.ODD, Parity.ODD, Parity.ODD);
    FourVector a = FourVector.from(0.0, on(X), on(Y), on(Z), ApplyDisplaceOp.NO);
    FourVector b = reflection.toNewFourVector(a);
    return ThreeVectorImpl.of(b.x(), b.y(), b.z());
  }
  
  @Override public ThreeVector reflection(Axis axis) {
    CoordTransform reflection = Reflect.the(axis);
    FourVector a = FourVector.from(0.0, on(X), on(Y), on(Z), ApplyDisplaceOp.NO);
    FourVector b = reflection.toNewFourVector(a);
    return ThreeVectorImpl.of(b.x(), b.y(), b.z());
  }
  
  //PRIVATE 
  
  private Map</*spatial*/Axis, Double> components = new LinkedHashMap<>();
  
  private ThreeVectorImpl(double xComp, double yComp, double zComp) {
    this.components.put(X, xComp);
    this.components.put(Y, yComp);
    this.components.put(Z, zComp);
  }
  
  private ThreeVectorImpl(Axis axis, double value) {
    this(0.0, 0.0, 0.0);
    this.components.put(axis, value);
  }

}
