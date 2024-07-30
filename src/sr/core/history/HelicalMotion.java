package sr.core.history;

import sr.core.Axis;
import sr.core.event.Event;
import sr.core.vector.Velocity;

/**
 History for a particle with mass moving uniformly in a helix.
 
 THIS CLASS IS PRELIMINARY; IT'S NOT DONE YET.
*/
public class HelicalMotion extends MoveableHistory {
  
  /*
   * zyz convention.
   * zyz, but the last rotation is 0, by convention? That might put Z parallel to Velocity. 
   * 
   * Start with the Euler angles, instead of computing them? 
   * 
   * https://easyspin.org/documentation/eulerangles.html
   * 
   * https://en.wikipedia.org/wiki/Helix
   *   arc length relates to velocity (arc length in unit time)
   *   radius, slope, and velocity
   *   no restriction on the acceleration
   *   input zyz Euler angles to define the directon; I already have rotation primitives
   */
  
  /**
   Constructor.
   @param velocity uniform motion to which circular motion is added, in a plane perpendicular to this velocity. Defines the direction of the axis of the helix.
   If this is a zero-vector, then the particle moves in a circle, not a helix.
   @param radius of the circular motion
   @param β speed of the circular motion around the axis of the helix. Must be a non-zero value in the range (-1,1). Negative values reverse the sense of the rotation.
   @param initialPhase of the circular motion must be in range (-2pi,+2pi).
  */
  private HelicalMotion(DeltaBase deltaBase, Velocity velocity, double radius, double β, Axis rotationalAxis, double initialPhase) {
    super(deltaBase);
  }
  
  @Override protected double Δct(double Δτ) {
    // TODO Auto-generated method stub
    return 0;
  }
  
  @Override protected Event Δevent(double Δct) {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override protected double Δτ(double Δct) {
    // TODO Auto-generated method stub
    return 0;
  }
  
  
  
  


}