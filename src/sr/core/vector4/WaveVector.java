package sr.core.vector4;

import static sr.core.Axis.CT;
import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;
import static sr.core.Util.mustBeSpatial;

import java.util.Map;

import sr.core.Axis;
import sr.core.vector3.Direction;
import sr.core.vector3.ThreeVector;

/**
 Wave vector <em>k<sup>i</sup></em>, with angular frequency and direction unit-vector. 
 The space components are the direction unit-vector multiplied by the frequency
 (neglecting factors of <em>c</em>, because in this project we use <em>c=1</em>).
*/
public final class WaveVector extends FourVector implements Builder<WaveVector> {
  
  /**
   Factory method.
   @param ω can be negative, but that will change the direction to its opposite. 
  */
  public static WaveVector of(double ω, Direction direction) {
    return new WaveVector(ω, direction);
  }
  
  /**
   Factory method taking any vector as source of the {@link Direction} unit vector.
   @param ω can be negative, but that will change the direction to its opposite. 
  */
  public static WaveVector of(double ω, ThreeVector vector) {
    return new WaveVector(ω, Direction.of(vector));
  }
  
  /**
   Factory method for case in which the direction is parallel to a spatial coordinate axis.
   @param ω can be negative, but that will change the direction to its opposite. 
   @param axis must be spatial.
  */
  public static WaveVector of(double ω, Axis axis) {
    mustBeSpatial(axis);
    return new WaveVector(ω, Direction.of(axis));
  }

  /** Unit vector in the direction of the k 3-vector. */
  public Direction direction() {  return direction;  }

  /** The time-component, being the angular frequency ω. */
  public double ω() { return  on(CT); }

  /** The k 3-vector, being the angular frequency ω times the direction unit-vector. */
  public ThreeVector k() {
    return spatialComponents();
  }
  
  @Override public WaveVector build(Map<Axis, Double> components) {
    double ω = components.get(CT);
    //direction is always a unit vector, and we can create it directly from the given components, and they will be rescaled
    Direction direction = Direction.of(
      components.get(X), 
      components.get(Y), 
      components.get(Z)
    ); 
    return new WaveVector(ω, direction);
  }
  
  private Direction direction;

  /** All construction must pass through here. */
  private WaveVector(double ω, Direction direction) {
    this.direction = direction;
    this.components.put(CT, ω);
    this.components.put(X, direction.times(ω).x());
    this.components.put(Y, direction.times(ω).y());
    this.components.put(Z, direction.times(ω).z());
  }
}