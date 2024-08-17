package sr.core.vector4;

import static sr.core.Axis.CT;
import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;
import static sr.core.Util.mustBeSpatial;

import java.util.Map;

import sr.core.Axis;
import sr.core.Physics;
import sr.core.vector3.Direction;
import sr.core.vector3.Velocity;

/** Velocity as a time-like four-vector. */
public final class FourVelocity extends FourVector implements Builder<FourVelocity> {
  
  public static FourVelocity of(Velocity v) {
    return new FourVelocity(v.magnitude(), Direction.of(v));
  }

  /** @param β can be negative, but it will change the direction to its opposite. */
  public static FourVelocity of(double β, Direction direction) {
    return new FourVelocity(β, direction);
  }
  
  /** 
   @param β can be negative, but it will change the direction to its opposite.
   @param axis must be spatial. 
  */
  public static FourVelocity of(double β, Axis axis) {
    mustBeSpatial(axis);
    return new FourVelocity(β, Direction.of(axis));
  }

  public double β() { return β; }
  public Direction direction() { return direction; }
  
  public Velocity velocity() { return Velocity.of(β, direction); }
  /** The Lorentz factor, being the time component of the four-velocity. */
  public double Γ() {return on(CT);} 

  @Override public FourVelocity build(Map<Axis, Double> components) {
    //'reverse engineer' the data
    double Γ = components.get(CT);
    Velocity v = Velocity.of(
      components.get(X) / Γ, 
      components.get(Y) / Γ, 
      components.get(Z) / Γ
    );
    return FourVelocity.of(v);
  }
  
  private double β;
  private Direction direction;

  /** All construction must pass through these pearly gates. */
  private FourVelocity(double β, Direction direction) {
    this.β = β;
    double Γ = Physics.Γ(β);
    this.direction = direction;
    this.components.put(CT, Γ);
    this.components.put(X, Γ * direction.times(β).x());
    this.components.put(Y, Γ * direction.times(β).y());
    this.components.put(Z, Γ * direction.times(β).z());
  }
}