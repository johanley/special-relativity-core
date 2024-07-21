package sr.core;

import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;

import java.util.Optional;

import sr.core.transform.ApplyDisplaceOp;
import sr.core.transform.FourVector;

/** The position of an object in space. */
public final class Position implements ThreeVector {

  /** Factory method, taking the 3 components of the position along the XYZ axes, in that order.  */
  public static Position of(double x, double y, double z) {
    return new Position(x, y, z);
  }

  /** Factory method. The vector has 1 non-zero component, along the given spatial coordinate axis. */
  public static Position of(Axis axis, double value) {
    return new Position(axis, value);
  }
  
  /** The origin of the coordinate system. */
  public static Position origin() {
    return new Position(0.0, 0.0, 0.0);
  }

  /** Return an event having this position and the given <em>ct</em> coordinate. */
  public FourVector eventForTime(double ct) {
    return FourVector.from(ct, on(X), on(Y), on(Z), ApplyDisplaceOp.YES);
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
  
  private VectorImpl vec;
  
  private Position(double xComp, double yComp, double zComp) {
    vec = VectorImpl.of(xComp, yComp, zComp);
  }
  
  private Position(Axis axis, double value) {
    vec = VectorImpl.of(axis, value);
  }
}