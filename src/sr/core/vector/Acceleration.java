package sr.core;

import java.util.Optional;

import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.FourVector;
import static sr.core.Axis.*;

/** 
 The 3-acceleration of an object having mass (not a 4-vector).
*/
public final class Acceleration implements ThreeVector {

  /** Factory method, taking the 3 components of the acceleration along the XYZ axes, in that order.  */
  public static Acceleration of(double ax, double ay, double az) {
    return new Acceleration(ax, ay, az);
  }
  
  /** Factory method for the case in which the acceleration is parallel to a coordinate axis. */
  public static Acceleration of(Axis axis, double ai) {
    return new Acceleration(axis, ai);
  }
  
  public static Acceleration zero() {
    return Acceleration.of(0.0, 0.0, 0.0);
  }
  
  /** Some cases only make sense when the acceleration is non-zero. */
  public static Acceleration nonZero(double ax, double ay, double az) {
    Acceleration result = Acceleration.of(ax, ay, az);
    Util.mustHave(result.magnitude() > 0, "Vector should have a non-zero magnitude.");
    return result;
  }
  
  /** 
   The 4-acceleration corresponding to this object, for the given {@link Velocity}.
   Always space-like (or zero). Always orthogonal to the 4-velocity! 
  */
  public FourVector fourAcceleration(Velocity vel) {
    //Reference https://en.wikipedia.org/wiki/Acceleration_(special_relativity)  (and others)
    double g4 = Math.pow(vel.Γ(), 4);
    double g2 = Math.pow(vel.Γ(), 2);
    double dot = this.dot(vel);
    double ct = g4 * dot;
    ThreeVector space = vel.multiply(g4).multiply(dot).plus(this.multiply(g2));
    return FourVector.from(ct, space.on(X), space.on(Y), space.on(Z), ApplyDisplaceOp.NO);
  }

  @Override public double on(Axis axis) {
    return vec.on(axis);
  }

  @Override public Optional<Axis> axis(){
    return vec.axis();
  }
  
  @Override public double dot(ThreeVector that) {
    return vec.dot(that);
  }
  
  @Override public ThreeVector cross(ThreeVector that) {
    return vec.cross(that);
  }
  
  @Override public double angle(ThreeVector that) {
    return vec.angle(that);
  }

  @Override public double square() {
    return vec.square();
  }

  @Override public double magnitude() {
    return vec.magnitude();  
  }

  @Override public ThreeVector plus(ThreeVector that) {
    return vec.plus(that);
  }
  
  @Override public ThreeVector minus(ThreeVector that) {
    return vec.minus(that);
  }

  @Override public ThreeVector multiply(double scalar) {
    return vec.multiply(scalar);
  }
  
  @Override public ThreeVector divide(double scalar) {
    return vec.divide(scalar);
  }
  
  @Override public  ThreeVector rotation(Rotation rotation) {
    return vec.rotation(rotation);
  }
  
  @Override public ThreeVector reflection() {
    return vec.reflection();
  }
  
  @Override public ThreeVector reflection(Axis axis) {
    return vec.reflection(axis);
  }
  
  //PRIVATE 
  
  private ThreeVectorImpl vec;
  
  private Acceleration(double xComp, double yComp, double zComp) {
    vec = ThreeVectorImpl.of(xComp, yComp, zComp);
    check();
  }
  
  private Acceleration(Axis axis, double value) {
    vec = ThreeVectorImpl.of(axis, value);
    check();
  }
  
  /** Validations on incoming constructor data. */
  private void check() {
    //should anything go here?
  }
}
