package sr.core.vector4;

import static sr.core.Axis.CT;
import static sr.core.Axis.X;
import static sr.core.Axis.Y;
import static sr.core.Axis.Z;
import static sr.core.Util.mustBeSpatial;

import java.util.Map;

import sr.core.Axis;
import sr.core.Util;
import sr.core.vector3.Direction;
import sr.core.vector3.PhaseGradient;
import sr.core.vector3.ThreeVector;

/**
 Phase-gradient (wave vector) <em>k<sup>i</sup></em> for a plane monochromatic wave with phase-velocity as the speed limit <em>c</em>.
 <P>This corresponds to a light wave travelling in a vacuum.
  
 <p>The general relation is <em>ω = c*k</em>. 
 <b>In this project <em>c=1</em>, so ω and <em>k</em> are numerically the same.
 This class considers them as interchangeable.</b>
   
 <P>The space components are the direction-vector multiplied by <em>k<em>(or ω).
*/
public final class FourPhaseGradient extends FourVector implements Builder<FourPhaseGradient> {
  
  /**
   Factory method.
   @param kω can be negative, but that will change the direction to its opposite. 
  */
  public static FourPhaseGradient of(double kω, Direction direction) {
    return new FourPhaseGradient(kω, direction);
  }
  
  /**
   Factory method taking any vector as source of the {@link Direction} unit vector.
   @param kω can be negative, but that will change the direction to its opposite. 
  */
  public static FourPhaseGradient of(double kω, ThreeVector vector) {
    return new FourPhaseGradient(kω, Direction.of(vector));
  }
  
  /**
   Factory method for case in which the direction is parallel to a spatial coordinate axis.
   @param kω can be negative, but that will change the direction to its opposite. 
   @param axis must be spatial.
  */
  public static FourPhaseGradient of(double kω, Axis axis) {
    mustBeSpatial(axis);
    return new FourPhaseGradient(kω, Direction.of(axis));
  }

  /** Unit vector in the direction of the phase-gradient (wave vector) <em>k</em>. */
  public Direction direction() {  return direction;  }

  /** The time-component, being the phase-frequency ω or the phase-gradient <em>k</em>. */
  public double kω() { return  on(CT); }

  /** The phase-gradient 3-vector <em>k</em>. */
  public PhaseGradient k() {
    return PhaseGradient.of(kω(), direction); 
  }
  
  @Override public FourPhaseGradient build(Map<Axis, Double> components) {
    double kω = components.get(CT);
    //direction is always a unit vector, and we can create it directly from the given components, and they will be rescaled
    Direction direction = Direction.of(
      components.get(X), 
      components.get(Y), 
      components.get(Z)
    ); 
    return new FourPhaseGradient(kω, direction);
  }
  
  private Direction direction;

  /** All construction must pass through here. */
  private FourPhaseGradient(double kω, Direction direction) {
    Util.mustHave(kω > 0, "kω must be positive: " + kω);
    this.direction = direction;
   //ω=ck, but here c=1
    this.components.put(CT, kω);
    this.components.put(X, direction.times(kω).x());
    this.components.put(Y, direction.times(kω).y());
    this.components.put(Z, direction.times(kω).z());
  }
}